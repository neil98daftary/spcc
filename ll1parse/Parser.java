import java.util.*;
import java.io.*;

/*
E' = X
T' = Y
*/

class Parser{
        public static void main(String[] args){
                String input;
                System.out.println("\nEnter the string\n");
                Scanner sc = new Scanner(System.in);
                input = sc.nextLine();
                input+="$";
                //System.out.println(input);
                ArrayList<String> temp = new ArrayList();
                
                HashMap <Character,ArrayList>   tableMap = new HashMap();
                temp = new ArrayList<String>();
                temp.add("TX");
                temp.add("");
                temp.add("");
                temp.add("TX");
                temp.add("");
                temp.add("");
                tableMap.put('E',temp);
                temp = new ArrayList<String>();
                temp.add("");
                temp.add("+TX");
                temp.add("");
                temp.add("");
                temp.add("e");
                temp.add("e");
                tableMap.put('X',temp);
                temp = new ArrayList<String>();
                temp.add("FY");
                temp.add("");
                temp.add("");
                temp.add("FY");
                temp.add("");
                temp.add("");
                tableMap.put('T',temp);
                temp = new ArrayList<String>();
                temp.add("");
                temp.add("e");
                temp.add("*FY");
                temp.add("");
                temp.add("e");
                temp.add("e");
                tableMap.put('Y',temp);
                temp = new ArrayList<String>();
                temp.add("i");
                temp.add("");
                temp.add("");
                temp.add("(E)");
                temp.add("");
                temp.add("");
                tableMap.put('F',temp); 
                
                //System.out.println(tableMap);
                
                Stack <Character> st = new Stack();
                
                st.push('$');
                st.push('E');
                
                //char top = st.peek();
                
                System.out.println("Input\tAction\tStack");
                
                while(st.peek() != '$' || input.equals("$")){
                char top = st.peek();
                
                if(top =='e'){
                    st.pop();
                    top = st.peek();
                }
                System.out.print(input + '\t');
                if(input.charAt(0)==top && top!='$'){
                        input = input.substring(1);
                        st.pop();
                }else{
                        if(top=='$'){
                            break;
                        }
                        String replace = "";
                        switch(input.charAt(0)){
                                case 'i' :
                                            replace = (String)tableMap.get(top).get(0);
                                        //System.out.println(tableMap.get(top).get(0));
                                            st.pop();
                                            for(int j=replace.length()-1;j>=0;j--){
                                                st.push(replace.charAt(j));
                                            }
                                            System.out.print(replace + '\t');
                                            System.out.println(st);
                                            break;
                                case '+' :
                                            //System.out.println(top);    
                                            replace = (String)tableMap.get(top).get(1);
                                            st.pop();
                                            for(int j=replace.length()-1;j>=0;j--){
                                                st.push(replace.charAt(j));
                                            }
                                            System.out.print(replace + '\t');
                                            System.out.println(st);
                                            break;
                                case '*' :
                                            replace = (String)tableMap.get(top).get(2);
                                            st.pop();
                                            for(int j=replace.length()-1;j>=0;j--){
                                                st.push(replace.charAt(j));
                                            }
                                            System.out.print(replace + '\t');
                                            System.out.println(st);
                                            break;
                                case '(' :
                                            replace = (String)tableMap.get(top).get(3);
                                            st.pop();
                                            for(int j=replace.length()-1;j>=0;j--){
                                                st.push(replace.charAt(j));
                                            }
                                            System.out.print(replace + '\t');
                                            System.out.println(st);
                                            break;
                                case ')' :
                                            replace = (String)tableMap.get(top).get(4);
                                            st.pop();
                                            for(int j=replace.length()-1;j>=0;j--){
                                                st.push(replace.charAt(j));
                                            }
                                            System.out.print(replace + '\t');
                                            System.out.println(st);
                                            break;
                                case '$' :
                                            replace = (String)tableMap.get(top).get(5);
                                            st.pop();
                                            for(int j=replace.length()-1;j>=0;j--){
                                                st.push(replace.charAt(j));
                                            }
                                            System.out.print(replace + '\t');
                                            System.out.println(st);
                                            break;                        
                                                                            
                        }                       
                }
                
            }   
            if(input.charAt(0) == '$'){
                System.out.println("\nACCEPTED\n");
            }
            else{
                System.out.println("\nREJECTED\n");
            }
                
        }
}
