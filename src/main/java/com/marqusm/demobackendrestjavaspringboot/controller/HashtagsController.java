package com.marqusm.demobackendrestjavaspringboot.controller;

import com.marqusm.demobackendrestjavaspringboot.model.dto.HashtagCollectionResponse;
import com.marqusm.demobackendrestjavaspringboot.service.HashtagService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** @author : Marko Mišković */
@AllArgsConstructor
@RestController
@RequestMapping("/hashtags")
public class HashtagsController {

  private final HashtagService hashtagService;

  @GetMapping("/suggestions")
  HashtagCollectionResponse getSuggestedHashtags(@RequestParam("pattern") String pattern) {
    return hashtagService.getSuggestions(pattern);
  }
}
