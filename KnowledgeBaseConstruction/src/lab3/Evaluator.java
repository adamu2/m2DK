package lab3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;


/**
 * Skeleton for an evaluator of the type extraction
 */
public class Evaluator {


	
	public double calculateRecall(File results, File goldStandard) throws IOException
	{
		double captured = 0;
		double count = 0;
		BufferedReader br = new BufferedReader(new FileReader(results));
		BufferedReader br2 = new BufferedReader(new FileReader(goldStandard));

		ArrayList<String> elements = new ArrayList<>();
		String line;
		String line2;
		while ((line2 = br2.readLine()) != null) {
			elements.add(line2.replaceAll(" ",""));
			count++;
		}
		while ((line = br.readLine()) != null) {
			if(elements.contains(line.replaceAll(" ","")))captured++;
		}
		br.close();
		br2.close();
		return (captured/count);
	}

	
	public double calculatePrecission(File results, File goldStandard) throws IOException
	{
		double captured = 0;
		double count = 0;
		BufferedReader br = new BufferedReader(new FileReader(results));
		BufferedReader br2 = new BufferedReader(new FileReader(goldStandard));

		ArrayList<String> elements = new ArrayList<>();
		String line;
		String line2;
		while ((line2 = br2.readLine()) != null) {
			elements.add(line2.replaceAll(" ",""));
		}
		
		while ((line = br.readLine()) != null) {
			count++;
			if(elements.contains(line.replaceAll(" ","")))captured++;
		}
		br.close();
		br2.close();
		return (captured/count);
	}
	/**
	 * Takes as arguments (1) the gold standard and (2) the output of the type
	 * extraction as a file. Prints to the screen one line with the precision
	 * and one line with the recall.
     * When computing recall, the denominator should not take into account the
     * gold standard items with "none".
	 */
	public static void main(String[] args) throws Exception {
		 //args = new String[] {
		 //"C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab3/goldstandard-sample.tsv",
		 //"C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab3/result.tsv" };
		 Evaluator evaluator = new Evaluator();
		 System.out.println("recall = "+evaluator.calculateRecall(new File(args[1]), new File(args[0])));
		 System.out.println("precision = "+evaluator.calculatePrecission(new File(args[1]), new File(args[0])));
	}
	
	
}
