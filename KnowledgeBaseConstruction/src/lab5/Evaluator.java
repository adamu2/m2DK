package lab5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Skeleton for an evaluator of the type extraction
 */
public class Evaluator {

	private void calculateMetrics(File results, File goldStandard) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(goldStandard));
		BufferedReader br2 = new BufferedReader(new FileReader(results));

		Map<String,String> elements = new HashMap<>();
		Map<String,String> elements2 = new HashMap<>();
		String line;
		String line2;
		
		while ((line = br.readLine()) != null) {
			String[] substrings = line.split("\t");
			elements.put(substrings[0], line.replaceAll(substrings[0]+"\t", ""));
		}
		while ((line2 = br2.readLine()) != null) {
			String[] substrings = line2.split("\t");
			if(elements.containsKey(substrings[0]))
			{
				elements2.put(substrings[0], substrings[1]);
			}
		}
		System.out.println("recall = "+calculateRecall(elements,elements2));
		System.out.println("precision = "+calculatePrecission(elements,elements2));
		br.close();
		br2.close();
	}
	
	private double calculateRecall(Map<String,String> goldStandard, Map<String,String> results) 
	{
		double captured = 0;
		for(String key: results.keySet())
		{
			if(goldStandard.containsKey(key)&&goldStandard.get(key).contains(results.get(key))){captured++;}
			if(goldStandard.containsKey(key)&&results.containsKey(key))
			{
				System.out.println(key+"::"+goldStandard.get(key)+":"+results.get(key));
			}
			
		}
		return captured/goldStandard.size();
	}

	
	private double calculatePrecission(Map<String,String> goldStandard, Map<String,String> results) 
	{
		double captured = 0;
		for(String key: results.keySet())
		{
			if(goldStandard.containsKey(key)&&goldStandard.get(key).contains(results.get(key))){captured++;}
		}
		return captured/results.size();
	}
	/**
	 * Takes as arguments (1) the gold standard and (2) the output of the type
	 * extraction as a file. Prints to the screen one line with the precision
	 * and one line with the recall.
     * When computing recall, the denominator should not take into account the
     * gold standard items with "none".
	 */
	public static void main(String[] args) throws Exception {
		 args = new String[] {
		 "C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab5/results.txt",
		 "C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab5/gold-standard-sample.tsv" };
		 Evaluator evaluator = new Evaluator();
		 evaluator.calculateMetrics(new File(args[0]), new File(args[1]));
	}
	
	
}
