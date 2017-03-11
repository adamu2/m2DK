


public class Automaton {

	String[] states;
		
	Integer[] failureFunction;
	
	public void initialize(String path)
	{ 
		states = path.substring(2).split("/");
		failureFunction = knuthMorrisPratt();
	}

	
	private Integer[] knuthMorrisPratt()
	{
		String[] p = states;
		int m = p.length;
		Integer[] a = new Integer[p.length];
		a[0] = 0;
		int i = 0;
		int j = i+1;
		for(;j<m;j++)
		{
			if(p[i].equals(p[j]))
			{
				a[j] = i +1;
				i=i+1;
			}else
			{   
				a[j] = compareWithA(a,p,i,j);
				i = a[j];
			}
		}
		return a;
	}

	private Integer compareWithA(Integer[] a,String[] p,int i,int j)
	{
		if(i==0)
		{
			return 0;
		}else if(i!=0&&p[i].equals(p[j]))		{
		     return i + 1;	
		}else
		{
			return compareWithA(a,p,a[i],j);
		}
	}

	public String[] getNodes() {
		return states;
	}


	public Integer[] getKnuthMorrisArray() {
		return failureFunction;
	}
	
	
}
