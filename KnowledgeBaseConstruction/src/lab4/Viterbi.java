package lab4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Skeleton for a Viterbi POS tagger.
 * 
 * @author Fabian M. Suchanek
 *
 */
public class Viterbi {

    /** HMM we'll use */
    protected HiddenMarkovModel model;

    /** Constructs the parser from a model file */
    public Viterbi(File modelFile) throws FileNotFoundException, IOException {
        model = new HiddenMarkovModel(modelFile);
    }

    /** Parses a sentence and returns the list of POS tags */
    public List<String> parse(String sentence) {
        List<String> words = Arrays.asList((". " + sentence.toLowerCase() + " .").split(" "));
        int numWords = words.size();
        List<String> tags = new ArrayList<String>(model.emissionProb.keySet());
        int numTags = tags.size();
        // Smart things happen here!
        List<String> result = new ArrayList<>();
        int maxem = 0;
        String trprev = "/.";
        String r = "/."; 
        boolean first = true;
        for(String word:words )
        {
        	boolean found = false;
        	for(String tag:tags)
        	{
        		if(first)
        		{
        			if((model.getEmissionProbability(tag, word))>maxem)
            		{
            			found = true;
            			r = tag;
            		}
        		}else
        		{
        			if((model.getEmissionProbability(tag, word)*model.getTransitionProbability(trprev, tag))>maxem)
            		{
            			found = true;
            			r = tag;
            		}
        		}
        		
        	}
    		
        	if (found){result.add(r);
        		first=false;
        		trprev=r;
        	}
        	
        }
        return result;
    }


    /**
     * Given (1) a Hidden Markov Model file and (2) a sentence (in quotes),
     * prints the sequence of POS tags
     */
    /**
     *RESULTS FOR "Elvis is the best" [., NNP, VBZ, NNP, RB, .]
     *RESULTS FOR "Elvis signs best"  [., NNP, NNS, RBS, .]
     *RESULTS FOR ""Elvis is in Krzdgwzy" [., NNP, VBZ, RP, .]
     *The results differ because the tags associated with the words
     * have different emission probabilities.
     *They also differ because the transition probabilites between the tags might make more likely one result . 
     */
    public static void main(String[] args) throws Exception {
    	//args=new String[]{"C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab4/results.txt","Elvis is the best"};
    	//args=new String[]{"C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab4/results.txt","Elvis signs best"};
    	//args=new String[]{"C:/ACrashPlan/Paris/KnowledgeBaseConstruction/Labs/resources/lab4/results.txt","Elvis is in Krzdgwzy"};
        System.out.println(new Viterbi(new File(args[0])).parse(args[1]));
    }
}
