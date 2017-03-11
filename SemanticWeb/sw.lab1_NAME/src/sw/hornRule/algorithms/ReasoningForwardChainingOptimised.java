/**
 * 
 */
package sw.hornRule.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import sw.hornRule.models.FactBase;
import sw.hornRule.models.Formalism;
import sw.hornRule.models.HornRule;
import sw.hornRule.models.HornRuleBase;
import sw.hornRule.models.Variable;

/**
 * @author  <Your name>
 *
 */
public class ReasoningForwardChainingOptimised extends AlogrithmChaining {
	
	
	private int countNbMatches=0;
	
	/**
	 * @param a knowledge base ruleBase (in a given formalism)
	 * @param a base of facts : factBase (in a given formalism)
	 * @return the saturation of KB w.r.t. facts (the minimal fix point of KB from facts)
	 */
	public FactBase forwardChainingOptimise(Formalism ruleBase, Formalism factBase){
		FactBase workFactBase = new FactBase(((FactBase)factBase).getFact());
		RulesAndFacts propagate = new RulesAndFacts();
		propagate.workRuleBase = (HornRuleBase)ruleBase; 
		HashSet<Variable> toAdd2 = new HashSet<>();
		for(Variable fact:((FactBase)factBase).getFact())
		{
			propagate = propagate(fact,propagate,0,fact.getNomVariable());
			toAdd2.addAll(propagate.factSet);			
		}
		workFactBase.getFact().addAll(toAdd2);
		return workFactBase;
	};
	

	public RulesAndFacts propagate(Variable var, RulesAndFacts  rf, int iteration,String previous)
	{
		HornRuleBase workRuleBase = new HornRuleBase(rf.workRuleBase.getConditions(),rf.workRuleBase.getConclusions(),rf.workRuleBase.getRules());
		HashSet<Variable> facts = new HashSet<>();
		
		for(HornRule rule: inRuleConditions(var, rf.workRuleBase))
		{
			countNbMatches++;
			HornRule delCon = new HornRule(rule.getConditions(),rule.getConclusions());
			workRuleBase = deleteConditionsFromRule(delCon, var,workRuleBase);
			
			if(delCon.getConditions().isEmpty())
			{
				workRuleBase = deleteRule(workRuleBase, delCon);
				facts.addAll(delCon.getConclusions());
			}
		}
		
		HashSet<Variable> toAdd1 = new HashSet<>();
		RulesAndFacts raf = new RulesAndFacts();
		raf.workRuleBase = workRuleBase;
		toAdd1.addAll(facts);
		for(Variable fact : facts)
		{	
			raf = propagate(fact,raf,iteration++,var.getNomVariable());
			toAdd1.addAll(raf.factSet);
		}
		raf.factSet=toAdd1;
		return raf;
	}
	
	public static HornRuleBase deleteRule(HornRuleBase ruleBase, HornRule del)
	{
		ArrayList<HornRule> rules =ruleBase.getRules().stream().filter(rule -> !rule.equals(del)).collect(Collectors.toCollection(ArrayList::new));
		ruleBase.setRules(rules);
		return ruleBase;
	}
	
	
	public HornRuleBase deleteConditionsFromRule(HornRule rule, Variable var, HornRuleBase ruleBase)
	{	
		ArrayList<HornRule> rules =ruleBase.getRules().stream().filter(rl -> !rl.equals(rule)).collect(Collectors.toCollection(ArrayList::new));
		rule.setConditions(rule.getConditions().stream().filter(condition -> !condition.getNomVariable().equals(var.getNomVariable())).collect(Collectors.toCollection(HashSet::new)));
		rules.add(rule);
		ruleBase.setRules(rules);
		return ruleBase;
	}
	
	public ArrayList<HornRule> inRuleConditions(Variable var, HornRuleBase ruleBase)
	{
		return ruleBase.getRules().stream().filter(rule->rule.getConditions().contains(var)).collect(Collectors.toCollection(ArrayList::new));
	}
	
 
	public boolean entailment(Formalism ruleBase, Formalism factBase, Formalism query) {
		return forwardChainingOptimise(ruleBase, factBase).getFact().contains(query);
		
	}

	@Override
	public int countNbMatches() {
		return countNbMatches;
	}

	private class RulesAndFacts
	{
		HornRuleBase workRuleBase;
		HashSet<Variable> factSet;
	}
	
}
