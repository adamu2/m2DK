package lab1;

import java.io.File;
import java.io.IOException;
import lab1.Trie;

/**
 * Skeleton for an entity recognizer based on a trie.
 * 
 * @author Fabian M. Suchanek
 */
public class EntityRecognizer {

    /**
     * The task is to modify the class so that it takes as arguments (1) the
     * Wikipedia corpus and (2) a file with a list of entities, and so that it
     * outputs appearances of entities in the content of articles. Each
     * appearance should be printed to the standard output as:
     * <ul>
     * <li>The title of the article where the mention occurs</li>
     * <li>TAB (\t)
     * <li>The entity mentioned</li>
     * <li>NEWLINE (\n)
     * </ul>
     * 
     * Hint: Go character by character, as in the lecture. It is not necessary
     * to go by word boundaries!
     */

    public static void main(String args[]) throws IOException {
        // Uncomment the following lines for your convenience.
        // Comment them out again before submission!
        //args = new String[] { "C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Lab1/wikipedia-first.txt",
        //"C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Lab1/entities.txt" };
        Trie trie = new Trie(new File(args[1]));
        try (Parser parser = new Parser(new File(args[0]))) {
            while (parser.hasNext()) {
                Page nextPage = parser.next();
                for(int i=0;i<nextPage.content.length();i++)
                {
                	int contained = trie.containedLength(nextPage.content, i);
                	if(contained!=-1)
                	{
                		 String entity = nextPage.content.substring(i,i+contained);
                		 System.out.print("<ul>");
                         System.out.print("<li>"+nextPage.title+"</li>");
                         System.out.print("\t");
                         System.out.print("<li>"+entity+"</li>");
                         System.out.print("\n");
                         System.out.println("</ul>");
                	}
                }
                 // TODO: do something smarter here
                
               
            }
        }
    }

}