/*
 * Copyright 2015 Megan Galloway
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.megallo.markoverator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.github.megallo.markoverator.TextUtils.removeExplicitNewlines;
import static com.github.megallo.markoverator.TextUtils.removeMentions;
import static com.github.megallo.markoverator.TextUtils.removePunctuation;
import static com.github.megallo.markoverator.TextUtils.removeUnmatchedParentheses;
import static com.github.megallo.markoverator.TextUtils.removeUrls;

/**
 * Build a bigram model out of text and generate some sentences.
 */
public class MarkovGenerator {

    private static final Logger loggie = LoggerFactory.getLogger(MarkovGenerator.class);

    /**
     * Do all the things! Currently takes file name as single argument.
     * Put the file in src/main/resources and give me the file name without path
     */
    public static void main(String[] args) throws IOException {
        MarkovGenerator mg = new MarkovGenerator();
        Bigrammer bigrams = new Bigrammer();
        bigrams.buildModel(mg.readAndCleanFile(args[0]));
        loggie.info(bigrams.generateRandom());
        loggie.info(bigrams.generateRandom());
        loggie.info(bigrams.generateRandom());
        loggie.info(bigrams.generateRandom());
        loggie.info(bigrams.generateRandom());
        loggie.info(bigrams.generateRandom());
        loggie.info(bigrams.generateRandom());
        loggie.info(bigrams.generateRandom());
        loggie.info(bigrams.generateRandom());
        loggie.info(bigrams.generateRandom());

    }

    private List<List<String>> readAndCleanFile(String filename) throws IOException {
        List<List<String>> cleanedTokenizedLines = new ArrayList<>();

        BufferedReader br = null;
        try {
            File file = new File(getClass().getClassLoader().getResource(filename).getFile());
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String line;
            while ((line = br.readLine()) != null)   {
                cleanedTokenizedLines.add(cleanUpLine(line));
            }
        } catch (FileNotFoundException e) {
            loggie.error("Unable to find file " + filename, e);
        } finally {
            if (br != null) {
                br.close();
            }
        }

        return cleanedTokenizedLines;
    }

    private List<String> cleanUpLine(String sentence) {
        String[] split = sentence.toLowerCase().split("\\s+"); //lowercase, break on whitespace
        List<String> splitSentence = new LinkedList<>(Arrays.asList(split));
        splitSentence = removeUrls(splitSentence);
        splitSentence = removeMentions(splitSentence);
        splitSentence = removeExplicitNewlines(splitSentence);
        splitSentence = removeUnmatchedParentheses(splitSentence);
        splitSentence = removePunctuation(splitSentence);

        if (loggie.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (String word : splitSentence) {
                sb.append(word).append(" ");
            }
            loggie.debug(sb.toString());
        }

        return splitSentence;
    }
}