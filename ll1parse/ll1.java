
import java.io.*;
import java.util.*;
public class ll1 {
static HashMap<String, String> ll_row;
public static void main(String args[]) throws IOException {
Scanner sc = new Scanner(System.in);
ArrayList<String> stack = new ArrayList<String>();
stack.add("$");
stack.add("E");
//entering entries in table
HashMap<String, HashMap<String, String>> ll_table = new HashMap<String,
HashMap<String, String>>();
ll_row = new HashMap<String, String>();
ll_row.put("i", "TX");
ll_row.put("(", "TX");
ll_table.put("E", ll_row);
ll_row = new HashMap<String, String>();
ll_row.put("+", "+TX");
ll_row.put(")", "epsilon");
ll_row.put("$", "epsilon");
ll_table.put("X", ll_row);
ll_row = new HashMap<String, String>();
ll_row.put("i", "FY");
ll_row.put("(", "FY");
ll_table.put("T", ll_row);
ll_row = new HashMap<String, String>();
ll_row.put("+", "epsilon");
ll_row.put("*", "*FY");
ll_row.put(")", "epsilon");
ll_row.put("$", "epsilon");
ll_table.put("Y", ll_row);
ll_row = new HashMap<String, String>();
ll_row.put("i", "i");
ll_row.put("(", "(E)");
ll_table.put("F", ll_row);
System.out.println("Enter the string to be checked");
String s = sc.next();
s = s.concat("$");
System.out.println("string " + s);
int flag = 1;
int point = 0;
while (stack.size() > 0) {
String lhs = stack.get(stack.size() - 1);
//System.out.println("point is "+point);
String ans = ll_table.get(lhs).get(s.charAt(point) + "");
if (ans == null) {
flag = 0;
System.out.println("invalid string");
break;
}
if (ans.equals("epsilon"))
stack.remove(stack.size() - 1);
System.out.println("stacktop " + lhs);
System.out.println("new production " + ans);
if (lhs.charAt(0) >= 'A' && lhs.charAt(0) <= 'Z') {
if (!ans.equals("epsilon")) {
System.out.println("popped " + stack.get(stack.size() - 1));
//pop top element
stack.remove(stack.size() - 1);
//insert new char
}
for (int k = ans.length() - 1; k >= 0; k--) {
if (!ans.equals("epsilon")) {
stack.add(ans.charAt(k) + "");
System.out.println("pushed " + ans.charAt(k));
}
}
}
if (s.charAt(point) == (stack.get(stack.size() - 1)).charAt(0)) {
//pop
stack.remove(stack.size() - 1);
//move to next element
point++;
System.out.println("valid character found");
} else if (stack.get(stack.size() - 1).charAt(0) >= 'A' && stack.get(stack.size() - 1).charAt(0)
<= 'Z') {
System.out.println("go to table");
} else {
if (!ans.equals("epsilon")) {
System.out.println("Invalid String ");
flag = 0;
break;
}
}
}
if (flag == 1)
System.out.println("Valid input String");
}
}
