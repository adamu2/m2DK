package sw.hornRule.models;

import java.util.HashSet; 

public class Query extends Formalism{
	HashSet<Variable> goals;

	public Query(HashSet<Variable> goals) {
		super();
		this.goals = goals;
	}

	public Query() {
		this.goals = new HashSet<Variable>(); 
	}

	public HashSet<Variable> getGoals() {
		return goals;
	}

	public void setFact(HashSet<Variable> goals) {
		this.goals = goals;
	}

	@Override
	public String toString() {
		return "query goals=" + goals +"\n";
	}
	
}
