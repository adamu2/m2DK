package lab1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Skeleton for a Trie data structure.
 * 
 * @author Fabian Suchanek and Luis Galarraga.
 *
 */
public class Trie {

	//boolean that have son.
  /**
   * Adds a string to the trie.
   */
	private int depth;
	private boolean value;
	private HashMap<Character,Trie> children= new HashMap<Character, Trie>();;
	/**
	 * TODO
	 * @param s
	 */
  	public Trie(boolean value, int depth)
  	{
  		this.value = value;
  		this.depth = depth;
  	}
  	
  public void add(String s) {
	  if(s.length()>1)
	  {
		  char c = s.charAt(0);
		  Trie t = children.containsKey(c)?children.get(c):new Trie(false,depth+1);
		  t.add(s.substring(1));
		  children.put(c, t);
	  }else
	  {
		  char c = s.charAt(0);
		  Trie t = children.containsKey(c)?children.get(c):new Trie(true,depth+1);
		  t.setValue(true);
		  children.put(c, t);
		  
	  }
  }

  public boolean getValue()
  {
	  return value;
  }
  public void setValue(boolean value)
  {
	  this.value = value;
  }
  private boolean inChildren(Character c)
  {
	  return children.get(c)!=null;
  }
  
  /**
   * Given a string and a starting position (<var>startPos</var>), it returns
   * the length of the longest word in the trie that starts in the string at
   * the given position, or else -1. For example, if the trie contains words
   * "New York", and "New York City", containedLength(
   * "I live in New York City center", 10) returns 13, that is the length of
   * the longest word ("New York City") registered in the trie that starts at
   * position 10 of the string.
   */
  public int containedLength(String s, int startPos) {
	  s=startPos>0?s.substring(startPos):s;
	  startPos = 0;
	  if(value)
	  {
		  return (s.length()>0&&inChildren(s.charAt(0)))?Math.max(depth,children.get(s.charAt(0)).containedLength(s.substring(1), startPos)):depth;
	  }
	  return s.length()>0?(inChildren(s.charAt(0))?children.get(s.charAt(0)).containedLength(s.substring(1), startPos):-1):-1;	  	 
  }

  /** Constructs a Trie from the lines of a file */
  public Trie(File file) throws IOException {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
      String line;
      while ((line = in.readLine()) != null) {
        add(line);
      }
    }
  }

  /** Constructs an empty Trie */
  public Trie() {
  }

  /** returns a list of all strings in the trie. Do not create a field of the class that contains all strings of the trie!*/
  public List<String> allStrings() {
    if(children.isEmpty())
    {
    	return new ArrayList<>();
    }else
    {
    	ArrayList<String> result = new ArrayList<>();
    	for(Character c:children.keySet())
    	{
    		List<String> words = children.get(c).allStrings();
    		for(String s:words)
    		{
    			result.add(Character.toString(c)+s);
    		}
    	}
    	return result;
    }
  }

  /** Use this to test your implementation. */
  public static void main(String[] args) throws IOException {
    // Hint: Remember that a Trie is a recursive data structure:
    // a trie has children that are again tries. You should
    // add the corresponding fields to the skeleton.
    // The methods add() and containedLength() are each no more than 15
    // lines of code!

    // Hint: You do not need to split the string into words.
    // Just proceed character by character, as in the lecture.

    Trie trie = new Trie();
    trie.add("New York City");
    trie.add("New York");
    trie.add("D");
    trie.allStrings().toString();
    System.out.println(trie.containedLength("I live in New York City center", 10) + " should be 13");
    System.out.println(trie.containedLength("I live in New York center", 10) + " should be 8");
    System.out.println(trie.containedLength("I live in Berlin center", 10) + " should be -1");
    System.out.println(trie.containedLength("I live in New Hampshire center", 10) + " should be -1");
    System.out.println(trie.containedLength("I live in New York center", 0) + " should be -1");
  }
  
 
}


