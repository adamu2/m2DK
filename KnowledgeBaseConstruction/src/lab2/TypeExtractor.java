package lab2;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Skeleton code for a type extractor.
 */
public class TypeExtractor {

	String s1 = "(?:was|is|are)\\s*";
	String s2 = "(?:the\\s*|a(?:n)?\\s*)";
	String s3 = "(?:like\\s*(?:a(?:n)?\\s))";
	String s4 = "([^(?:\\s]*(?:\\s*|\\p{Punct})?)";
		Pattern p = Pattern.compile(s1+s2+"?"+s3+"?(?:"+s4+")");
	
	public String[] extractType(String content)
{
	List<Matcher> ml = new ArrayList<>();
	ml.add(p.matcher(content));
	for(Matcher m:ml)
			    if(m.find()) return new String[]{m.group(0),m.group(1)};
	return null;
}
  /**
Given as argument a Wikipedia file, the task is to run through all Wikipedia articles,
and to extract for each article the type (=class) of which the article
entity is an instance. For example, from a page starting with "Leicester is a city",
you should extract "city". 

* extract the longest possible type ("American rock-and roll star") consisting of adjectives,
  nationalities, and nouns
* if the type cannot reasonably be extracted ("Mathematics was invented in the 19th century"),
  skip the article (do not output anything)
* take only the first item of a conjunction ("and")
* do not extract provenance ("from..", "in..", "by.."), but do extract complements
  ("body of water")
* do not extract too general words ("type of", "way", "form of"), but resolve like a
  human ("A Medusa  is one of two forms of certain animals" -> "animals")
* keep the plural

The output shall be printed to the screen in the form
    entity TAB type NEWLINE
with one or zero lines per entity.
   */
  public static void main(String args[]) throws IOException {
    //args = new String[] { "C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab2/wikipedia-first.txt" };
    File f = new File(args[0]);
    File f2 = new File(f.getParent()+"/result.tsv");
    if(!f2.exists())f2.createNewFile();
    PrintWriter printWriter = new PrintWriter(f2);
    try (Parser parser = new Parser(new File(args[0]))) {
      while (parser.hasNext()) {
        Page nextPage = parser.next();
        TypeExtractor te = new TypeExtractor();
        String[] type= te.extractType(nextPage.content);
        // Magic happens here
        if(type!=null) printWriter.println(nextPage.title+"\t"+type[1]);;
      }
    }
    printWriter.close();
  }

}