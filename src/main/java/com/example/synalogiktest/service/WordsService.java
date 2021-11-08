package com.example.synalogiktest.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    /**
     * Reads file and break it up into words.
     * @param file
     * @return List<String>
     */
    public List<String> processFile(MultipartFile file) {
        List<String> words = new ArrayList<String>();
        if (!file.getContentType().equals(TEXT_CONTENT_TYPE)) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported uploaded file type");
        }

        BufferedReader bufferedReader;
        try {
            String line;
            InputStream inputStream = file.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            // Read file line by line, as opposed to a large String, which may have performance issues if reading large file
            while ((line = bufferedReader.readLine()) != null) {
                String[] wordsInLine = line.split(" ");
                words.addAll(Arrays.asList(wordsInLine));
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IO Exception");
        }
        return words;
    }

    /**
     * Returns the output string containing statistics around the file content.
     * @return String
     */
    public String getOutput(List<String> words) {
        // StringBuilder used here as we need the object to be mutable and unsynchronised.
        StringBuilder output = new StringBuilder();
        appendWordCount(output, words);
        appendAverageWordLength(output, words);
        Map<Integer, Integer> wordLengthFrequency = calculateWordLengthFrequency(words);
        appendNumberOfWordsByLength(output, wordLengthFrequency);
        appendMaxOccurringWordLength(output, wordLengthFrequency);
       
        return output.toString();
    }

    /**
     * @param StringBuilder output
     * @param List<String> words
     */
    private void appendWordCount(StringBuilder output, List<String> words) {
        output.append("Word count = " + words.size() + "\n");
    }

    /**
     * @param StringBuilder output
     * @param List<String> words
     */
    private void appendAverageWordLength(StringBuilder output, List<String> words) {
        Float averageWordLength = 0f;
        int totalWordCount = 0;
        DecimalFormat df = new DecimalFormat("#.000");

        for (int i=0;i < words.size();i++)
        {
            String word = words.get(i);
            if ((word.matches("^.*[a-zA-Z0-9]+.*$"))) {
                totalWordCount += words.get(i).length();
            }
        }

        if (words.size() != 0) {
            averageWordLength = (float) totalWordCount / words.size();
        }
        averageWordLength = Float.valueOf(df.format(averageWordLength));
        output.append("Average word length = " + averageWordLength + "\n");
    }

    /**
     * 
     * @param List<String> words
     * @return Map<Integer,Integer>
     */
    private Map<Integer,Integer> calculateWordLengthFrequency(List<String> words) {
        // A map of (key) word length, (value) frequency
        Map<Integer,Integer> frequencyOfWordLength = new HashMap<Integer,Integer>();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            // If the last letter is a full stop, then don't include it as part of the word
            if (word.substring(word.length() - 1).equals(".")) {
                word = word.substring(0, word.length() - 1);
            }
            Integer previousValue = frequencyOfWordLength.get(word.length()) != null ? frequencyOfWordLength.get(word.length()) : 0;
            frequencyOfWordLength.put(word.length(), previousValue + 1);
        }
        return frequencyOfWordLength;
    }

    /**
     * @param StringBuilder output
     * @param Map<Integer,Integer> frequencyOfWordLength
     */
    private void appendNumberOfWordsByLength(StringBuilder output, Map<Integer,Integer> frequencyOfWordLength) {
        for (Map.Entry<Integer,Integer> entry : frequencyOfWordLength.entrySet()) {
            output.append("Number of words of length " + entry.getKey() + " is " + entry.getValue() + "\n");
        }
    }

    /**
     * @param StringBuilder output
     * @param Map<Integer,Integer> frequencyOfWordLength
     */
    private void appendMaxOccurringWordLength(StringBuilder output, Map<Integer,Integer> frequencyOfWordLength) {
        if (frequencyOfWordLength.size() == 0) {
            return;
        }
        int maxWordLengthFrequency = Collections.max(frequencyOfWordLength.values());
        List<String> maxWordLengths = new ArrayList<String>();


        for (Map.Entry<Integer,Integer> entry : frequencyOfWordLength.entrySet()) {
            if (entry.getValue() == maxWordLengthFrequency) {
                maxWordLengths.add(entry.getKey().toString());
            }
        }
        
        output.append(
            "The most frequently occurring word length is " + maxWordLengthFrequency +
            ", for word lengths of " + String.join(" & ", maxWordLengths)
        );
    }
}
