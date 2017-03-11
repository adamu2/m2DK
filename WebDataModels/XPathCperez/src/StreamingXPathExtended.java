

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

public class StreamingXPathExtended {

	private String filePath;
	private Stack<Integer> stack1;
	private Stack<Integer> stack3;
	private Automaton nfaTables[];
	private int iKMP;
	private int nodeCounter;
	private int ct;
	private PrintWriter printWriter;
	
	public StreamingXPathExtended(String path, String query, String printPath) throws FileNotFoundException
	{
		filePath = path;
		nfaTables = LazyDFAExtension.nFATables(query);
		iKMP = 0;
		nodeCounter = -1;
		stack1 = new Stack<>();
		stack3 = new Stack<>();
		ct = 0;
		printWriter = new PrintWriter(new File(printPath));
	}
	
	
	public void streaming() throws IOException
	{
		BufferedReader in = new BufferedReader(new FileReader(filePath));
		String line;
		while((line = in.readLine())!=null)
		{
			preprocessLine(line);
		}
		in.close();
		printWriter.close();
	}
	
	private void preprocessLine(String l)
	{
		String[] x = l.split(" ");
		if(x[0].equals("0"))
		{
			processNode(x[1]);
			stack1.push(iKMP);
			stack3.push(ct);
		}else if(x[0].equals("1"))
		{
			stack1.pop();
			stack3.pop();
		}
	}
	
	public void processNode(String n)
	{
		nodeCounter++;
		iKMP=(!stack1.isEmpty()?(int) stack1.peek():iKMP);
		ct = (!stack3.isEmpty()?(int) stack3.peek():ct);
		Automaton a = nfaTables[ct];
		if(n.equals(a.getNodes()[iKMP]))
		{
			nextStep();
		}else
		{
			if(failureTransition(a.getNodes(),a.getKnuthMorrisArray(),n,iKMP))
			{
				iKMP = failureTransitionInt(a.getNodes(),a.getKnuthMorrisArray(),n,iKMP);
				nextStep();
			}else{
				iKMP = failureTransitionInt(a.getNodes(),a.getKnuthMorrisArray(),n,iKMP);
			}
		}
	}
	
	private void nextStep()
	{
		Automaton a = nfaTables[ct];
		if(iKMP == a.getNodes().length-1)
		{
			if(ct == nfaTables.length-1)
			{
				printNode(nodeCounter);
				iKMP = a.getKnuthMorrisArray()[iKMP];
			}else
			{
				ct++;
				iKMP = 0;
			}
		}else
		{
			iKMP ++;
		}
	}
	
	private boolean failureTransition(String[] a,Integer[] b,String p,int i)
	{
		if(i==0)
		{
			return (p.equals(a[0])?true:false);
		}else		{
		    return (p.equals(a[b[i-1]])?true:failureTransition(a,b,p,b[i-1]));
		}
	}
	
	private int failureTransitionInt(String[] a,Integer[] b,String p,int i)
	{
		if(i==0)
		{
			return 0;
		}else		{
		    return (p.equals(a[b[i-1]])?b[i-1]:failureTransitionInt(a,b,p,b[i-1]));
		}
	}
	
	private void printNode(int node)
	{
		printWriter.println(node);
	}
	
	public static void main(String[] args) {
		try{
		File f = new File(args[1]);
		SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss") ;
		BufferedReader in = new BufferedReader(new FileReader(f));
		String line;
		int i = 1;
		while((line = in.readLine())!=null)
		{
			StreamingXPathExtended sXP = new StreamingXPathExtended(args[0],line,f.getParent()+"/"+dateFormat.format(new Date())+"query_"+i+".out");
			sXP.streaming();
			i++;
		}
		in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
