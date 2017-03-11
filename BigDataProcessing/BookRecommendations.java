import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class BookRecommendations {

  public static class TokenizerMapper
       extends Mapper<Object, Text,  Text,Text>{

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {    
    	String[] splitted = value.toString().split(" ");
    	for(int i=0;i<splitted.length;i++)
    	{
    		for(int j=0;j<splitted.length;j++)
    		{
    			context.write(new Text(splitted[i]),new Text(splitted[j]));
    		}
    	}
    }   
  }
  
  
  public static class BookListReducer
       extends Reducer<Text,Text,Text,Text> {

    public void reduce(Text key, Iterable<Text> recommendations,
                       Context context
                       ) throws IOException, InterruptedException {
      ArrayList<String> added =new ArrayList<>(); 
      for (Text book : recommendations) {
    	  if(!(key.toString().equals(book.toString()))&&!added.contains(book.toString()))
    	  {
    		  added.add(book.toString());
    	   }
      }
      context.write(key, new Text(added.toString()));
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "book recommender");
    job.setJarByClass(BookRecommendations.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(BookListReducer.class);
    job.setReducerClass(BookListReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
