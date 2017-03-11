package lab6;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

/**
 * Skeleton class for a program that maps the entities from one KB to the
 * entities of another KB.
 * 
 * @author Fabian
 *
 */
public class EntityMapper {

	private static double setsFromSameEntity(Set<String> set1,Set<String> set2)
	{
		double count=0.0;
		for(String p1:set1)
		{
			for(String p2:set2)
			{
				if(p1.equals(p2))
				{
					count++;
				}
			}
		}
		return count;
	}
	
	private static double compareEntities(String entity1, String entity2, Map<String, Map<String, Set<String>>> facts1,Map<String, Map<String, Set<String>>> facts2 )
	{
		Map<String, Set<String>> f1=facts1.get(entity1);
		Map<String, Set<String>> f2=facts2.get(entity2);
		double count=0;
		for(String keyf1:f1.keySet())
		{
			for(String keyf2:f2.keySet())
			{
				    count +=setsFromSameEntity(f1.get(keyf1),f2.get(keyf2));
			}
		}

		return count;
	}

	
    /**
     * Takes as input (1) one knowledge base (2) another knowledge base and (3)
     * an output file.
     * 
     * Writes into the output file "entity1 TAB entity2 NEWLINE", if the first
     * entity from the first knowledge base is the same as the second entity
     * from the second knowledge base. Output 0 or 1 line per entity1.
     */
    public static void main(String[] args) throws IOException {
        // Uncomment for your convenience. Comment it again before submission!
        
//          args = new String[] {
//          "C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab6/yago-anonymous.tsv",
//          "C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab6/dbpedia.tsv",
//          "C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab6/result.tsv" };
         
        KnowledgeBase kb1 = new KnowledgeBase(new File(args[0]));
        KnowledgeBase kb2 = new KnowledgeBase(new File(args[1]));
        try (Writer result = new OutputStreamWriter(new FileOutputStream(args[2]), "UTF-8")) {
            for (String entity1 : kb1.facts.keySet()) {
                String mostLikelyCandidate = null;
                double max = 0;
                for (String entity2 : kb2.facts.keySet()) {
                	double temp = EntityMapper.compareEntities(entity1,entity2,kb1.facts,kb2.facts);
                	if(temp>max)
                	{
                		max = temp;
                		mostLikelyCandidate = entity2;
                	}
                }
                if (mostLikelyCandidate != null) {
                    result.write(entity1 + "\t" + mostLikelyCandidate + "\n");
                }
            }
        }
    }
}
