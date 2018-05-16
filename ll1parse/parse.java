import java.util.*;
import java.lang.*;
public class parse{
	static char[] stack = new char[20];
	static int top;
	static ArrayList<Table> prods = new ArrayList<Table>();
	public static void main(String[] args){
		top = -1;
		prods.add(new Table('E','i',"TX"));
		prods.add(new Table('E','(',"TX"));
		prods.add(new Table('X','+',"+TX"));
		prods.add(new Table('X',')',"^"));
		prods.add(new Table('X','$',"^"));
		prods.add(new Table('T','i',"FY"));
		prods.add(new Table('T','(',"FY"));
		prods.add(new Table('Y','+',"^"));
		prods.add(new Table('Y','*',"*FY"));
		prods.add(new Table('Y',')',"^"));
		prods.add(new Table('Y','$',"^"));
		prods.add(new Table('F','i',"i"));
		prods.add(new Table('F','(',"(E)"));
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the input to be checked");
		String input = sc.next();
		System.out.println("INPUT\t\tACTION\t\tSTACK");
		push('$');
		push('E');
		for(int i = 0; i < input.length();){
			char tsym = stack[top];
			boolean found = false;
			if(stack[top] == '$' && input.charAt(i) == '$'){
				System.out.println("String is derivable");
				break;
			}
			if(stack[top] == '^'){
				pop();
				continue;
			}
			for(int j = 0 ; j < prods.size(); j++){
				Table x = prods.get(j);
				if(x.getCol() == input.charAt(i) && x.getRow() == tsym){
					found = true;
					String result = x.getProd();
					pop();
					for(int k = result.length() - 1; k >= 0; k--){
						push(result.charAt(k));
					}
				System.out.print(input.substring(i)+"\t\t"+result+"\t\t");
				for(int a=0; a<=top; a++)
					System.out.print(stack[a]+", ");
				System.out.println("");
				}
			}
			if(!found){
				System.out.println("String is not derivable");
				break;
			}
			if(stack[top] == input.charAt(i)){
				i++;
				pop();
			}
		}
	}
	static void pop(){
		top--;
		//return stack[top--];
	}
	static void push(char x){
		stack[++top] = x;
	}
}



class Table{
	char row;
	char col;
	String prod;
	Table(char row, char col, String result){
		this.row = row;
		this.col = col;
		prod = result;
	}
	public char getRow(){
		return row;
	}
	public char getCol(){
		return col;
	}
	public String getProd(){
		return prod;
	}
}