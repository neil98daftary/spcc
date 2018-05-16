import java.util.*;
import java.io.*;

public class Assembler{
	
	HashMap<String,Symbol> symbolTable;
	HashMap<String,Literal> literalTable;
	int lc = 0;
	HashMap<Integer,Integer> baseRegister;
	ArrayList<String> result;

	public static void main(String args[]){
		new Assembler();
	}

	public Assembler(){
		try{
			symbolTable = new HashMap<String,Symbol>();
			literalTable = new HashMap<String,Literal>();
			baseRegister = new HashMap<Integer,Integer>();
			result = new ArrayList<String>();

			Scanner scan = new Scanner(new File("code.asm"));
			while(scan.hasNextLine()){
				String line = scan.nextLine();
				//System.out.println(line);
				String words[] = line.split("\\s+");
				//System.out.println(Arrays.toString(words));
				pass1(words);
			}

			scan = new Scanner(new File("code.asm"));
			while(scan.hasNextLine()){
				String line = scan.nextLine();
				//System.out.println(line);
				String words[] = line.split("\\s+");
				//System.out.println(Arrays.toString(words));
				pass2(words);
			}
		}
		catch(Exception e){
			System.out.println("File not found");
		}
	}

	private void pass1(String words[]){
		switch(words[1]){
			case "START":
				lc = Integer.parseInt(words[2]);
				if(words[0].length() > 0){
					insertSymbol(words[0],lc,1,"R");
				}
				break;
			case "USING":
				//skip perform in pass 2
				break;
			case "EQU":
				if(words[0].length() > 0){
					int value = 0;
					if(words[2].equals("*")){
						value = lc;
						insertSymbol(words[0],value,1,"R");
					}
					else{
						value = Integer.parseInt(words[2]);
						insertSymbol(words[0],value,1,"A");
					}

				}
				break;
			case "END":
				printSymbolTable();
				printLiteralTable();
				break;
			case "LA":
				performRXInPass1(words);
				break;
			case "L":
				performRXInPass1(words);
				break;
			case "A":
				performRXInPass1(words);
				break;
			case "ST":
				performRXInPass1(words);
				break;
			case "C":
				performRXInPass1(words);
				break;
			case "AR":
				performRRinPass1(words);
				break;
			case "SR":
				performRRinPass1(words);
				break;
			case "LR":
				performRRinPass1(words);
				break;
			case "LTORG":
				assignValueToLiterals();
				break;
			case "DS":
				if(words[0].length() > 0){
					insertSymbol(words[0],lc,4,"R");
				}
				//Hardcoding only for float(size 4)
				String numStr = words[2].substring(0,words[2].length() - 1);
				int num = Integer.parseInt(numStr);
				lc = lc + num*4;
				break;
			case "DC":
				if(words[0].length() > 0){
					insertSymbol(words[0],lc,4,"R");
				}
				//Hardcoding assuming only one one number is present
				lc = lc + 4;
				break;
		}
	}

	private void pass2(String words[]){
		switch(words[1]){
			case "START":
				lc = Integer.parseInt(words[2]);
				break;
			case "USING":
				setValueInBaseRegister(words);
				break;
			case "EQU":
				//skip
				break;
			case "END":
				printResult();
				break;
			case "LA":
				performRXInPass2(words);
				break;
			case "L":
				performRXInPass2(words);
				break;
			case "A":
				performRXInPass2(words);
				break;
			case "ST":
				performRXInPass2(words);
				break;
			case "C":
				performRXInPass2(words);
				break;
			case "AR":
				performRRInPass2(words);
				break;
			case "SR":
				performRRInPass2(words);
				break;
			case "LR":
				performRRInPass2(words);
				break;
			case "LTORG":
				//skip
				break;
			case "DS":
				//skip
				break;
			case "DC":
				//skip
				break;
		}
	}

	private void setValueInBaseRegister(String words[]){
		int base, value;

		try{
			base = Integer.parseInt(words[4]);
		}
		catch(Exception e){
			Symbol symbol = symbolTable.get(words[4]);
			base = symbol.value;
		}

		try{
			value = Integer.parseInt(words[2]);
		}
		catch(Exception e){
			if(words[2].equals("*")){
				value = lc;
			}
			else{
				Symbol symbol = symbolTable.get(words[2]);
				value = symbol.value;
			}
		}

		baseRegister.put(base,value);
		//System.out.println(baseRegister);
	}

	private void performRRinPass1(String words[]){
		if(words[0].length() > 0){
			insertSymbol(words[0],lc,2,"R");
		}

		lc = lc + 2;
	}

	private void performRXInPass1(String words[]){
		if(words[0].length() > 0){
			insertSymbol(words[0],lc,4,"R");
		}
		if(words[4].contains("=")){
			String name = words[4].substring(1,words[4].length());
			insertLiteral(name,0,4,"R");
		}
		lc = lc + 4;
	}

