package com.marqusm.demobackendrestjavaspringboot.service;

import com.marqusm.demobackendrestjavaspringboot.cache.HashtagSuggestionCache;
import com.marqusm.demobackendrestjavaspringboot.model.dto.HashtagCollectionResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/** @author : Marko Mišković */
@AllArgsConstructor
@Service
public class HashtagService {

  private final HashtagSuggestionCache hashtagSuggestionCache;

  public HashtagCollectionResponse getSuggestions(String pattern) {
    return HashtagCollectionResponse.of(hashtagSuggestionCache.getSuggestion(pattern));
  }
}
