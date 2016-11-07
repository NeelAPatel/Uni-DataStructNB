package structures;

import java.util.ArrayList;

/**
 * This class implements a compressed trie. Each node of the tree is a CompressedTrieNode, with fields for
 * indexes, first child and sibling.
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie2 {
	
	/**
	 * Words indexed by this trie.
	 */
	ArrayList<String> words;
	
	/**
	 * Root node of this trie.
	 */
	TrieNode root;
	
	/**
	 * Initializes a compressed trie with words to be indexed, and root node set to
	 * null fields.
	 * 
	 * @param words
	 */
	public Trie2() {
		root = new TrieNode(null, null, null);
		words = new ArrayList<String>();
	}
	
	/**
	 * Inserts a word into this trie. Converts to lower case before adding.
	 * The word is first added to the words array list, then inserted into the trie.
	 * 
	 * @param word Word to be inserted.
	 */
	public void insertWord(String word)
	{
		/** COMPLETE THIS METHOD **/
		//add words from file to array list words
		word = word.toLowerCase();	
		words.add(word);
		
		if(root.firstChild == null)
		{
			Indexes newindex = new Indexes(0,(short)0,(short)(word.length()-1));
			TrieNode newnode = new TrieNode(newindex,null,null);
			root.firstChild = newnode;
			return;
		}
		else
		{
			for(short arraylistindex = 0; arraylistindex < words.size()-1; arraylistindex++)
			{
				TrieNode trieindex = root.firstChild;
				TrieNode parentindex = root;

					// loop through each of the root's children to find a matching string to the word being added 
					// using do - while will ensure that this section will be executed at least once				
					short commonindex = compareWords(words.get(arraylistindex),word);
					
					while(trieindex.sibling != null)
					{
						if(words.get(trieindex.substr.wordIndex).charAt(0) == word.charAt(0))
						{
							//if the trieindex has children, then check if there is a better match with a child
							if(trieindex.firstChild != null)
							{
								TrieNode temp = trieindex.firstChild;
								while(temp.sibling != null)
								{
									if(temp.substr.wordIndex != trieindex.substr.wordIndex)
									{
										short tempcommonindex = compareWords(words.get(temp.substr.wordIndex),word);
										if(tempcommonindex > commonindex)
										{
											trieindex = temp;
											break;
										}
										else
										{
											temp = temp.sibling;
										}
									}
									else
									{
										temp = temp.sibling;
									}
								}
							}							
							break;
						}
						else
						{
							trieindex = trieindex.sibling;
						}	
					}
					
					if(commonindex != 0)
					{
						do
						{
							addToSubtree(trieindex, parentindex,commonindex, arraylistindex, word); 
							break;
						} while(trieindex.firstChild != null);
					} // if
					else
					{	
						do
						{
							addToSubtree(trieindex, parentindex,commonindex, arraylistindex, word);
							break;
						}while(trieindex.firstChild != null);
					}
			} // for
		}			
	}
	
	private void addToSubtree(TrieNode trieindex, TrieNode parentindex, short commonindex, short arraylistindex, String word)
	{
		// check whether the child starts with the same index as the word match with the array list words 
		if(trieindex.substr.wordIndex == arraylistindex)
		{
			if ((commonindex ==0) && (trieindex.sibling == null))
			{
				// create a sibling as there is nothing common and there is no existing sibling;
				// if there is a sibling, then continue with the loop
				// create a sibling of trieindex
				Indexes newindex = new Indexes((int)(words.size()-1),commonindex,(short)(words.get(words.size()-1).length()-1));
				TrieNode newnode = new TrieNode(newindex,null,null);
				trieindex.sibling = newnode;
				trieindex = trieindex.sibling;
				return;
			}
			if (trieindex.substr.endIndex > (short) (commonindex-1) )
			{
				// found less in common than previous matches
				if(trieindex.firstChild != null)
				{
					// save parent's end index
					short original_endIndex = trieindex.substr.endIndex;
					
					//changing the parent's startIndex and creating 2 children
					trieindex.substr.endIndex = (short)(commonindex-1);
					
					// create first child
					Indexes newindex = new Indexes((int)arraylistindex, (short)(commonindex), original_endIndex);
					TrieNode newnode = new TrieNode(newindex,null,null);
					
					//move the firstchild to newnode
					newnode.firstChild = trieindex.firstChild;
					
					//link the parent to child
					trieindex.firstChild = newnode;
					
					//move the trieindex down
					trieindex = trieindex.firstChild;
												
					//create the second child
					//link the siblings
					//update the second child
					Indexes secondindex = new Indexes(words.size()-1,(short)(commonindex),(short)(word.length()-1));
					TrieNode secondnode = new TrieNode(secondindex,null,null);
					trieindex.sibling = secondnode;
					trieindex = trieindex.sibling;	
					return;
				}
				else
				{
					trieindex.substr.endIndex = (short)(commonindex-1);
					Indexes newindex = new Indexes(arraylistindex,commonindex,(short)(words.get(arraylistindex).length()-1));
					TrieNode newnode = new TrieNode(newindex,null,null);	
					trieindex.firstChild = newnode;
					trieindex = trieindex.firstChild;
					parentindex = parentindex.firstChild;
					Indexes secondchildindex = new Indexes(words.size()-1,commonindex,(short)(word.length()-1));
					TrieNode secondchildnode = new TrieNode(secondchildindex,null,null);
					trieindex.sibling = secondchildnode;
					trieindex = trieindex.sibling;
				}
			}	
			else if(trieindex.substr.endIndex == (short) (commonindex-1))
			{
				// if trieindex has a sibling
				if(words.get(trieindex.substr.wordIndex).charAt(0) != word.charAt(0))
				{
					// if the startIndex is the same, then go to the end and add a sibling
					while (trieindex.sibling != null)
					{
						trieindex = trieindex.sibling;
					}
				}
				if(trieindex.firstChild != null)
				{
					// save parent's end index
					short original_endIndex = trieindex.substr.endIndex;
					
					if(trieindex.substr.endIndex == commonindex-1)
					{
						//if the common letters then add a child at the end
						parentindex = trieindex;
						trieindex = trieindex.firstChild;
						int newcommonindex = commonindex;
						while (trieindex.sibling !=null)
						{
							newcommonindex = compareWords (word, words.get(trieindex.substr.wordIndex));
							if (newcommonindex > commonindex)
							{
								break;
							}
							else
							{
								trieindex = trieindex.sibling;
							}
						}
						//check the last child
						newcommonindex = compareWords (word, words.get(trieindex.substr.wordIndex));
						if (newcommonindex > commonindex)
						{
							// created two children
						}
						else if (newcommonindex == commonindex)
						{
							// add a new node to parentindex
							Indexes newindex = new Indexes((words.size()-1), (short)(commonindex), (short)(word.length()-1));
							TrieNode newnode = new TrieNode(newindex,null,null);
							trieindex.sibling = newnode;
							trieindex = trieindex.sibling;
						}
						return;
					}
					//changing the parent's startIndex and creating 2 children
					trieindex.substr.endIndex = (short)(commonindex-1);
					
					// create first child
					Indexes newindex = new Indexes((int)arraylistindex, (short)(commonindex), original_endIndex);
					TrieNode newnode = new TrieNode(newindex,null,null);
					
					//move the firstchild to newnode
					newnode.firstChild = trieindex.firstChild;
					
					//link the parent to child
					trieindex.firstChild = newnode;
					
					//move the trieindex down
					trieindex = trieindex.firstChild;
												
					//create the second child
					//link the siblings
					//update the second child
					Indexes secondindex = new Indexes(words.size()-1,(short)(commonindex),(short)(word.length()-1));
					TrieNode secondnode = new TrieNode(secondindex,null,null);
					trieindex.sibling = secondnode;
					trieindex = trieindex.sibling;	
					return;
				}
				else
				{
					// add a new node at the end
					Indexes newindex = new Indexes(words.size()-1,(short)(commonindex-1),(short)(word.length()-1));
					TrieNode newnode = new TrieNode(newindex,null,null);
					trieindex.sibling = newnode;
				}
			}
			else if ( trieindex.substr.endIndex < (short) (commonindex-1))
			{
				boolean foundbettermatch = false;
				if(trieindex.firstChild != null)
				{
					TrieNode temp = trieindex.firstChild;					
					while(temp.sibling != null)
					{
						if(temp.substr.wordIndex != arraylistindex)
						{
							short tempcompare = compareWords(words.get(trieindex.substr.wordIndex), word);
							if(tempcompare > commonindex)
							{
								trieindex = temp;
								foundbettermatch = true;
							}
							else
							{
								temp = temp.sibling;
							}
						}
						else
						{
							temp = temp.sibling;
						}
					}
				}
				if(!foundbettermatch)
				{
					trieindex = trieindex.firstChild;
				}
				trieindex.substr.endIndex = (short) (commonindex -1);
				// add a new node at the end
				Indexes newindex = new Indexes(arraylistindex, commonindex,(short)(words.get(arraylistindex).length()-1));
				TrieNode newnode = new TrieNode(newindex,null,null);
				trieindex.firstChild = newnode;
				trieindex = trieindex.firstChild;
				
				Indexes secondchildindex = new Indexes(words.size()-1, commonindex, (short)(word.length()-1));
				TrieNode secondchildnode = new TrieNode(secondchildindex,null,null);
				trieindex.sibling = secondchildnode;
				trieindex = trieindex.sibling;
				return;
			}
		}
		else
		{
			boolean foundbettermatch = false;
			short tempcompare =0;
			TrieNode temp = trieindex.firstChild;					

			if((trieindex.firstChild != null)&&(words.get(arraylistindex).charAt(0)==word.charAt(0)))
			{
				while(temp.sibling != null)
				{
					if(temp.substr.wordIndex != arraylistindex)
					{
						tempcompare = compareWords(words.get(temp.substr.wordIndex), word);
						if(tempcompare > commonindex)
						{
							trieindex = temp;
							foundbettermatch = true;
							break;
						}
						else
						{
							temp = temp.sibling;
						}
					}
					else
					{
						temp = temp.sibling;
					}
				}
			}
			
			if((foundbettermatch)&&(trieindex.firstChild.substr.wordIndex != arraylistindex))
			{
				return;
			}
			if(trieindex.substr.wordIndex != arraylistindex)
			{
				if((trieindex.firstChild != null) && (words.get(arraylistindex).charAt(0) == word.charAt(0)))
				{
					trieindex = trieindex.firstChild;
					TrieNode tempindex = trieindex;
					while (tempindex.sibling != null)
					{
						// if word is already inserted, then return
						if (tempindex.substr.wordIndex == (words.size()-1))
						{
							return;
						}
						else
						{
							tempindex = tempindex.sibling;
						}
							
					}
					
					if (tempindex.substr.wordIndex == (words.size()-1))
					{
						//check the last sibling to see if that is the word we are inserting
						return;
					}
					
					while(trieindex.substr.wordIndex != arraylistindex)
					{
						if(trieindex.sibling != null)
						{
							trieindex = trieindex.sibling;
						}
						else
						{
							break;
						}
					}
					// update nodes in subtree and parent nodes
					int newcommonindex = commonindex;
					while (trieindex.sibling !=null)
					{
						newcommonindex = compareWords (word, words.get(trieindex.substr.wordIndex));
						if (newcommonindex > commonindex)
						{
							return;
						}
						else
						{
							trieindex = trieindex.sibling;
						}
					}
					short original_endIndex = (short) ((words.get(trieindex.substr.wordIndex)).length()-1);
					trieindex.substr.endIndex = (short)(commonindex-1);
					Indexes newindex = new Indexes(trieindex.substr.wordIndex, commonindex, original_endIndex);
					TrieNode newnode = new TrieNode(newindex,null,null);
					trieindex.firstChild = newnode;
					trieindex = trieindex.firstChild;

					Indexes secondchildindex = new Indexes(words.size()-1, commonindex,(short) (word.length()-1));
					TrieNode secondchildnode = new TrieNode(secondchildindex,null,null);
					trieindex.sibling = secondchildnode;
				}
			}
			
			if(word.charAt(0) == words.get(trieindex.substr.wordIndex).charAt(0))
			{
				return;
			}
			else if(arraylistindex != words.size()-1)
			{
				return;
			}
			else if ((trieindex.sibling == null) && (trieindex.substr.wordIndex != arraylistindex) 
					&&(parentindex.firstChild.substr.wordIndex == arraylistindex))
			{
				// create a sibling of trieindex
				Indexes newindex = new Indexes((int)arraylistindex,(short)(commonindex-1),(short)(words.get(arraylistindex).length()-1));
				TrieNode newnode = new TrieNode(newindex,null,null);
				trieindex.sibling = newnode;
				trieindex = trieindex.sibling;
				return;
			}
			else
			{
				if(trieindex.sibling != null)
				{
					trieindex = trieindex.sibling;
				}
			}
		}	
	}
	
	/*
	 * compareWords compares the two words and returns 0 if nothing is common between them
	 * or it returns number of common characters
	 */
	private short compareWords(String first_word, String second_word)
	{
		short common_index = 0;
		for (int i=0; i < first_word.length();i++)
		{
			if ( first_word.charAt(i) == second_word.charAt(i))
			{
				common_index++;
			}
			else
			{
				break;
			}
		}
		return common_index;
	}
	

	public ArrayList<String> completionList(String prefix) 
	{
		/** COMPLETE THIS METHOD **/		
		/** FOLLOWING LINE IS A PLACEHOLDER FOR COMPILATION **/
		/** REPLACE WITH YOUR IMPLEMENTATION **/
		
		TrieNode parentindex = root.firstChild;
		TrieNode bestmatchNode = null;		
		ArrayList<String> answer = new ArrayList<String>();			
		bestmatchNode = find_best_match(prefix,parentindex,words,bestmatchNode);
		answer = Construct_Arraylist (bestmatchNode, words);		
		return answer;
	}
	
	private TrieNode find_best_match (String prefix, TrieNode startptr, ArrayList<String> words, TrieNode bestmatchNode)
	{
		String match_string = "";	
		for (TrieNode pointer=startptr; pointer != null; pointer=pointer.sibling)
		{
			if(lookupSubstring(pointer.substr).charAt(0) == prefix.charAt(0))
			{
				//check if the length of the prefix is greater, less than, or equal to length of matched
				match_string = lookupSubstring(pointer.substr);
				if(match_string.startsWith(prefix))
				{
					//if it gets here, print everything under the tree
					return pointer;
				}
				else if (pointer.firstChild !=null)
				{
					bestmatchNode = find_best_match (prefix, pointer.firstChild, words, bestmatchNode);
				}
			}					
		}
		return bestmatchNode;
	}
	
	private String lookupSubstring(Indexes stringindex)
	{
		String answer = "";
		for(int i = 0; i <= stringindex.endIndex; i++)
		{
			// lookup word from index 0 to endIndex
			answer += words.get(stringindex.wordIndex).charAt(i);
		}
		return answer;
	}
	
	public void print() {
		print(root, 1, words);
	}
	
	private ArrayList<String> Construct_Arraylist (TrieNode ptr, ArrayList<String> words)
	{
		ArrayList<String> answer = new ArrayList<String>();
				
		if (ptr == null) {
			return answer;
		}

		if (ptr.substr != null) {
			if (!(answer.contains(words.get(ptr.substr.wordIndex))))
			{
				answer.add(words.get(ptr.substr.wordIndex));
			}
		}
		
		for (TrieNode pointer=ptr.firstChild; pointer != null; pointer=pointer.sibling) {
			answer.addAll(Construct_Arraylist(pointer,words));
		}
		
		ArrayList<String> finalanswer = new ArrayList<String>();
		for (int i=0; i < answer.size();i++)
		{
			if (!(finalanswer.contains(answer.get(i))))
			{
				finalanswer.add(answer.get(i));
			}
		}
		
		return finalanswer;
	}

	
	private static void print(TrieNode root, int indent, ArrayList<String> words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			System.out.println("      " + words.get(root.substr.wordIndex));
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		System.out.println("(" + root.substr + ")");
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }