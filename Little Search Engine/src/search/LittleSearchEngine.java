package search;

import java.io.*;
import java.util.*;

/**
 * This class encapsulates an occurrence of a keyword in a document. It stores the
 * document name, and the frequency of occurrence in that document. Occurrences are
 * associated with keywords in an index hash table.
 * 
 * @author Sesh Venugopal
 * 
 */
class Occurrence {
	/**
	 * Document in which a keyword occurs.
	 */
	String document;
	
	/**
	 * The frequency (number of times) the keyword occurs in the above document.
	 */
	int frequency;
	
	/**
	 * Initializes this occurrence with the given document,frequency pair.
	 * 
	 * @param doc Document name
	 * @param freq Frequency
	 */
	public Occurrence(String doc, int freq) {
		document = doc;
		frequency = freq;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + document + "," + frequency + ")";
	}
}

/**
 * This class builds an index of keywords. Each keyword maps to a set of documents in
 * which it occurs, with frequency of occurrence in each document. Once the index is built,
 * the documents can searched on for keywords.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in descending
	 * order of occurrence frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash table of all noise words - mapping is from word to itself.
	 */
	HashMap<String,String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashMap<String,String>(100,2.0f);
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.put(word,word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeyWords(docFile);
			mergeKeyWords(kws);
		}
		
	}

	/**
	 * DONE - Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeyWords(String docFile) 
	throws FileNotFoundException {
		// COMPLETE THIS METHOD
		if (docFile == null)
		{
			throw new FileNotFoundException();
		}
		

		HashMap<String,Occurrence> keywordHash = new HashMap <String, Occurrence>();
		Scanner sc = new Scanner(new File(docFile));
		
		
		while (sc.hasNext())
		{	
			//Import word from file
			String importedWord = getKeyWord(sc.next());
			
			if(importedWord != null) // if imported word is null, then no point on importing
			{
				//Check to see if word exists in Hash or not
				/**
				 * If (key exists in hash)
				 * 		raise frequency
				 * or else
				 * 		add it in 
				 */
				if (keywordHash.get(importedWord) != null)
				{
					keywordHash.get(importedWord).frequency ++;
				}
				else
				{
					Occurrence o = new Occurrence(docFile,1); //docFile = name of the file to mark, 1 = first occurence
					keywordHash.put(importedWord, o);				
				}
				
			}	
		}
		
		
			
		
		
		
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		return keywordHash;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeyWords(HashMap<String,Occurrence> kws) {
		// COMPLETE THIS METHOD
		/**
		 * TLDR; Take every string/occurrence pair in kws and transfer to keywordsIndex
		 */
		
		
		
		
		
	}
	
	/**
	 * DONE - Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * TRAILING punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyWord(String word) {
		// COMPLETE THIS METHOD
		word = word.toLowerCase();
		System.out.println("\n Word in LowerCase: <" + word + ">");
		
		
		
		// Kill off .,?!:; at the end of the list
		while(!Character.isLetter(word.charAt(word.length()-1)))
		{
			char c = word.charAt(word.length()-1); // character at the very end of the word
			switch (c)
			{
				case '.': 
					word = word.substring(0, word.length()-1);
					break;
				case ',':
					word = word.substring(0, word.length()-1);
					break;
				case '?': 
					word = word.substring(0, word.length()-1);
					break;
				case ':': 
					word = word.substring(0, word.length()-1);
					break;
				case ';': 
					word = word.substring(0, word.length()-1);
					break;
				case '!': 
					word = word.substring(0, word.length()-1);
					break;
				default:
					return null; 
				
			}
		}
		
		
		// If at any point in the word minus trailing list is a non-letter, kill the process and return null
		for (int i = 0; i < word.length(); i++)
		{
			if (!Character.isLetter(word.charAt(i)))
				return null;
		}
		
		//checks if the word is one of the noise words
		if (noiseWords.containsKey(word))
			return null;
		
		
		//pass all the tests = return word.
		System.out.println(word + " is allowed");
		return word;
		
//		
//		if (word.length() == 1)
//		{
//			return null;
//		}
//		
//		String[] wordArr = new String[word.length()];
//		
//		
//		//Adds each letter individually to the list
//		for (int i = 0; i < word.length(); i++)
//		{
//			wordArr[i] = word.charAt(i)+"";
//		}
//		
//		
//
//		//findStartIndex of when letters start
//		
//		int wordArrIndex = 0;
//		while(wordArrIndex <= wordArr.length-1)
//		{
//			if (!Character.isLetter(wordArr[wordArrIndex].charAt(0)))
//				wordArr[wordArrIndex] = null;
//			else
//				break;
//			wordArrIndex++;
//		}
//		
//		System.out.println(word.substring(wordArrIndex));
//		word = word.substring(wordArrIndex);
//		
//		String[] newWordArr = new String[word.length()];
//		System.arraycopy(wordArr, wordArrIndex, newWordArr, 0, newWordArr.length);
//		
//		wordArr = newWordArr;
//		
//		System.out.println(word);
//		//At this point word is stripped off any nonletters at the front
//		
//		
//		//Remove all symbols from end
//		for (int i = 0; i < wordArr.length; i++)
//		{
////			System.out.println(wordArr[i]);
//		}
//		
//		wordArrIndex = wordArr.length-1;
//	//	System.out.println(wordArr[wordArrIndex].charAt(0));
//		
//		while(!Character.isLetter(wordArr[wordArrIndex].charAt(0)))
//		{
//			wordArrIndex--;
//		}
//		
//		//System.out.println(word.substring(0,wordArrIndex+1));
//		word = (word.substring(0,wordArrIndex+1));
//		
//		
//		//TODO: CHECK IF word IS INCLUDED IN noiseWords + has symbols in middle
//		/**
//		 * If (word.contains(symbols))
//		 * 		return null
//		 * else if (noiseWords.contains(word)
//		 * 		return null
//		 * else
//		 * 		return word
//		 *  
//		 */
//		
//		boolean hasNonLetters = false;
//		for (char ch : word.toCharArray()) {
//		  if (!Character.isLetter(ch)){
//		  //if (!Character.isLetterOrDigit(ch)) {
//		    hasNonLetters = true;
//		    break;
//		  }
//		}
//		System.out.println(hasNonLetters);
//		String symbols = new String("~!@#$%^&*()_+-={}|:<>?[];,./\\");
//		
//		
//		
//		if (noiseWords.containsKey(word) || hasNonLetters)
//		{
//			System.out.println("Has Non-Letters  or is a KeyWord = NULL");
//			return null;
//		}
//		else 
//		{
//			System.out.println("YAY LEGIT WORD");
//			return word;
//		}
		
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * same list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion of the last element
	 * (the one at index n-1) is done by first finding the correct spot using binary search, 
	 * then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		return null;
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of occurrence frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will appear before doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matching documents, the result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of NAMES of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matching documents,
	 *         the result is null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		return null;
	}
}
