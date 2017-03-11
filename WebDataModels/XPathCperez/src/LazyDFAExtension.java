

public class LazyDFAExtension {

	public static Automaton[] nFATables (String query)
	{
		String[] x = query.split("//");
		Automaton a[]= new Automaton[x.length-1];
		for(int i=0;i<a.length;i++)
		{
			Automaton b = new Automaton();
			b.initialize("//"+x[i+1]);
			a[i] = b;
		}
		return a;
	}
	
	
}
