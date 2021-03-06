package com.example.synalogiktest.controller;

import com.example.synalogiktest.service.WordsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ReadFileController {

    private final WordsService wordsService;

	public ReadFileController (WordsService wordsService) {
		this.wordsService = wordsService;
	}

    @GetMapping("/file")
	public String hello(@RequestParam("file") MultipartFile file) {
		return wordsService.processFile(file);

	}
}
