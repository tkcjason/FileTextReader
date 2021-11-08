package com.example.synalogiktest.controller;

import com.example.synalogiktest.service.WordsService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ReadFileController {

    private final WordsService wordsService;

	public ReadFileController (WordsService wordsService) {
		this.wordsService = wordsService;
	}

    @PostMapping("/file")
	public String hello(@RequestParam("file") MultipartFile file) {
        wordsService.processFile(file);
		return wordsService.getOutput();

	}
}
