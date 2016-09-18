package SemanticWeb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ForwardChaining {

	public static ArrayList<String> FB;
	public static Map <String[],String[]> RB;
	
	public static final String[] RULE_1 = new String[]{"boat","sport","sail"};
	public static final String[] C_RULE_1 = new String[]{"sailboat"};
	public static final String[] RULE_5 = new String[]{"no_keel","sailboat"};
	public static final String[] C_RULE_5 = new String[]{"sailingDinghy","portable"};
	public static final String[] RULE_6 = new String[]{"habitable","sailboat"};
	public static final String[] C_RULE_6 = new String[]{"sailboat_cruise","no_portable"};
	public static final String[] RULE_9 = new String[]{"longer_than_13"};
	public static final String[] C_RULE_9 = new String[]{"longer_than_10"};
	
	
	public static void main(String[] args) {
		init();
		System.out.println("result fact base: "+forwardChaining(FB,RB).toString());
	}
	
	public static void init()
	{
		FB = new ArrayList<String>();
		RB = new HashMap<String[], String[]>();
		FB.add("longer_than_13");
		FB.add("habitable");
		FB.add("no_keel");
		FB.add("boat");
		FB.add("sport");
		FB.add("sail");
		RB.put(RULE_1,C_RULE_1);
		RB.put(RULE_5,C_RULE_5);
		RB.put(RULE_6,C_RULE_6);
		RB.put(RULE_9,C_RULE_9);
	}
	
	public static ArrayList<String> forwardChaining(ArrayList<String> factBase,Map <String[],String[]> ruleBase)
	{
		ArrayList<String> workFactBase = new ArrayList<String>(factBase);
	    ArrayList<String> comparisonFactBase;
		int iteration = 0;
		System.out.println("iteration "+iteration+" factbase:"+workFactBase.toString());
	    do{
	    	comparisonFactBase = new ArrayList<String>(workFactBase);
	        iteration++;
		for(String[] key: RB.keySet())
	    {
	    	if(isRule(workFactBase,key))
	    	{
	    		workFactBase.addAll(Arrays.asList(RB.get(key)));
	    	}
	    }
		System.out.println("iteration "+iteration+" factbase:"+workFactBase.toString());
	    }while(!listsAreTheSame(comparisonFactBase,workFactBase));
		return workFactBase;
	}
	
	private static boolean listsAreTheSame(ArrayList<String>list1,ArrayList<String>list2)
	{
		boolean ret = true;
		
		for(String element:list2)
		{
			ret= ret & list1.contains(element);
		}
		for(String element:list1)
		{
			ret= ret & list2.contains(element);
		}
		return ret;
	}
	
	public static boolean isRule(ArrayList<String> FB, String[] rule)
	{
		boolean ret = true;
		for(int i=0; i < rule.length;i++)
		{
			ret = ret && FB.contains(rule[i]);
		}
		return ret;
	}
}
