package tree;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/*
 * Binary Search Tree (BST)
 * Duplicates not allowed
 * Comparable requires T objects to implement compareTo
 * 
 * Comparable is an interface that imposes a total ordering on the objects
 * of the class that implements it.
 * An interface is not a class, it defines a set of variables and methods.
 */
public class BST <T extends Comparable<T>> {
	
	BSTNode<T> root;
	int size;
	
	public BST() {
		root = null;
		size = 0;
	}
	public void insert (T key) {
		// 1. search for the key until it fails
		BSTNode<T> ptr = root;
		BSTNode<T> p = null;
		int c = 0;
		while (ptr != null) {
			c = key.compareTo(ptr.key);
			if (c == 0) { // equal
				throw new IllegalArgumentException(key + " already in BST");
			}
			p = ptr;
			if (c < 0) {
				ptr = ptr.left;
			} else {
				ptr = ptr.right;
			}
		}
		// 2. create new node and insert it 
		BSTNode<T> node = new BSTNode(key, null, null);
		if (p == null) {
			// empty tree
			root = node;
		} else if (c < 0) {
			p.left = node;
		} else {
			p.right = node;
		}
		size++;
	}
	public void delete (T key) {
		// 1. find node x to delete (call it x)
		BSTNode<T> x = root;
		BSTNode<T> p = null;
		int c = 0;
		while (x != null) {
			c = key.compareTo(x.key);
			if (c == 0) { // found it
				break;
			}
			p = x;
			x = (c < 0) ? x.left : x.right; // ternary statement
		}
		// 2. key is not found
		if (x == null) {
			throw new NoSuchElementException(key + " not found");
		}
		// 3. check if has two children
		BSTNode<T> y = null;
		if (x.left != null && x.right != null) {
			// find inorder predecessor (y)
			y = x.left;
			p = x;
			while (y.right != null) {
				p = y;
				y = y.right;
			}
			// copy y into x
			x.key = y.key;
			
			// prepare to delete y
			x = y;
		}
		// 4. p is null, x is the root and x does not have two children
		if (p == null) {
			root = x.left != null ? x.left : x.right;
			size--;
			return;
		}
		// 5. handle case 1 and 2 at the same code
		BSTNode<T> tmp = x.right != null ? x.right : x.left;
		if (x == p.left) {
			p.left = tmp;
		} else {
			p.right = tmp;
		}
		size--;
	}
	
	private static <T extends Comparable> void inOrder (BSTNode<T> root,ArrayList<T> list){
			if (root == null)
				return;
			inOrder(root.left, list);
			list.add(root.key);
			inOrder(root.right,list);
		}
	
		
	private static void main (String[] args)
	{
		int[] array ={35, 67, 30, 20, 45, 57};
		BST<Integer> bst = new BST();
		
		for(int i = 0; i <array.length; i++)
		{
			bst.insert(i);
		}
		
		ArrayList<Integer> list = new ArrayList<Integer>();
				inOrder(bst.root,list);
				
		System.out.println(list);
		
		
	}
	
	
	
	
	
	
	
	
	
	
}
