/**
 * 
 */
package sw.hornRule.algorithms;

import java.util.ArrayList;
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
public class ReasoningBackwardChaining extends AlogrithmChaining {
 
	private int countNbMatches=0;
	
	public boolean entailment(Formalism ruleBase, Formalism factBase, Formalism query) {
		return backwardChaining(ruleBase,factBase,query);
	}

	private boolean backwardChaining(Formalism ruleBase, Formalism factBase,
			Formalism query) {

		if (matchQuery((FactBase)factBase,(Variable)query))
		{
			countNbMatches++;
			return true;
		}else{
			for(HornRule rule: getRulesConcludingQuery((HornRuleBase)ruleBase,(Variable)query))
			{
				countNbMatches+=rule.getConclusions().size();
				boolean b = true;				
				for(Variable condition: rule.getConditions())
				{
					if(!b){break;}
					b= backwardChaining(ruleBase,factBase,condition);
				}
				if(b){return true;}
			}
			return false;
		}
	}
	
	
	public static boolean matchQuery(FactBase factBase, Variable q)
	{	
		return factBase.getFact().stream().anyMatch(fact-> fact.equals(q));
	}
	

	public static ArrayList<HornRule> getRulesConcludingQuery(HornRuleBase ruleBase,Variable var)
	{
		return ruleBase.getRules().stream().filter(rule -> rule.getConclusions().stream().anyMatch(conclusion-> conclusion.equals(var))).collect(Collectors.toCollection(ArrayList::new));
	}
	

	@Override
	public int countNbMatches() {
		
		return countNbMatches;
	}

}
