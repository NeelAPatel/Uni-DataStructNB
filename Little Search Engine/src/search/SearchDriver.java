package search;

import java.io.IOException;

public class SearchDriver {
	
	public static void main(String[] args) throws IOException
	{
		LittleSearchEngine LSE = new LittleSearchEngine();
		
		
		String docsFile = "docs.txt";
		String noiseFile = "noisewords.txt";
		LSE.makeIndex(docsFile,noiseFile); // already coded.
		//LSE.makeIndex("docs.txt", "noisewords.txt");
		
		
		
	}

}
