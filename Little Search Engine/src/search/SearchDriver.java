package search;

import java.io.IOException;

public class SearchDriver {
	
	public static void main(String[] args) throws IOException
	{
		LittleSearchEngine LSE = new LittleSearchEngine();
		String docsFile = "";
		String noiseFile = "";
		//LSE.makeIndex(docsFile,noiseFile); // already coded.
		
		LSE.getKeyWord("|.@#$%^&... the  ;*135136");
		
		LSE.makeIndex("docs.txt", "noisewords.txt");
		
		
		
	}

}