	private void performRXInPass2(String words[]){
		int firstValue,secondValue;
		int offset = 0,index = 0,base = 0;

		try{
			firstValue = Integer.parseInt(words[2]);
		}
		catch(Exception e){
			Symbol symbol = symbolTable.get(words[2]);
			firstValue = symbol.value;
		}

		try{
			secondValue = Integer.parseInt(words[4]);
		}
		catch(Exception e){
			if(words[4].contains("=")){
				Literal literal = literalTable.get(words[4].substring(1,words[4].length()));
				secondValue = literal.value;

				index = 0;
			}
			else{
				if(words[4].contains("(")){
					String indexStr = words[4].substring(words[4].indexOf("(")+1,words[4].indexOf(")"));
					Symbol indexSymbol = symbolTable.get(indexStr);
					index = indexSymbol.value;

					words[4] = words[4].substring(0,words[4].indexOf("("));
					//System.out.println(words[4]);
				}
				else{
					index = 0;
				}

				Symbol symbol = symbolTable.get(words[4]);
				secondValue = symbol.value;
				//System.out.println(secondValue);
			}

			int min_offset = 9999;
			for(Integer key : baseRegister.keySet()){
				if(secondValue >= baseRegister.get(key) && (min_offset > secondValue - baseRegister.get(key)) ){
					base = key;
					min_offset = secondValue - baseRegister.get(key);
				}
			}
			offset = min_offset;
		}
		result.add(words[1] + "\t" + firstValue + ", " + offset + "(" + index + " , " + base +")");
	}

	private void performRRInPass2(String words[]){
		int firstValue, secondValue;
		try{
			firstValue = Integer.parseInt(words[2]);
		}
		catch(Exception e){
			Symbol symbol = symbolTable.get(words[2]);
			firstValue = symbol.value;
		}

		try{
			secondValue = Integer.parseInt(words[4]);
		}
		catch(Exception e){
			Symbol symbol = symbolTable.get(words[4]);
			secondValue = symbol.value;
		}

		result.add(words[1] + "\t" + firstValue + " , " + secondValue);
	}

	private void insertSymbol(String name,int value,int length,String reloc){
		if(!symbolTable.containsKey(name)){
			Symbol symbol = new Symbol();
			symbol.name = name;
			symbol.value = value;
			symbol.length = length;
			symbol.reloc = reloc;

			symbolTable.put(name,symbol);
		}
		else{
			System.out.println("Symbol " + name + " have been repeated");
		}
	}

	private void insertLiteral(String name,int value,int length,String reloc){
		if(!literalTable.containsKey(name)){
			Literal literal = new Literal();
			literal.name = name;
			literal.value = value;
			literal.length = length;
			literal.reloc = reloc;

			literalTable.put(name,literal);
		}
		else{
			System.out.println("Literal " + name + " have been repeated");
		}
	}

	private void assignValueToLiterals(){
		if(lc%8 != 0){
			//System.out.println("Lc = " + lc);
			lc = ((int)lc/8)*8 + 8;   //making lc next multiple of 8;
			//System.out.println("Lc = " + lc);
		}
		int value = lc;
		for(String key : literalTable.keySet()){
			Literal literal = literalTable.get(key);
			literal.value = value;
			value = value + literal.length;
		}
	}

	private void printSymbolTable(){
		System.out.println("Symbol Table");
		System.out.println("Name" + "\t\t" + "Value" + "\t" + "Length" + "\t" + "Relocation");
		for(String key : symbolTable.keySet()){
			Symbol symbol = symbolTable.get(key);
			System.out.print(String.valueOf(symbol.name) + "\t\t");
			System.out.print(String.valueOf(symbol.value) + "\t");
			System.out.print(String.valueOf(symbol.length) + "\t");
			System.out.print(String.valueOf(symbol.reloc) + "\n");
		}
		System.out.println("\n\n\n");
	}

	private void printLiteralTable(){
		System.out.println("Literal Table");
		System.out.println("Name" + "\t\t" + "Value" + "\t" + "Length" + "\t" + "Relocation");
		for(String key : literalTable.keySet()){
			Literal literal = literalTable.get(key);
			System.out.print(String.valueOf(literal.name) + "\t\t");
			System.out.print(String.valueOf(literal.value) + "\t");
			System.out.print(String.valueOf(literal.length) + "\t");
			System.out.print(String.valueOf(literal.reloc) + "\n");
		}
		System.out.println("\n\n\n");
	}

	private void printResult(){
		System.out.println("Output");
		for(String output : result){
			System.out.println(output);
		}
	}
}

class Symbol{
	public String name;
	public int value;
	public int length;
	public String reloc;
}

class Literal{
	public String name;
	public int value;
	public int length;
	public String reloc;
}