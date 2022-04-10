package com.ikhokha.techcheck;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		
		Map<String, Integer> totalResults = new HashMap<>();
				
		File commentFolder = new File("docs");
		File[] commentFiles = commentFolder.listFiles((d, n) -> n.endsWith(".txt"));
		
		for (File commentFile : commentFiles) {
			CommentAnalyzer commentAnalyzer = new CommentAnalyzer(commentFile);
			Map<String, Integer> fileResults = commentAnalyzer.analyze();
			addReportResults(fileResults, totalResults);						
		}
		
		System.out.println("RESULTS\n=======");
		totalResults.forEach((k,v) -> System.out.println(k + " : " + v));
	}
	
	/**
	 * This method adds the result counts from a source map to the target map 
	 * @param fileMap the source map
	 * @param totalMap the target map
	 */
	private static void addReportResults(Map<String, Integer> fileMap, Map<String, Integer> totalMap) {

		for (Map.Entry<String, Integer> entry : fileMap.entrySet()) {
			var key = entry.getKey();
			var metricsCount = entry.getValue();

			totalMap.putIfAbsent(key, 0);
			totalMap.put(key, totalMap.get(key) + metricsCount);
		}
		
	}

}
