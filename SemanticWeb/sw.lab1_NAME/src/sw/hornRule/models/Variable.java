package sw.hornRule.models;


/**
 * @author 
 *
 */
public class Variable extends Formalism{
	protected String variableName; 
	
	// Deux variables specifiques, Vrai et False
	public final static Variable False = new Variable("False");
	public final static Variable True = new Variable("True");
	
	public Variable() {
		this.variableName = "UnknownVariable";
	}
	
	public Variable(String variableName) {
		super();
		this.variableName = variableName;
	}

	public String getNomVariable() {
		return variableName;
	}

	public void setNomVariable(String variableName) {
		this.variableName = variableName;
	}

	@Override
	public String toString() {
		return  variableName;
	}
	/**
	 * Fix for hash comparing methods
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
	    if (obj == this) return true;
	    if (!(obj instanceof Variable))return false;
		return this.getNomVariable().equals(((Variable) obj).getNomVariable());
	}
	
	/**
	 * Fix for hash comparing methods
	 */
	@Override
	public int hashCode() {
		  return getNomVariable().length();
	}
}
