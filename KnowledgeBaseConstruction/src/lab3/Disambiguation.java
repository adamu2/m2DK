package lab3;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Skeleton class to perform disambiguation
 * 
 * @author Jonathan Lajus
 *
 */
public class Disambiguation {

  /** This program takes 3 command line arguments, namely the paths to:
      - yagoLinks.tsv
      - yagoLabels.tsv
      - wikipedia-ambiguous.txt      
   in this order. You may also ignore the last argument at your will.
   The program prints statements of the form:
      <pageTitle>  TAB  <yagoEntity> NEWLINE   
   It is OK to skip articles.      
  */
   public static final String[] stopwords = {"a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing", "don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours", "",	"ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves"};
   public static final String[] punctuationSigns = {".","!","(",")","[","]","\"",";","?",",",":"};
   
   private SimpleDatabase simpleDatabase;
   
   public Disambiguation(SimpleDatabase simpleDatabase)
   {
	   this.simpleDatabase = simpleDatabase;
   }
   
   public String getDisambiguatedEntity(String title,String content, String label)
   {
	   ArrayList<String> words =  (ArrayList<String>) Arrays.stream(content.split(" ")).filter(word->!Arrays.asList(stopwords).contains(word)).collect(Collectors.toList());
	   Set<String> entities =simpleDatabase.reverseLabels.get(label);
	   long max = 0;
	   String ent="";
	   for(String entity:entities)
	   {
		   Set<String> labels = simpleDatabase.labels.get(entity);
		   Set<String> entitiesR = simpleDatabase.links.get(entity);
		   long c1 = words.stream().filter(w->containsWord(w,labels)).count();
		   long c2 = words.stream().filter(w->containsWord(w,entitiesR)).count();
		   if(c1+c2>max) {ent=entity;max=c1+c2;}
	   }
	   return ent;
   }
   
   public boolean containsWord(String word,Set<String> context )
   {
	   for(String s:context)
	   {
		   if (s.contains(word)) return true;
	   }
	   return false;
   }
	public String removePuntuationSigns(String word)
	{
		for(String p: punctuationSigns)
		{
			word=word.replace(p, "");
		}
		return word;
	}
   
  public static void main(String[] args) throws IOException {
    //args = new String[] { "C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab3/yagoLinks.tsv",
    //		"C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab3/yagoLabels.tsv",
    //		"C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab3/wikipedia-ambiguous.txt"};
    if (args.length < 3) {
        System.err.println("usage: Disambiguation <yagoLinks> <yagoLabels> <wikiText>");
        return;
      }
    File dblinks = new File(args[0]);
    File dblabels = new File(args[1]);
    File wiki = new File(args[2]);

    SimpleDatabase db = new SimpleDatabase(dblinks, dblabels);
    Disambiguation d = new Disambiguation(db);
    PrintWriter printWriter;
    try (Parser parser = new Parser(wiki)) {
    	File f2 = new File(wiki.getParent()+"/result.tsv");
        if(!f2.exists())f2.createNewFile();
        printWriter = new PrintWriter(f2);
    	while (parser.hasNext()) {
        Page nextPage = parser.next();
        String pageTitle = nextPage.title; 
        String pageContent = nextPage.content; 
        String pageLabel = nextPage.label(); 
        String correspondingYagoEntity = d.getDisambiguatedEntity(pageTitle, pageContent, pageLabel);
        System.out.println(pageTitle + "\t" + correspondingYagoEntity);
        printWriter.println(pageTitle + "\t" + correspondingYagoEntity);
        
      }
    }
    printWriter.close();
  }
}
