package tree;

public class BST <T extends Comparable<T>>
{
	BSTNode <T> root;
	int size;
	public BST()
	{
		root = null;
		size = 0;
	}
	
	
	public void insert (T key)
	{
		BSTNode <T> ptr = root;
		BSTNode <T> p = null;
		int c = 0;
		while (ptr != null)
		{
			c = key.compareTo(ptr.key);
			if (c == 0)
			{
				throw new IllegalArgumentException (key + " already")
			}
		}
	}
	
	

}
