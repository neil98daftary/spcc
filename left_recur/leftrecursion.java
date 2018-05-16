import java.io.*;
import java.util.*;

public class leftrecursion{
	static String prod = new String();
	static ArrayList<String> init_prod = new ArrayList<String>();
	public static void main(String args[]){
		int i=0;
		Scanner sc  = new Scanner(System.in);
		System.out.println("Enter no. of productions");
		int noOfProd = sc.nextInt();
		System.out.println("Enter productions");
		while(i<noOfProd) {
			prod = sc.next();
			init_prod.add(prod);
			i++;
		}
		System.out.println("After Left Recursion:");
		for(String prod : init_prod){
			String prod_split[] = prod.split("->");
			String left = prod_split[0];
			String right[] = prod_split[1].split("\\|");
			if(right[0].startsWith(left)){
                System.out.println(left+"->"+right[1]+left+"'");
                System.out.println(left+"'"+"->"+right[0].substring(1)+left+"'"+"|e");
            }else{
            	System.out.println(prod);
            }
		}
	}
}
