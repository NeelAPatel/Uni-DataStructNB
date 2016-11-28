package search;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

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
		
//		// index all keywords
//		if (docsFile.equals(""))
//			return;
		
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			System.out.println("CurrentFile: " + docFile);
			HashMap<String,Occurrence> kws = loadKeyWords(docFile);
			
			System.out.println("load done, merge start");
			mergeKeyWords(kws);
		}
		
		
		
		System.out.println("MAKEINDEX FINISHED");
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
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(new File(docFile));
		
		
		while (sc.hasNext())
		{	
			//Import word from file
			String importedWord = getKeyWord(sc.next());
			
			if(importedWord != null) // if imported word is null, then no point on importing
			{

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
		
		
			
		//Check to see if word exists in Hash or not
		/**
		 * If (key exists in hash)
		 * 		raise frequency
		 * or else
		 * 		add it in 
		 */
		
		
		System.out.println("LOAD KEYWORDS DONE");
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
		
		Set<Entry<String, Occurrence>> entrySet1 = kws.entrySet();
		Iterator<Entry<String, Occurrence>> entrySetIterator = entrySet1.iterator();
		while (entrySetIterator.hasNext()) {
			
			
		   Entry<String, Occurrence> entry = entrySetIterator.next();
		   String key = entry.getKey();

		  // System.out.println("WHILE: key: " + key);
		   
		   if(keywordsIndex.containsKey(key)) //If it does contain the item then increase occurence
			{
				keywordsIndex.get(key).add(kws.get(key));
				insertLastOccurrence(keywordsIndex.get(key));
				keywordsIndex.put(key, keywordsIndex.get(key));
			}
			else //if it doesn't contain key, then add to keywords.index;
			{
				
				
				ArrayList<Occurrence> occurence = new ArrayList<Occurrence>();
				occurence.add(kws.get(key));
				keywordsIndex.put(key, occurence);
			}
		}
		
		
		
//		for (String key: kws.keySet())
//		{
//			 System.out.println("FOR: key: " + key);
//		}
	
		
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
		//System.out.println("\n Word in LowerCase: <" + word + ">");
		if (word.length() == 1)
		{
			return null;
		}
		//System.out.println(word);
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
			if (Character.isLetter(word.charAt(i)) == false)
			{
				return null;
			}
				
		}
		
		//checks if noiseword
		if (noiseWords.containsKey(word))
		{
			return null;
		}
		
		
		//pass all the tests = return word.
		//System.out.println(word + " is allowed");
		return word;
		
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
		if (occs.size() == 1)
			return null;
		
		ArrayList<Integer> midValues = new ArrayList<Integer>();
		int last = occs.get(occs.size()-1).frequency;
		
		//Binary search
		int low = 0, high = occs.size()- 2; 
		int mid; 
		
		while (low < high) 
		{
			mid = (low + high) / 2;
			Occurrence atMid = occs.get(mid);
			midValues.add(mid);
			
			if (atMid.frequency == occs.get(occs.size()-1).frequency)
			{
				low = mid;
				high = mid;
				break;
			}
			else if (atMid.frequency > occs.get(occs.size()-1).frequency)
				low = mid+1;
			else if (atMid.frequency < occs.get(occs.size()-1).frequency)
				high = mid;

		} // while loop
		
		
		
		//adding into occurance
		int index = low;
		if (occs.get(index).frequency < last)
			occs.add(index, occs.remove(occs.size()-1));
		else
			occs.add(index+1, occs.remove(occs.size()-1));
		
		return midValues;
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
		ArrayList<String> docs = new ArrayList<String>();
		ArrayList<Occurrence> o1 = null; 
		ArrayList<Occurrence> o2 = null;
		
		if (keywordsIndex.get(kw1) != null)
			o1 = keywordsIndex.get(kw1);
		
		if (keywordsIndex.get(kw2) != null)
			o2 = keywordsIndex.get(kw2);
		
		
		if (o1 == null && o2 == null) // empty | empty
			return docs;
		else if (o1 != null && o2 == null) // Occupied | empty
		{
		
			int i = 0;
			while (i < o1.size() && docs.size()<5)
			{
				if (docs.contains(o1.get(i)))
					continue;
				else
					docs.add(o1.get(i).document);
				
				i++;
			}
			
		}
		else if (o1 == null && o2 != null) // empty | occupied
		{
			int i = 0;
			if (docs.size() < 5)
				while (i < o2.size() && docs.size()<5)
				{
					if (docs.contains(o2.get(i)))
						continue;
					else
						docs.add(o2.get(i).document);
					
					i++;
				}
		}
		else // occupied | occupied
		{
		int i1 = 0;
		int i2 = 0;
		
		while (i1 < o1.size() && i2 < o2.size() && docs.size()<5)
		{
			Occurrence occ1 = o1.get(i1);
			Occurrence occ2 = o2.get(i2);
			
			
			if (occ1.frequency > occ2.frequency)
			{
				if (!docs.contains(occ1.document))
					docs.add(occ1.document);
				i1++;
			}
			else if (occ1.frequency < occ2.frequency)
			{
				if (!docs.contains(occ2.document))
					docs.add(occ2.document);
				i2++;
			}
			else
			{
				if (!docs.contains(occ1.document)) 
					docs.add(occ1.document);
				if (docs.size() < 5 && !docs.contains(occ2.document))
					docs.add(occ2.document);
				
				i1++;
				i2++;
			}
		}	
			if (i1 == o1.size())
			{
				while (i2 < o2.size() && docs.size() < 5)
				{
					if (!docs.contains(o2.get(i2).document))
						docs.add(o2.get(i2).document);
					
					i2++;
				}
			}
			if (i2 == o2.size())
			{
				while (i1 < o2.size() && docs.size() < 5)
				{
					if (!docs.contains(o1.get(i1).document))
						docs.add(o1.get(i1).document);
					i1++;
				}// while
			}//if
		
		}
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
	return docs;
		
	}
}
