package com.example.synalogiktest.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
* Words Service to read a plain text file and display statistics around the content.
* @author Jason Tse
* 
*/
@Service
public class WordsService {
    private final static String TEXT_CONTENT_TYPE = "text/plain";

    private List<String> words = new ArrayList<String>();
    
    /**
     * Reads file and break it up into words.
     * @param file
     */
    public void processFile(MultipartFile file) {
        if (!file.getContentType().equals(TEXT_CONTENT_TYPE)) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported uploaded file type");
        }

        BufferedReader bufferedReader;
        try {
            String line;
            InputStream inputStream = file.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                String[] wordsInLine = line.split(" ");
                words.addAll(Arrays.asList(wordsInLine));
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IO Exception");
        }
    }

    /**
     * Returns the output string containing statistics around the file content.
     * @return String
     */
    public String getOutput() {
        // StringBuilder used here as we need the object to be mutable and unsynchronised.
        StringBuilder output = new StringBuilder();
        output.append("Word count = " + getWordCount(words) + "\n");
        output.append("Average word length = " + getAverageWordLength(words)+ "\n");
        return output.toString();
    }

    /**
     * @param wordsArray
     * @return
     */
    private int getWordCount(List<String> wordsArray) {
        return wordsArray.size();
    }

    private float getAverageWordLength(List<String> wordsArray) {
        Float averageWordLength = 0f;
        int totalWordCount = 0;
        DecimalFormat df = new DecimalFormat("#.000");

        // Return early if empty, since we don't want to divide by 0.
        if (0 == wordsArray.size()) {
            return averageWordLength;
        }
        for (int i=0;i < wordsArray.size();i++)
        {
            String word = wordsArray.get(i);
            if ((word.matches("^.*[a-zA-Z0-9]+.*$"))) {
                totalWordCount += wordsArray.get(i).length();
            }
        }
        averageWordLength = (float) totalWordCount / wordsArray.size();
        return Float.valueOf(df.format(averageWordLength));
    }
}
