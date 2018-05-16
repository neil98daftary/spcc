import java.util.*;
public class Left{
public static void main(String[] args){
	Scanner sc = new Scanner(System.in);
	int j = 0;
	System.out.println("Enter number of productions");
	int n = sc.nextInt();
	String productions;
	ArrayList<String> al=new ArrayList<String>();
	ArrayList<String> lengthOne = new ArrayList<>();
	ArrayList<String> lengthTwo = new ArrayList<>();
	while (j<n) {
		productions = sc.next();
		al.add(productions);
		j++;
	}
	for (j=0; j<n; j++) {
		System.out.println("Rules for production "+j);
		System.out.println(al.get(j));
		String prods[] = al.get(j).split(":");
		
		if (prods[1].contains(prods[0])) {
			String right[] = prods[1].split(prods[0]);
			
			String beta[] = right[1].split("\\|");
			
			String nonterm = prods[0];
			String[] rhs = prods[1].split("\\|");
			for(int i = 0; i < rhs.length; i++) {
				switch(rhs[i].length()) {
					case 1:
					lengthOne.add(rhs[i]);
					break;
					case 2:
					lengthTwo.add(rhs[i]);
					break;
				}
			}
			System.out.print("A->");
			for(int i = 0; i < lengthOne.size(); i++) {
				System.out.print(lengthOne.get(i) + "B|");
			}
			System.out.print("\n\nB->");
			for(int i = 0; i < lengthTwo.size(); i++) {
				if(lengthTwo.get(i).charAt(0) == 'A') {
					System.out.print(lengthTwo.get(i).charAt(1) + "B|");
				}
			}
			System.out.println("\u03B5\n");
			
		}
		else {
		System.out.println("Does not contain left recursion");
		}
	}
}
}