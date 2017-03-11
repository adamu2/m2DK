/**
 * 
 */
package sw.hornRule.algorithms;

import java.util.Scanner;

import sw.hornRule.models.FactBase;
import sw.hornRule.models.Formalism;
import sw.hornRule.models.HornRule;
import sw.hornRule.models.HornRuleBase;
import sw.hornRule.models.Variable;

/**
 * @author  <Your name>
 *
 */
public class ReasoningBackwardChainingwithQuestions extends AlogrithmChaining {

	private int countNbMatches=0;
	private Scanner scanIn ;
	@Override
	public boolean entailment(Formalism ruleBase, Formalism factBase, Formalism query) {
		boolean b = backwardChainingWithQuestions(ruleBase,factBase,query);
		return b;
	}
 
	public void setScanner(Scanner scanIn2)
	{
		scanIn = scanIn2;
	}
	private boolean backwardChainingWithQuestions(Formalism ruleBase, Formalism factBase,
			Formalism query) {
		if (ReasoningBackwardChaining.matchQuery((FactBase)factBase,(Variable)query))
		{
			countNbMatches++;
			return true;
		}else{
			for(HornRule rule: ReasoningBackwardChaining.getRulesConcludingQuery((HornRuleBase)ruleBase,(Variable)query))
			{
				boolean b = true;
				countNbMatches+=rule.getConclusions().size()+rule.getConditions().size();
				for(Variable condition: rule.getConditions())
				{
					if(!b){break;}
					b= backwardChainingWithQuestions(ruleBase,factBase,condition);
				}
				if(b){return true;}
			}
			if(demandable(((HornRuleBase)ruleBase),((Variable)query)))
			{
				if(questionToUser((Variable)query))
				{
					countNbMatches++;
					return true;
				}else
				{
					return false;
				}
			}else
			{
				return false;
			}
		}				
	}
	
	
	private boolean demandable(HornRuleBase ruleBase,Variable query)
	{
		return ReasoningBackwardChaining.getRulesConcludingQuery(ruleBase, query).isEmpty();
	}
	
	private boolean questionToUser(Variable query)
	{
		System.out.println("Is "+query.getNomVariable()+" true?(y\\n) ");
		String answer=scanIn.nextLine();
		return (answer.equalsIgnoreCase("y")||answer.equalsIgnoreCase("yes"));
	}
	@Override
	public int countNbMatches() {
		return countNbMatches;
	}

}
