package lab5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Empty skeleton for a location extractor with POS tags.
 * 
 * @author Fabian M. Suchanek
 */
public class POSLocationExtractor {

    /**
     * Given a POS-tagged Wikipedia article, returns extract wherever possible
     * the location of a geographic entity. For example, from a page starting
     * with "Leicester/NNP is/VBZ a/DT city/NN in/IN the/DT Midlands/NNP", you
     * should extract "Midlands". 
     * 
     * Try to extract exactly the region, country, or
     * city. Do not extract locations for non-geographical entities.
     * You can also skip articles by returning NULL.
     */
    public static String findLocation(Page nextPage) {
        
        String[] locationIndicators= new String [] {"city", "country", "state", "town", "capital", "area","duchy"};
        
        String ret ="";
        List<String> geoWords = new ArrayList<>();
        List<String> geoTags = new ArrayList<>();
        boolean isLocation = false;
        for(String wordtag: nextPage.content.split(" "))
        {
        	if(wordtag.split("/")!=null&&wordtag.split("/").length==2)
        	{
        		String word = wordtag.split("/")[0].toLowerCase();
        		String tag = wordtag.split("/")[1].toLowerCase();
        		for(String indicator:locationIndicators)
        		{
        			if(indicator.equals(word.toLowerCase())){
        				isLocation = true;    				
        			}
        		}
        		if(isLocation)
        		{
        			geoWords.add(word);
        			geoTags.add(tag);
        		}
        	}
        }
        boolean afterIn=false;
        int state = 0;
        for(int i = 0; i <geoTags.size();i++)
        {
        	if((state==0||state==1)&&afterIn && (geoTags.get(i).equals("nnp")||geoTags.get(i).equals("nnps")))
        	{
        		ret+=geoWords.get(i)+ " ";
        		state = 1;
        	}
        	if(state==1 &&!(geoTags.get(i).equals("nnp")||geoTags.get(i).equals("nnps")))
        	{
        		state = 2;
        	}
        	if(geoTags.get(i).equals("in")){afterIn = true;};
        	
        }
        
        return ret.isEmpty()?null:ret;
    }

    /**
     * Given as arguments (1) a POS-tagged Wikipedia and (2) a target file,
     * writes "Title TAB location NEWLINE" to the target file
     */
    public static void main(String args[]) throws IOException {
        // Uncommment the following line for your convenience. Comment it out
        // again before submitting!
        //args = new String[] { "C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab5/wikipedia-first-pos.txt", "C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab5/resultslocation.txt" };
        try (Parser parser = new Parser(new File(args[0]))) {
            try (Writer result = new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8")) {
                while (parser.hasNext()) {
                    Page nextPage = parser.next();
                    String type = findLocation(nextPage);
                    if (type == null) continue;
                    result.write(nextPage.title + "\t" + type + "\n");
                }
            }
        }
    }

}