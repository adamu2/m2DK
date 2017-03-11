package problem.hornRules;
 
import java.util.HashSet;
import java.util.Scanner;

import sw.hornRule.algorithms.*;
import sw.hornRule.models.*;

public class ReasoningHorn {

	public static void main(String[] args) {
		Scanner scanIn = new Scanner(System.in);
		String userInput="";
		System.out.println("Please select the algorithm");
		System.out.println("1 - forward chaining");
		System.out.println("2 - forward chaining with propagation");
		System.out.println("3 - backward chaining");
		System.out.println("4 - backward chaining with questions");
		if(scanIn.hasNext())
	    {
	    	userInput=scanIn.nextLine();
	    }
		switch (userInput) {
		case "1":
			forwardChaining();
			break;
		case "2":
			forwardChainingWithPropagation();
			break;
		case "3":
			backwardChaining();
			break;
		case "4":
			backwardChainingWithQuestions(scanIn);
			break;
		default:
			System.out.println("Invalid input values");
			System.out.println("Please execute again with valid values 1 2 3 or 4");
			break;
		}		
		
		scanIn.close();
	}
	
	public static void forwardChaining()
	{

		System.out.println("------------------------------------");
		System.out.println("Forward chaining");
		System.out.println("------------------------------------");
		
		ReasoningForwardChaining reasoner = new ReasoningForwardChaining();
		Tutorial1 pb = new Tutorial1();
		HornRuleBase kb = pb.getRuleBase();
		FactBase fb = pb.getFactBase();
		
		for(HornRule r: kb.getRules()){
			System.out.println(r);
		}
		System.out.print("\nThe fact base is: \n");
		System.out.print(fb);
		
		//Display all facts inferred by the given knowledge base kb and fact base fb
		HashSet<Variable> inferredAllFacts = reasoner.forwardChaining(kb,fb).getFact();
		System.out.println("All the inferred facts are:");
		int i=0;
		for(Variable s: inferredAllFacts){
			i++;
			System.out.println("fact_"+i+" - "+s);
		}
		
		Variable q = new Variable("transoceanic_race");
		System.out.println("\nquery = "+q.getNomVariable());
		if(reasoner.entailment(kb, fb, q))
			System.out.println("\nYes, the query is entailed by the given rules and facts");
		else
			System.out.println("\nNo, the query is not entailed based on the given rules and facts");
		System.out.println("\nNumber of matches :"+reasoner.countNbMatches());
	}
	
	public static void forwardChainingWithPropagation()
	{
		//Forward chaining with propagation
				System.out.println("------------------------------------");
				System.out.println("Forward chaining with propagation");
				System.out.println("------------------------------------");
				
				ReasoningForwardChainingOptimised reasoner = new ReasoningForwardChainingOptimised();
				Tutorial1 pb = new Tutorial1();
				HornRuleBase kb = pb.getRuleBase();
				FactBase fb = pb.getFactBase();
				for(HornRule r: kb.getRules()){
					System.out.println(r);
				}
				
				System.out.print("\nThe fact base is: \n");
				System.out.print(fb);
				
				//Display all facts inferred by the given knowledge base kb and fact base fb
				HashSet<Variable>inferredAllFacts = reasoner.forwardChainingOptimise(kb,fb).getFact();
				System.out.println("All the inferred facts are:");
				int i=0;
				for(Variable s: inferredAllFacts){
					i++;
					System.out.println("fact_"+i+" - "+s);
				}
				
				 Variable q = new Variable("transoceanic_race");
				 System.out.println("\nquery = "+q.getNomVariable());
				 System.out.println("\nNumber of matches :"+reasoner.countNbMatches());
				if(reasoner.entailment(kb, fb, q))
					System.out.println("\nYes, the query is entailed by the given rules and facts");
				else
					System.out.println("\nNo, the query is not entailed based on the given rules and facts");
				
	}
	
	public static void backwardChaining()
	{
		//Backward chaining without questions
				System.out.println("------------------------------------");
				System.out.println("Backward chaining without questions");
				System.out.println("------------------------------------");
				ReasoningBackwardChaining reasoner = new ReasoningBackwardChaining();
				Tutorial1 pb = new Tutorial1();
				HornRuleBase kb = pb.getRuleBase();
				FactBase fb = pb.getFactBase();
				for(HornRule r: kb.getRules()){
					System.out.println(r);
				}
				System.out.print("\nThe fact base is: ");
				System.out.print(fb);
				Variable q = new Variable("transoceanic_race");
				System.out.println("\nquery = "+q.getNomVariable());
				if(reasoner.entailment(kb, fb, q))
					System.out.println("\nYes, the query is entailed by the given rules and facts");
				else
					System.out.println("\nNo, the query is not entailed based on the given rules and facts");
				System.out.println("\nNumber of matches :"+reasoner.countNbMatches());
				
	}
	
	public static void backwardChainingWithQuestions(Scanner scanIn)
	{
		System.out.println("------------------------------------");
		System.out.println("\nBackward chaining with questions\n");
		System.out.println("------------------------------------");
		//Backward chaining with questions
		ReasoningBackwardChainingwithQuestions reasoner = new ReasoningBackwardChainingwithQuestions();
		reasoner.setScanner(scanIn);
		Tutorial1 pb = new Tutorial1();
		HornRuleBase kb = pb.getRuleBase();
		FactBase fb = pb.getFactBase();
		for(HornRule r: kb.getRules()){
			System.out.println(r);
		}
		System.out.print("\nThe fact base is: \n");
		System.out.print(fb);
		Variable q = new Variable("gaff_rig");
		System.out.println("\nQuery:"+q.getNomVariable()+"\n");
		if(reasoner.entailment(kb, fb, q))
			System.out.println("\nYes, the query is entailed by the given rules and facts");
		else
			System.out.println("\nNo, the query is not entailed based on the given rules and facts");
		System.out.println("\nNumber of matches :"+reasoner.countNbMatches());
	}
}
