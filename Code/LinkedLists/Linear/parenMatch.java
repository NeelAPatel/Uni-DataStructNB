package Linear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class parenMatch {
	
	public static boolean parenMatch(String expression)
	{
		Stack <Character> stack = new Stack<Character>();
		for(int i = 0 ; i < expression.length();i++)
		{
			char c = expression.charAt(i);
			if(c== '(')
			{
				stack.push(c);
			}
			else if(c == ')')
			{
				stack.pop();
			}
		}
		
		
		return stack.isEmpty();
	}
	public static void main(String[] args)
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the expression: ");
		
		try {
			String expr = br.readLine();
			if(parenMatch(expr))
			{
				System.out.println("Matched");
			}
			else
			{
				System.out.println("Not matched");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
