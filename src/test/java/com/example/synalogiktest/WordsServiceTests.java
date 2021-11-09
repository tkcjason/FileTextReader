package com.example.synalogiktest;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;

import com.example.synalogiktest.service.WordsService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
class WordsServiceTests {

	@Test
	void TestProcessFileThrowsExceptionWhenFileContentTypeIsJpeg() {
		WordsService wordsService = new WordsService();
		MultipartFile mockFile = new MockMultipartFile("file.txt",
            "file.txt",
            "image/jpeg",
            "contents".getBytes(StandardCharsets.UTF_8));
		assertThrows(ResponseStatusException.class, () -> {
        	wordsService.processFile(mockFile);
		});
	}

	@Test
	void TestProcessFileThrowsExceptionWhenFileContentTypeIsPdf() {
		WordsService wordsService = new WordsService();
		MultipartFile mockFile = new MockMultipartFile("file.txt",
            "file.txt",
            "application/pdf",
            "contents".getBytes(StandardCharsets.UTF_8));
		assertThrows(ResponseStatusException.class, () -> {
        	wordsService.processFile(mockFile);
		});
	}

	@Test
	void TestProcessFileSucceedsWhenReadingValidTextFile() {
		WordsService wordsService = new WordsService();
		String contents = "Hello world & good morning. The date is 18/05/2016";
		MultipartFile mockFile = new MockMultipartFile(".txt",
            "file.txt",
            "text/plain",
            contents.getBytes(StandardCharsets.UTF_8));
		
		String result = wordsService.processFile(mockFile);
		String expected = "Word count = 9\n"
			+ "Average word length = 4.556\n"
			+ "Number of words of length 1 is 1\n"
			+ "Number of words of length 2 is 1\n"
			+ "Number of words of length 3 is 1\n"
			+ "Number of words of length 4 is 2\n"
			+ "Number of words of length 5 is 2\n"
			+ "Number of words of length 7 is 1\n"
			+ "Number of words of length 10 is 1\n"
			+ "The most frequently occurring word length is 2, for word lengths of 4 & 5\n";
		Assertions.assertEquals(result, expected);
	}

	@Test
	void TestProcessFileSucceedsWhenReadingValidTextFileWithPunctuation() {
		WordsService wordsService = new WordsService();
		String contents = "***** \"These daughters' words.\"";
		MultipartFile mockFile = new MockMultipartFile(".txt",
            "file.txt",
            "text/plain",
            contents.getBytes(StandardCharsets.UTF_8));
		
		String result = wordsService.processFile(mockFile);
		String expected = "Word count = 4\n"
			+ "Average word length = 6.25\n"
			+ "Number of words of length 5 is 3\n"
			+ "Number of words of length 10 is 1\n"
			+ "The most frequently occurring word length is 3, for word lengths of 5\n";
		Assertions.assertEquals(result, expected);
	}

	@Test
	void TestProcessFileSucceedsWhenReadingValidTextFileWithFormattedNumbers() {
		WordsService wordsService = new WordsService();
		String contents = "41.6% -1.3 $2.30 01-01-1990";
		MultipartFile mockFile = new MockMultipartFile(".txt",
            "file.txt",
            "text/plain",
            contents.getBytes(StandardCharsets.UTF_8));
		
		String result = wordsService.processFile(mockFile);
		String expected = "Word count = 4\n"
			+ "Average word length = 6.0\n"
			+ "Number of words of length 4 is 1\n"
			+ "Number of words of length 5 is 2\n"
			+ "Number of words of length 10 is 1\n"
			+ "The most frequently occurring word length is 2, for word lengths of 5\n";
		Assertions.assertEquals(result, expected);
	}
}
