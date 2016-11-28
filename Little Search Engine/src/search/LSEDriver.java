package search;
import java.io.*;
import java.util.ArrayList;


public class LSEDriver 
{
	public static void main(String[] args) throws IOException 
	{
		
		LittleSearchEngine test = new LittleSearchEngine();
		test.makeIndex("docs.txt", "noisewords.txt");
		for (String key : test.keywordsIndex.keySet())
		{
			System.out.println (key + " " + test.keywordsIndex.get(key).toString());
		}	
		System.out.println();
		System.out.println();
		ArrayList<String> results = test.top5search("simply", "finished");
		
		for (int i = 0; i < results.size(); i++)
		{
			System.out.println("RESULTS: " +results.get(i));
		}
		
		/*
		Scanner scan = new Scanner(System.in);
		System.out.println("Input dis shit doe: ");
		String word = scan.nextLine();
		System.out.println(keyWord(word));
		while (!word.equals("stop"))
		{
			System.out.println("Input more shit doe: ");
			word = scan.nextLine();
			System.out.println(keyWord(word));
		}
		*/
	}
}