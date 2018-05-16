import java.util.*;
import java.io.*;

public class MacroCR{

	private ArrayList<String> MDT;	//Creating MDT table to store Macro definition
	private int MDTC = 0;	//pointer to MDT table
	private HashMap<String,Integer> MNT;	//Creating MNT table to store Macro Name and pointer to MDT
	private int MNTC = 0;	//pointer to MNT Table
	private ArrayList<String> ALA;

	public static void main(String args[]){
	    new MacroCR();
	}

	public MacroCR(){
		try{
	        File file = new File("macroCR.asm");
	        Scanner scan = new Scanner(file);
	        MDT = new ArrayList<String>();
	        MNT = new HashMap<String,Integer>();
	        boolean flag = false;

	        while(scan.hasNextLine()){
	        	String line = scan.nextLine();
	        	line = line.trim();

	        	if(line.length() <=0 )
	        		continue;

	        	if(line.equals("MACRO")){
	        		//Beginning of macro
	        		ALA = new ArrayList<String>();
	        		flag = true;
	        		line = scan.nextLine();
	        		line = line.trim();	//removing extra space at start and end
	        		//Assuming that it start with Macro name and no starting argument
	        		String words[] = line.split("\\s+");	//spliting on space, tab
	        		MDT.add(line);
	        		MNT.put(words[0],MDTC);
	        		MDTC++;
	        		MNTC++;

	        		for(int i=1;i<words.length;i++){
	        			if(words[i].startsWith("&")){
	        				//This is a argument
	        				ALA.add(words[i]);
	        			}
	        		}
	        		System.out.println("ALA Table for " + words[0]);
	        		printALATable(ALA);
	        	}
	        	else if(flag){
	        		//This is content of macro
	        		//Assuming argument is present only at the end
	        		String words[] = line.split("\\s+");

	        		if(words[words.length-1].startsWith("&")){
	        			//This is argument replace with #index
	        			String replc = "#" + ALA.indexOf(words[words.length-1]);
	        			line = line.replaceAll(words[words.length-1],replc);
	        		}

	        		MDT.add(line);
	        		MDTC++;
	        		if(line.equals("MEND"))
	        			flag = false;
	        	}
	        	else{
	        		String words[] = line.split("\\s+");
	        		//checking if this a macro
	        		if(MNT.containsKey(words[0])){
	        			ALA = new ArrayList<String>();
	        			//skipping ',(comma)' by incrementing i with 2
	        			for(int i=1;i<words.length;i=i+2){
	        				ALA.add(words[i]);
	        			}
	        			expandMacro(ALA,MNT.get(words[0]));
	        		}
	        		else
	        			System.out.println(line);
	        	}
	        }
	        scan.close();
	  		printMDTTable();
	  		printMNTTable();	
	    }
	    catch(Exception e){
	        System.out.println("Exception aaya hai");
	    }
	}

	private void expandMacro(ArrayList<String> ALA, int index){
		int i = index + 1;
		while(true){
			String line = MDT.get(i);

			if(line.equals("MEND"))
				return;

			String words[] = line.split("\\s+");
			//Assuming only last element is a argument
    		if(words[words.length-1].startsWith("#")){
    			//This is argument replace with #index
    			String indexNo = line.substring(line.indexOf("#") + 1);
    			String replc = ALA.get(Integer.parseInt(indexNo));
    			line = line.replaceAll(words[words.length-1],replc);
    			System.out.println(line);
    		}
    		else
    			System.out.println(line);

			i++;
		}

	}

	private void printALATable(ArrayList<String> ALA){
		System.out.println("Index" + "\t" + "Argument");

		for(int i=0;i<ALA.size();i++)
			System.out.println(i + "\t" + ALA.get(i));
		System.out.println();
	}

	private void printMDTTable(){
		System.out.println("\nMDT Table");
		System.out.println("Index" + "\t" + "Card");
		for(int i=0;i<MDT.size();i++){
			System.out.println(i + "\t" + MDT.get(i));
		}
	}

	private void printMNTTable(){
		System.out.println("\nMNT Table");
		System.out.println("Index" + "\t" + "Name" + "\t" + "MDT Index");
		int i = 0;
		for(String key : MNT.keySet())
			System.out.println((i++) + "\t" + key + "\t" + MNT.get(key));
	}
}
