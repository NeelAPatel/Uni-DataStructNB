package Linear;

public class BinarySearch {
	
	public static int binarySearch(int[] A, int target)
	{
		int low = 0;
		int high = A.length-1;
		
		while(low <= high)
		{
			int middle = (low+high)/2; //integer division
			if (target == A[middle])
			{
				return middle;
			}
			else
			{
				if (target < A[middle])
				{
					high = middle -1;
				}
				else
					low = middle + 1;
			}
		
		
		
		
		
		
		
		
		}
		
		return -1;
	}
	
	public static void main(String[] args){
		int[] array = { 3,10,20,53,70,99};
		System.out.println(binarySearch(array, 53));
		System.out.println(binarySearch(array, 55));
	}
}
