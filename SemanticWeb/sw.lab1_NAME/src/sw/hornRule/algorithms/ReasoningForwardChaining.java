/**
 * 
 */
package sw.hornRule.algorithms;

import java.util.ArrayList;
import java.util.HashSet;

import sw.hornRule.models.FactBase;
import sw.hornRule.models.Formalism;
import sw.hornRule.models.HornRule;
import sw.hornRule.models.HornRuleBase;
import sw.hornRule.models.Variable;

/**
 * @author  <Your name>
 *
 */
public class ReasoningForwardChaining extends AlogrithmChaining {
 
	private int countNbMatches=0;
	/**
	 * @param a knowledge base kb (in a given formalism)
	 * @param facts (in a given formalism)
	 * @return forwardChaining(ruleBase,factBase), also called the saturation of ruleBase w.r.t. factBase, 
	 * mathematically it computes the minimal fix point of KB from facts)
	 */
	//It's your turn to implement the algorithm, including the methods match() and eval()
	public FactBase forwardChaining(Formalism ruleBase, Formalism factBase){
		FactBase workFactBase =(FactBase) factBase;
	    FactBase  comparisonFactBase=(FactBase) workFactBase;
	    do{
	    	comparisonFactBase=workFactBase;
	    	ArrayList<HornRule>  relRules = new ArrayList<>();
		for(HornRule rule: ((HornRuleBase)ruleBase).getRules())
	    {
	    	if(eval(workFactBase,rule))
	    	{
	    		countNbMatches+= rule.getConditions().size();
	    		workFactBase = addConclusions(workFactBase,rule.getConclusions());
	    		relRules.add(rule);
	    	}
	    }
	    releaseRules((HornRuleBase)ruleBase,relRules);
	    }while(!match(comparisonFactBase,workFactBase));
		return workFactBase;
	};
	
	public boolean match(FactBase factBase1, FactBase factBase2)
	{
        return factBase1.getFact().containsAll(factBase2.getFact()) &&
        		factBase2.getFact().containsAll(factBase1.getFact());
	}

	public boolean eval(FactBase factBase1, HornRule rule1)
	{	
		return rule1.getConditions().stream().allMatch(variable -> isInFactBase(factBase1.getFact(),variable));
	}
	
	private boolean isInFactBase(HashSet<Variable> facts,Variable rule)
	{
		return facts.stream().anyMatch((fact-> fact.getNomVariable().equals(rule.getNomVariable())));
	}
	
	public  FactBase addConclusions(FactBase factBase1,HashSet<Variable> conclusions)
	{
		HashSet<Variable> workFactBase = factBase1.getFact();
		workFactBase.addAll(conclusions);
		factBase1.setFact(workFactBase);
		return factBase1;
	}
	
	public  HornRuleBase releaseRules(HornRuleBase hornRuleBase1, ArrayList<HornRule> relRules)
	{
		hornRuleBase1.getRules().removeAll(relRules);
		return hornRuleBase1;
	}
	
	/*
	 * Modify method to solve object comparison problem.
	 * @see sw.hornRule.algorithms.AlogrithmChaining#entailment(sw.hornRule.models.Formalism, sw.hornRule.models.Formalism, sw.hornRule.models.Formalism)
	 */
	public boolean entailment(Formalism ruleBase, Formalism factBase, Formalism query) {
		return forwardChaining(ruleBase, factBase).getFact().stream().anyMatch(fact->fact.getNomVariable().equals(((Variable)query).getNomVariable()));
	}

	
	@Override
	//It's your turn to implement this method
	/*The number of comparisons made
	 * 
	 */
	public int countNbMatches() {
		
		return countNbMatches;
	}

}
