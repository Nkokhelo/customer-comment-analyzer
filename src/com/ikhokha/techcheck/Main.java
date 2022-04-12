package com.ikhokha.techcheck;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

	public static void main(String[] args) {
		var totalResults = new HashMap<String, Integer>();
		var commentFolder = new File("docs");
		var commentFiles = commentFolder.listFiles((d, n) -> n.endsWith(".txt"));
		var metricsList = getMetricsList();
		ExecutorService excecutor = Executors.newFixedThreadPool(commentFiles.length);

		for (var commentFile : commentFiles) {
			try {
				Future<Map<String, Integer>> metricsFuture = excecutor.submit(new CommentAnalyzer(commentFile, metricsList));
				var fileResults = metricsFuture.get();
				addReportResults(fileResults, totalResults);
			}
			catch(Exception e) {
				e.printStackTrace();
			}						
		}
		
		excecutor.shutdown();
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

	/**
	 * This method initialize metrics list to used to analyze the comments documents 
	 */
	private static List<Metrics> getMetricsList() {
		var metricsList = new ArrayList<Metrics>();
		metricsList.add(new Metrics("SHORTER_THAN_15", MetricsType.LESS, 15));
		metricsList.add(new Metrics("MOVER_MENTIONS", MetricsType.CONTAINS, "Mover"));
		metricsList.add(new Metrics("SHAKER_MENTIONS", MetricsType.CONTAINS, "Shaker"));
		metricsList.add(new Metrics("QUESTIONS", MetricsType.CONTAINS, "?"));
		metricsList.add(new Metrics("SPAM", MetricsType.CONTAINS, "http"));
		return metricsList;
	}

}
