package structures;

import java.util.ArrayList;

/**
 * This class implements a compressed trie. Each node of the tree is a
 * CompressedTrieNode, with fields for indexes, first child and sibling.
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {

	/**
	 * Words indexed by this trie.
	 */
	ArrayList<String> words;

	/**
	 * Root node of this trie.
	 */
	TrieNode root;

	/**
	 * Initializes a compressed trie with words to be indexed, and root node set
	 * to null fields.
	 * 
	 * @param words 
	 */
	public Trie() {
		root = new TrieNode(null, null, null);
		words = new ArrayList<String>();
	}

	public void insertWord(String word) {
		/** COMPLETE THIS METHOD **/

		word = word.toLowerCase();
		words.add(word);

		// check if tree is null
		int wordIndex = 0;

		if (root.firstChild == null) {
			// No trie
			Indexes i = new Indexes(wordIndex, (short) 0, (short) (word.length() - 1));
			TrieNode n = new TrieNode(i, null, null);
			root.firstChild = n;

			System.out.println("Word: " + word);

			System.out.println("Indexes toString: " + i.toString());
			System.out.println("TrieNode toString: " + n.toString());

		} else {

			TrieNode ptr = root.firstChild;

			int startIndex = 0;
			System.out.println();
			while (ptr != null) {
				String prefix = "";
				String currNode = words.get(ptr.substr.wordIndex);

				System.out.println("========== WORD TO INSERT: " + word);
				System.out.println("currNode: " + currNode);

				// CREATE PREFIX
				for (int i = startIndex; i < word.length(); i++) {
					if (i != currNode.length() - 1)
						if (word.charAt(i) == currNode.charAt(i))
							prefix += word.charAt(i);
						else
							break;
					else
						break;
					//System.out.println("\n88 prefix: " + prefix);
				}
				System.out.println("90 prefix: " + prefix);

				if (prefix.length() >= 1) // if any kind of prefix is found
				{

					System.out.println("START");
					System.out.println("START");
					System.out.println("START");

					TrieNode ptrTrv = ptr;
					System.out.println(ptr.toString());
					System.out.println(ptrTrv.toString());
					if (ptrTrv.firstChild != null) {
						ptrTrv = ptrTrv.firstChild;
					}

					System.out.println(ptr.toString());
					System.out.println(ptrTrv.toString());
					String newPrefix = "";
					int newStartIndex = startIndex;
					TrieNode newPtr = ptr;
					String newCurrNode = currNode;
					while (ptrTrv != null) {
						String ptrWord = words.get(ptrTrv.substr.wordIndex);
						System.out.println("126: ptrWord " + ptrWord + " "+ptrTrv.toString());
						System.out.println(" : prefix " + prefix);
						newPrefix = "";
						newCurrNode = words.get(ptrTrv.substr.wordIndex);

						for (int i = 0; i < word.length(); i++) {
							if (i != newCurrNode.length() - 1)
								if (word.charAt(i) == newCurrNode.charAt(i))
									newPrefix += word.charAt(i);
								else
									break;
							else
								break;

						}
						System.out.println("ptrWord : " + ptrWord);
						System.out.println("new prefix " + newPrefix);

						if (prefix.equals(newPrefix)) {

							newPtr = ptrTrv;
							ptrTrv = ptrTrv.sibling;

						} else if (newPrefix.equals(word.substring(0, newPrefix.length()))) {
							newPtr = ptrTrv;
							newStartIndex += newPrefix.length();

							ptrTrv = ptrTrv.firstChild;
						}
					}

					System.out.println("STOP");
					System.out.println("STOP");
					System.out.println("STOP");

					System.out.println("ptrTrv Details: ");
					if (newPtr != null) {
						System.out.println("ptrTrv word :" + words.get(newPtr.substr.wordIndex));
						System.out.println("ptrTrv val :" + newPtr.toString());
					}
					System.out.println("newPrefix :[" + newPrefix + "]" );
					System.out.println("startIndex: " + startIndex);
					System.out.println("newStartIndex: " + newStartIndex);

					System.out.println("STOP");
					System.out.println("STOP");
					// Combine Terms

					if (newPrefix.length() >= prefix.length()) {

						if (words.get(ptr.substr.wordIndex).charAt(0) == words.get(newPtr.substr.wordIndex).charAt(0)) {
							ptr = newPtr;
							prefix = newPrefix;
							currNode = newCurrNode;
						}
					}

					System.out.println();

					if (ptr.firstChild == null) {

						// fix ptr index
						ptr.substr.startIndex = (short) startIndex;
						ptr.substr.endIndex = (short) (startIndex + prefix.length() - 1);
						System.out.println("startIndex" + startIndex);
						System.out.println("newStartIndex" + newStartIndex);
						System.out.println("startIndex + prefix length " + (startIndex +  (prefix.length() -1)));
						System.out.println("startIndex + newPrefix length " + (startIndex +  (newPrefix.length() -1)));
						System.out.println("newStartIndex + prefix length " + (newStartIndex +  (prefix.length() -1)));
						System.out.println("newStartIndex + newPrefix length " + (newStartIndex +  (newPrefix.length() -1)));
						
						System.out.println("RIGHT NODE: " +(startIndex + prefix.length()));
						
						
						
						// add two children for trailing substring
						Indexes i1 = new Indexes(words.indexOf(currNode), (short) (startIndex + prefix.length()),
								(short) (currNode.length() - 1));
						TrieNode n1 = new TrieNode(i1, null, null);
						ptr.firstChild = n1;
						System.out.println("LEFT NODE: "+ (startIndex +prefix.length()));

						Indexes i2 = new Indexes(words.indexOf(word), (short) (startIndex + prefix.length()),
								(short) (word.length() - 1));
						TrieNode n2 = new TrieNode(i2, null, null);
						ptr.firstChild.sibling = n2;
						System.out.println("RIGHT NODE: " +(startIndex + prefix.length()));
						break;

					} else {

						// startIndex += prefix.length();
						startIndex += (prefix.length() - 1);
						ptr = ptr.firstChild;
						continue;
					}

				} // prefix exists
				else {
					// sibling
					if (ptr.sibling == null) {
						Indexes i = new Indexes(words.indexOf(word), (short) (0), (short) (word.length() - 1));
						TrieNode n = new TrieNode(i, null, null);
						ptr.sibling = n;
						break;
					} else {
						ptr = ptr.sibling;
					}
				}

				print();
			}

		}

	}

	/**
	 * Given a string prefix, returns its "completion list", i.e. all the words
	 * in the trie that start with this prefix. For instance, if the tree had
	 * the words bear, bull, stock, and bell, the completion list for prefix "b"
	 * would be bear, bull, and bell; for prefix "be" would be bear and bell;
	 * and for prefix "bell" would be bell. (The last example shows that a
	 * prefix can be an entire word.) The order of returned words DOES NOT
	 * MATTER. So, if the list contains bear and bell, the returned list can be
	 * either [bear,bell] or [bell,bear]
	 * 
	 * @param prefix
	 *            Prefix to be completed with words in trie
	 * @return List of all words in tree that start with the prefix, order of
	 *         words in list does not matter. If there is no word in the tree
	 *         that has this prefix, null is returned.
	 */
	public ArrayList<String> completionList(String prefix) {
		/** COMPLETE THIS METHOD **/

		/** FOLLOWING LINE IS A PLACEHOLDER FOR COMPILATION **/

		ArrayList<String> list = new ArrayList<String>();
		list = reccursiveAdd(root, 1, words, list);

		ArrayList<String> foundWords = new ArrayList<String>();

		for (int i = 0; i < list.size(); i++) {
			String word = list.get(i);
			if (word.length() >= prefix.length())
				if (word.substring(0, prefix.length()).equals(prefix))
					if (!(foundWords.contains(word)))
						foundWords.add(word);
		}

		if (foundWords.isEmpty())
			return null;

		return foundWords;
	}

	private ArrayList<String> reccursiveAdd(TrieNode root, int indent, ArrayList<String> words,
			ArrayList<String> list) {
		if (root == null) {
			return list;
		}
		for (int i = 0; i < indent - 1; i++) {
		}

		if (root.substr != null) {
			list.add(words.get(root.substr.wordIndex));
		}

		for (int i = 0; i < indent - 1; i++) {

		}

		for (TrieNode ptr = root.firstChild; ptr != null; ptr = ptr.sibling) {
			for (int i = 0; i < indent - 1; i++) {
			}
			reccursiveAdd(ptr, indent + 1, words, list);
		}

		return list;
	}

	public void print() {
		print(root, 1, words);
	}

	private static void print(TrieNode root, int indent, ArrayList<String> words) {
		if (root == null) {
			return;
		}
		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}

		if (root.substr != null) {
			System.out.println("      " + words.get(root.substr.wordIndex));
		}

		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		System.out.println("(" + root.substr + ")");

		for (TrieNode ptr = root.firstChild; ptr != null; ptr = ptr.sibling) {
			for (int i = 0; i < indent - 1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent + 1, words);
		}
	}
}
