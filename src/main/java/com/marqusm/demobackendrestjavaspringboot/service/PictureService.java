package com.marqusm.demobackendrestjavaspringboot.service;

import com.marqusm.demobackendrestjavaspringboot.annotation.CacheAffecting;
import com.marqusm.demobackendrestjavaspringboot.annotation.Transactional;
import com.marqusm.demobackendrestjavaspringboot.enumeration.PictureDisplayableStatus;
import com.marqusm.demobackendrestjavaspringboot.enumeration.PicturePurchasableStatus;
import com.marqusm.demobackendrestjavaspringboot.model.common.TokenData;
import com.marqusm.demobackendrestjavaspringboot.model.database.FileRecord;
import com.marqusm.demobackendrestjavaspringboot.model.database.HashtagRecord;
import com.marqusm.demobackendrestjavaspringboot.model.database.PictureRecord;
import com.marqusm.demobackendrestjavaspringboot.model.database.PictureToHashtagRecord;
import com.marqusm.demobackendrestjavaspringboot.model.dto.PictureDisplayableStatusRequest;
import com.marqusm.demobackendrestjavaspringboot.model.dto.PictureResponse;
import com.marqusm.demobackendrestjavaspringboot.repository.FileRepository;
import com.marqusm.demobackendrestjavaspringboot.repository.HashtagRepository;
import com.marqusm.demobackendrestjavaspringboot.repository.PictureRepository;
import com.marqusm.demobackendrestjavaspringboot.repository.PictureToHashtagRepository;
import com.marqusm.demobackendrestjavaspringboot.util.ModelMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/** @author : Marko Mišković */
@AllArgsConstructor
@Service
public class PictureService {

  private final TokenData tokenData;
  private final FileRepository fileRepository;
  private final PictureRepository pictureRepository;
  private final HashtagRepository hashtagRepository;
  private final PictureToHashtagRepository pictureToHashtagRepository;

  @SneakyThrows
  @CacheAffecting
  @Transactional
  public PictureResponse createPicture(MultipartFile file, String name, List<String> hashtags) {
    val fileRecord = persistFile(file.getOriginalFilename(), file.getSize(), file.getBytes());
    val pictureRecord = persistPicture(fileRecord.getId(), name);
    val existingHashtagRecords = fetchHashtags(hashtags);
    val newHashtags = calculateNewHashtags(existingHashtagRecords, hashtags);
    val newHashtagRecords = persistHashtags(newHashtags);
    val hashtagRecords = mergeAndSortHashtags(existingHashtagRecords, newHashtagRecords, hashtags);
    persistPictureToHash(hashtagRecords, pictureRecord.getId());
    return ModelMapper.toPictureResponse(pictureRecord, hashtagRecords);
  }

  @Transactional
  public List<PictureResponse> getAllPictures(PictureDisplayableStatus displayableStatus) {
    List<PictureRecord> pictureRecords = fetchPictureRecords(displayableStatus);
    val hashtagRecordMap = fetchHashtagRecordMap(pictureRecords);
    return calculatePictureResponse(pictureRecords, hashtagRecordMap);
  }

  @Transactional
  public void approvePicture(PictureDisplayableStatusRequest pictureApproveRequest) {
    pictureRepository.updateApprove(
        pictureApproveRequest.getPictureId(), pictureApproveRequest.getDisplayableStatus());
  }

  private FileRecord persistFile(String filename, long size, byte[] content) {
    return fileRepository.create(FileRecord.of(null, filename, size, content));
  }

  private PictureRecord persistPicture(UUID fileId, String name) {
    return pictureRepository.create(
        PictureRecord.of(
            null,
            tokenData.getAccountId(),
            fileId,
            name,
            PictureDisplayableStatus.INITIAL,
            PicturePurchasableStatus.INITIAL));
  }

  private List<HashtagRecord> fetchHashtags(List<String> hashtags) {
    return hashtagRepository.readByNames(hashtags);
  }

  private List<String> calculateNewHashtags(
      List<HashtagRecord> existingHashtagRecords, List<String> newHashtags) {
    val existingHashtags =
        existingHashtagRecords.stream().map(HashtagRecord::getName).collect(Collectors.toSet());
    return newHashtags.stream()
        .filter(hashtag -> !existingHashtags.contains(hashtag))
        .collect(Collectors.toList());
  }

  private List<HashtagRecord> persistHashtags(List<String> hashtags) {
    return hashtagRepository.createInBatch(
        hashtags.stream()
            .map(hashtag -> HashtagRecord.of(null, hashtag))
            .collect(Collectors.toList()));
  }

  private List<HashtagRecord> mergeAndSortHashtags(
      List<HashtagRecord> originalHashtagRecords,
      List<HashtagRecord> addedHashtagRecords,
      List<String> allHashtags) {
    val finalList = new LinkedList<>(originalHashtagRecords);
    finalList.addAll(addedHashtagRecords);
    finalList.sort(Comparator.comparingInt(a -> allHashtags.indexOf(a.getName())));
    return finalList;
  }

  private void persistPictureToHash(List<HashtagRecord> hashtagRecords, UUID pictureId) {
    pictureToHashtagRepository.createInBatch(
        hashtagRecords.stream()
            .map(hashtagRecord -> PictureToHashtagRecord.of(pictureId, hashtagRecord.getId()))
            .collect(Collectors.toList()));
  }

  private List<PictureRecord> fetchPictureRecords(
    PictureDisplayableStatus pictureDisplayableStatus) {
    return switch (Objects.requireNonNull(tokenData.getAccountRole())) {
        case ADMIN -> pictureRepository.findByExample(
            PictureRecord.of(null, null, null, null, pictureDisplayableStatus, null));
        case GENERAL -> pictureRepository.findByExample(
            PictureRecord.of(
                null,
                tokenData.getAccountId(),
                null,
                null,
                PictureDisplayableStatus.APPROVED,
                null));
      };
  }

  private Map<UUID, List<HashtagRecord>> fetchHashtagRecordMap(List<PictureRecord> pictureRecords) {
    return hashtagRepository.readByPictureIds(
        pictureRecords.stream().map(PictureRecord::getId).collect(Collectors.toList()));
  }

  private List<PictureResponse> calculatePictureResponse(
      List<PictureRecord> pictureRecords, Map<UUID, List<HashtagRecord>> hashtagRecordMap) {
    return pictureRecords.stream()
        .map(
            pictureRecord ->
                ModelMapper.toPictureResponse(
                    pictureRecord, hashtagRecordMap.get(pictureRecord.getId())))
        .collect(Collectors.toList());
  }
}
