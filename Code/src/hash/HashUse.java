package hash;

import java.util.HashMap;

import javax.print.attribute.standard.MediaSize.Other;

class Contact {

	String firstName, lastName;
	String email;
	String phone;
	
	//modulus over size of the hashtable
	
	public Contact (String firstName, String lastName, String email)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	
	public boolean equals(Object o )
	{
		if (o == null || !(o instanceof Contact))
		{
			return false;
		}
		
		Contact other = (Contact) o;
		return email.equals(other.email);

	}
	
	
	public String toString()
	{
		return (firstName + " " + lastName + ", " + email);
	}
}

class Point{
	int x, y; 
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Point))
		{
			return false;
		}
		
		Point other = (Point) o;
		return x == other.x && y == other.y; 
	
	}
	
	
	public int hashCode()
	{
		return ("" + x + y ).hashCode();
	}
	
	
}

public class HashUse
{
	public static void main (String[] args)
	{
		HashMap<String,Contact> friends = new HashMap<String,Contact>(500,22.5f); // can specify load factor
		Contact tmp = new Contact("Sapan", "Parmar", "sap.parm@gmail.com");
		Contact tmp2 = new Contact ("Cristiano", "Ronaldo", "cr7@gmail.com");
		friends.put("Sapan",  tmp);
		friends.put("Cristiano",  tmp2);
		System.out.println(friends.get("Sapan"));
		
		// A hash table for lines, <Start, end> 
		HashMap<Point, Point> lines = new HashMap<Point, Point>(20,1.5f); 
		Point A = new Point(3,5);
		Point B = new Point(1,2);
		Point C = new Point (6,9);
		Point D = new Point (50,34);
		
		
		
		lines.put(A, B);
		lines.put(C, D);
		
		System.out.println("key: " + A + " value: " + lines.get(A));
	}
}

































