package com.marqusm.demobackendrestjavaspringboot.controller;

import com.marqusm.demobackendrestjavaspringboot.enumeration.PictureDisplayableStatus;
import com.marqusm.demobackendrestjavaspringboot.model.dto.*;
import com.marqusm.demobackendrestjavaspringboot.service.PictureService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Arrays;

/** @author : Marko Mišković */
@AllArgsConstructor
@RestController
@RequestMapping("/pictures")
public class PicturesController {

  private final PictureService pictureService;

  @PostMapping
  PictureResponse createPicture(
      @RequestParam("file") MultipartFile file,
      @RequestParam("name") String name,
      @RequestParam("hashtags") String hashtags) {
    return pictureService.createPicture(file, name, Arrays.asList(hashtags.split(",")));
  }

  @GetMapping
  PictureCollectionResponse getPictures(
      @RequestParam(name = "displayable_status", required = false)
          PictureDisplayableStatus displayableStatus) {
    return PictureCollectionResponse.of(pictureService.getAllPictures(displayableStatus));
  }

  @PutMapping
  PictureResponse updatePicture(@RequestBody @Valid PictureRequest pictureRequest) {
    return null;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/approve")
  void approvePicture(@RequestBody @Valid PictureDisplayableStatusRequest pictureApproveRequest) {
    pictureService.approvePicture(pictureApproveRequest);
  }

  @PostMapping("/like")
  void likePicture(@RequestBody @Valid PictureLikeRequest pictureLikeRequest) {}
}
