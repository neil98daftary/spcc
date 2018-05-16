import java.util.*;

public class quadruple{
	static int count = 1;
	public static void pop_two(Stack<String> stack, char op){
		String op2 =(stack.pop()).toString();
		String op1 =(stack.pop()).toString();
		System.out.println("\n"+op+"\t"+op1+"\t"+op2+"\t"+"t"+count);
		String temp = "t" + count;
		stack.push(temp);
		count++;
	}
	public static void main(String [] args){
		int n;
		String inp;
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter postfix expression");
		inp = sc.nextLine();
		Stack<String> st = new Stack<String>();
		System.out.println("Op\tOp1\tOp2\tResult");
		for(int i=0;i<inp.length();i++)
		{
			char ch =inp.charAt(i);
			if(ch == '*' | ch == '/' | ch == '+' | ch == '-' | ch == '^')
				pop_two(st,ch);
			else
				st.push(ch+"");
		}				
	}
}


