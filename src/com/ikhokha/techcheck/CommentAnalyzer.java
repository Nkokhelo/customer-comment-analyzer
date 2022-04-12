package com.ikhokha.techcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class CommentAnalyzer implements Callable<Map<String, Integer>> {
    private final File file;
    public List<Metrics>  metricsList;
    
    public CommentAnalyzer(File file, List<Metrics> metricsList) {
        this.file = file;
        this.metricsList = metricsList;
    }

    @Override
    public Map<String, Integer> call() throws Exception {
        return analyze();
    }

    public Map<String, Integer> analyze() {
        var resultsMap = new HashMap<String, Integer>();
        try (var reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (Metrics metrics : metricsList) {
                    if(metrics.testLine(line)) {
                        incOccurrence(resultsMap, metrics.getKey());
                    }
                }
            }
        } catch (FileNotFoundException  e) {
            System.out.println("File not found: " + file.getAbsolutePath());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO Error processing file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
        return resultsMap;
    }

    /**
     * This method increments a counter by 1 for a match type on the countMap.
     * Uninitialized keys will be set to 1
     *
     * @param countMap the map that keeps track of counts
     * @param key      the key for the value to increment
     */
    private void incOccurrence(Map<String, Integer> countMap, String key) {
        countMap.putIfAbsent(key, 0);
        countMap.put(key, countMap.get(key) + 1);
    }



}
