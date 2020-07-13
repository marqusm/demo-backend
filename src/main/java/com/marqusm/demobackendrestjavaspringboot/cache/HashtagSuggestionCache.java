package com.marqusm.demobackendrestjavaspringboot.cache;

import com.marqusm.demobackendrestjavaspringboot.model.database.HashtagPopularityRecord;
import com.marqusm.demobackendrestjavaspringboot.repository.HashtagPopularityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/** @author : Marko Mišković */
@RequiredArgsConstructor
@Component
public class HashtagSuggestionCache {
  private final HashtagPopularityRepository hashtagPopularityRepository;

  private final Map<String, List<String>> valueMap = new HashMap<>();

  public List<String> getSuggestion(String pattern) {
    return Optional.ofNullable(valueMap.get(pattern))
        .orElseGet(
            () -> {
              var suggestions =
                  hashtagPopularityRepository.getSuggestions(pattern).stream()
                      .map(HashtagPopularityRecord::getName)
                      .collect(Collectors.toList());
              valueMap.put(pattern, suggestions);
              return suggestions;
            });
  }

  public void invalidate(String hashtag) {
    valueMap.keySet().stream()
        .filter(hashtag::startsWith)
        .collect(Collectors.toList())
        .forEach(valueMap::remove);
  }
}
