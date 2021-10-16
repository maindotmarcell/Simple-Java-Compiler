package com.company;
import com.company.ParseTree;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyntacticAnalyser {

	public static ParseTree parse(List<Token> tokens) throws SyntaxException {

		// Model Parsing table in a HashMap -- Marcell
		HashMap<String, HashMap> parseTable = new HashMap<>();
		// add rows as the value of each key
		parseTable.put("Prog", new HashMap<String, Integer>() {{
			put("public",1);
		}});
		parseTable.put("Los", new HashMap<String, Integer>() {{
			put("ID",2);
			put("}}",3);
			put(";",2);
			put("}",3);
			put("for",2);
			put("if",2);
			put("System.out.println(",2);
			put("int",2);
			put("char",2);
			put("boolean",2);
			put("",2);
		}});
		parseTable.put("Stat", new HashMap<String, Integer>() {{
			put("ID",7);
			put(";",10);
			put("while",4);
			put("for",5);
			put("if",6);
			put("System.out.println(",9);
			put("int",8);
			put("char",8);
			put("boolean",8);
		}});
		parseTable.put("While", new HashMap<String, Integer>() {{
			put("while",11);
		}});
		parseTable.put("For", new HashMap<String, Integer>() {{
			put("For",12);
		}});
		parseTable.put("Forstart", new HashMap<String, Integer>() {{
			put("ID",14);
			put(";",15);
			put("int",13);
			put("char",13);
			put("boolean",13);
			put("",15);
		}});
		parseTable.put("Forarith", new HashMap<String, Integer>() {{
			put("ID",16);
			put("(",16);
			put(")",17);
			put("num",16);
			put("",17);
		}});
		parseTable.put("If", new HashMap<String, Integer>() {{
			put("if",18);
		}});
		parseTable.put("Elseif", new HashMap<String, Integer>() {{
			put("ID",20);
			put("}}",20);
			put(";",20);
			put("while",20);
			put("for",20);
			put("if",20);
			put("else",19);
			put("System.out.println(",20);
			put("int",20);
			put("char",20);
			put("boolean",20);
			put("",20);
		}});
		parseTable.put("ElseQif", new HashMap<String, Integer>() {{
			put("else",21);
		}});
		parseTable.put("Possif", new HashMap<String, Integer>() {{
			put("{",23);
			put("for",22);
			put("",23);
		}});
		parseTable.put("Assign", new HashMap<String, Integer>() {{
			put("ID",24);
		}});
		parseTable.put("Decl", new HashMap<String, Integer>() {{
			put("int",25);
			put("char",25);
			put("boolean",25);
		}});
		parseTable.put("Possassign", new HashMap<String, Integer>() {{
			put(";",27);
			put("=",26);
			put("",27);
		}});
		parseTable.put("Print", new HashMap<String, Integer>() {{
			put("System.out.println(",28);
		}});
		parseTable.put("Type", new HashMap<String, Integer>() {{
			put("int",29);
			put("char",30);
			put("boolean",31);
		}});
		parseTable.put("Expr", new HashMap<String, Integer>() {{
			put("ID",32);
			put("(",32);
			put("'",33);
			put("true",32);
			put("false",32);
			put("num",32);
		}});
		parseTable.put("Charexpr", new HashMap<String, Integer>() {{
			put("'",34);
		}});
		parseTable.put("Boolexpr", new HashMap<String, Integer>() {{
			put(")",36);
			put(";",36);
			put("==",35);
			put("!=",35);
			put("&&",35);
			put("||",35);
			put("",36);
		}});
		parseTable.put("Boolop", new HashMap<String, Integer>() {{
			put("==",37);
			put("!=",37);
			put("&&",38);
			put("||",38);
		}});
		parseTable.put("Booleq", new HashMap<String, Integer>() {{
			put("==",39);
			put("!=",40);
		}});
		parseTable.put("Boollog", new HashMap<String, Integer>() {{
			put("&&",41);
			put("||",42);
		}});
		parseTable.put("Relexpr", new HashMap<String, Integer>() {{
			put("ID",43);
			put("(",43);
			put("true",44);
			put("false",45);
			put("num",43);
		}});
		parseTable.put("Relexpr2", new HashMap<String, Integer>() {{
			put(")",47);
			put(";",47);
			put("==",47);
			put("!=",47);
			put("&&",47);
			put("||",47);
			put("<",46);
			put("<=",46);
			put(">",46);
			put(">=",46);
			put("",47);
		}});
		parseTable.put("Relop", new HashMap<String, Integer>() {{
			put("<",48);
			put("<=",49);
			put(">",50);
			put(">=",51);
		}});
		parseTable.put("Arithexpr", new HashMap<String, Integer>() {{
			put("ID",52);
			put("(",52);
			put("num",52);
		}});
		parseTable.put("Arithexpr2", new HashMap<String, Integer>() {{
			put(")",55);
			put(";",55);
			put("==",55);
			put("!=",55);
			put("&&",55);
			put("||",55);
			put("<",55);
			put("<=",55);
			put(">",55);
			put(">=",55);
			put("+",53);
			put("-",54);
			put("",55);
		}});
		parseTable.put("Term", new HashMap<String, Integer>() {{
			put("ID",56);
			put("(",56);
			put("num",56);
		}});
		parseTable.put("Term2", new HashMap<String, Integer>() {{
			put(")",60);
			put(";",60);
			put("==",60);
			put("!=",60);
			put("&&",60);
			put("||",60);
			put("<",60);
			put("<=",60);
			put(">",60);
			put(">=",60);
			put("+",60);
			put("-",60);
			put("*",57);
			put("/",58);
			put("%",59);
			put("",60);
		}});
		parseTable.put("Factor", new HashMap<String, Integer>() {{
			put("ID",62);
			put("(",61);
			put("num",63);
		}});
		parseTable.put("Printexpr", new HashMap<String, Integer>() {{
			put("ID",64);
			put("(",64);
			put("true",64);
			put("false",64);
			put("num",64);
			put("\"",65);
		}});
		parseTable.put("Variables", new HashMap<String, Integer>() {{
			put("/",67);
			put("num",66);
			put("",67);
		}});
		parseTable.put("Variable", new HashMap<String, Integer>() {{
			put("num",68);
		}});
		parseTable.put("Operators", new HashMap<String, Integer>() {{
			put("%",70);
			put("\"",69);
			put("Stringlit",69);
			put("",70);
		}});
		parseTable.put("Operator", new HashMap<String, Integer>() {{
			put("\"",71);
			put("Stringlit",72);
		}});




		//Turn the List of Tokens into a ParseTree.
		return new ParseTree();
	}

}
