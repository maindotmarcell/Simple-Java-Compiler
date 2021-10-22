package com.company;

import apple.laf.JRSUIUtils;
import com.company.ParseTree;
import javafx.util.Pair;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyntacticAnalyser {

    private static String tokenToString(Token token) {
        switch (token.getType()) {
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case TIMES:
                return "*";
            case DIVIDE:
                return "/";
            case MOD:
                return "%";
            case ASSIGN:
                return "=";
            case EQUAL:
                return "==";
            case NEQUAL:
                return "!=";
            case LT:
                return "<";
            case LE:
                return "<=";
            case GT:
                return ">";
            case GE:
                return ">=";
            case LPAREN:
                return "(";
            case RPAREN:
                return ")";
            case LBRACE:
                return "{";
            case RBRACE:
                return "}";
            case AND:
                return "&&";
            case OR:
                return "||";
            case SEMICOLON:
                return ";";
            case PUBLIC:
                return "public";
            case CLASS:
                return "class";
            case STATIC:
                return "static";
            case VOID:
                return "void";
            case MAIN:
                return "main";
            case STRINGARR:
                return "String[]";
            case ARGS:
                return "args";
            case TYPE:
                return token.getValue() + "";
            case PRINT:
                return "System.out.println(";
            case WHILE:
                return "while";
            case FOR:
                return "for";
            case IF:
                return "if";
            case ELSE:
                return "else";
            case DQUOTE:
                return "\"";
            case SQUOTE:
                return "'";
            case ID:
                return "ID";
            case NUM:
                return "num";
            case TRUE:
                return "true";
            case FALSE:
                return "false";
            case STRINGLIT:
                return "Stringlit";
        }
        return "";
    }


    public static ParseTree parse(List<Token> tokens) throws SyntaxException {


        // Model Parsing table in a HashMap -- Marcell
        HashMap<TreeNode.Label, HashMap> parseTable = new HashMap<>();
        // add rows as the value of each key
        parseTable.put(TreeNode.Label.prog, new HashMap<String, Integer>() {{
            put("public", 1);
        }});
        parseTable.put(TreeNode.Label.los, new HashMap<String, Integer>() {{
            put("ID", 2);
            put(";", 2);
            put("while", 2); // +
            put("}", 3);
            put("for", 2);
            put("if", 2);
            put("System.out.println(", 2);
            put("int", 2);
            put("char", 2);
            put("boolean", 2);
            put("", 2);
        }});
        parseTable.put(TreeNode.Label.stat, new HashMap<String, Integer>() {{
            put("ID", 7);
            put(";", 10);
            put("while", 4);
            put("for", 5);
            put("if", 6);
            put("System.out.println(", 9);
            put("int", 8);
            put("char", 8);
            put("boolean", 8);
        }});
        parseTable.put(TreeNode.Label.whilestat, new HashMap<String, Integer>() {{
            put("while", 11);
        }});
        parseTable.put(TreeNode.Label.forstat, new HashMap<String, Integer>() {{
            put("For", 12);
        }});
        parseTable.put(TreeNode.Label.forstart, new HashMap<String, Integer>() {{
            put("ID", 14);
            put(";", 15);
            put("int", 13);
            put("char", 13);
            put("boolean", 13);
            put("", 15);
        }});
        parseTable.put(TreeNode.Label.forarith, new HashMap<String, Integer>() {{
            put("ID", 16);
            put("(", 16);
            put(")", 17);
            put("num", 16);
            put("", 17);
        }});
        parseTable.put(TreeNode.Label.ifstat, new HashMap<String, Integer>() {{
            put("if", 18);
        }});
        parseTable.put(TreeNode.Label.elseifstat, new HashMap<String, Integer>() {{
            put("ID", 20);
            put(";", 20);
            put("while", 20);
            put("}", 20); // +
            put("for", 20);
            put("if", 20);
            put("else", 19);
            put("System.out.println(", 20);
            put("int", 20);
            put("char", 20);
            put("boolean", 20);
            put("", 20);
        }});
        parseTable.put(TreeNode.Label.elseorelseif, new HashMap<String, Integer>() {{
            put("else", 21);
        }});
        parseTable.put(TreeNode.Label.possif, new HashMap<String, Integer>() {{
            put("{", 23);
            put("if", 22); // for -> if
            put("", 23);
        }});
        parseTable.put(TreeNode.Label.assign, new HashMap<String, Integer>() {{
            put("ID", 24);
        }});
        parseTable.put(TreeNode.Label.decl, new HashMap<String, Integer>() {{
            put("int", 25);
            put("char", 25);
            put("boolean", 25);
        }});
        parseTable.put(TreeNode.Label.possassign, new HashMap<String, Integer>() {{
            put(";", 27);
            put("=", 26);
            put("", 27);
        }});
        parseTable.put(TreeNode.Label.print, new HashMap<String, Integer>() {{
            put("System.out.println(", 28);
        }});
        parseTable.put(TreeNode.Label.type, new HashMap<String, Integer>() {{
            put("int", 29);
            put("char", 30);
            put("boolean", 31);
        }});
        parseTable.put(TreeNode.Label.expr, new HashMap<String, Integer>() {{
            put("ID", 32);
            put("(", 32);
            put("'", 33);
            put("true", 32);
            put("false", 32);
            put("num", 32);
        }});
        parseTable.put(TreeNode.Label.charexpr, new HashMap<String, Integer>() {{
            put("'", 34);
        }});
        parseTable.put(TreeNode.Label.boolexpr, new HashMap<String, Integer>() {{
            put(")", 36);
            put(";", 36);
            put("==", 35);
            put("!=", 35);
            put("&&", 35);
            put("||", 35);
            put("", 36);
        }});
        parseTable.put(TreeNode.Label.boolop, new HashMap<String, Integer>() {{
            put("==", 37);
            put("!=", 37);
            put("&&", 38);
            put("||", 38);
        }});
        parseTable.put(TreeNode.Label.booleq, new HashMap<String, Integer>() {{
            put("==", 39);
            put("!=", 40);
        }});
        parseTable.put(TreeNode.Label.boollog, new HashMap<String, Integer>() {{
            put("&&", 41);
            put("||", 42);
        }});
        parseTable.put(TreeNode.Label.relexpr, new HashMap<String, Integer>() {{
            put("ID", 43);
            put("(", 43);
            put("true", 44);
            put("false", 45);
            put("num", 43);
        }});
        parseTable.put(TreeNode.Label.relexprprime, new HashMap<String, Integer>() {{
            put(")", 47);
            put(";", 47);
            put("==", 47);
            put("!=", 47);
            put("&&", 47);
            put("||", 47);
            put("<", 46);
            put("<=", 46);
            put(">", 46);
            put(">=", 46);
            put("", 47);
        }});
        parseTable.put(TreeNode.Label.relop, new HashMap<String, Integer>() {{
            put("<", 48);
            put("<=", 49);
            put(">", 50);
            put(">=", 51);
        }});
        parseTable.put(TreeNode.Label.arithexpr, new HashMap<String, Integer>() {{
            put("ID", 52);
            put("(", 52);
            put("num", 52);
        }});
        parseTable.put(TreeNode.Label.arithexprprime, new HashMap<String, Integer>() {{
            put(")", 55);
            put(";", 55);
            put("==", 55);
            put("!=", 55);
            put("&&", 55);
            put("||", 55);
            put("<", 55);
            put("<=", 55);
            put(">", 55);
            put(">=", 55);
            put("+", 53);
            put("-", 54);
            put("", 55);
        }});
        parseTable.put(TreeNode.Label.term, new HashMap<String, Integer>() {{
            put("ID", 56);
            put("(", 56);
            put("num", 56);
        }});
        parseTable.put(TreeNode.Label.termprime, new HashMap<String, Integer>() {{
            put(")", 60);
            put(";", 60);
            put("==", 60);
            put("!=", 60);
            put("&&", 60);
            put("||", 60);
            put("<", 60);
            put("<=", 60);
            put(">", 60);
            put(">=", 60);
            put("+", 60);
            put("-", 60);
            put("*", 57);
            put("/", 58);
            put("%", 59);
            put("", 60);
        }});
        parseTable.put(TreeNode.Label.factor, new HashMap<String, Integer>() {{
            put("ID", 62);
            put("(", 61);
            put("num", 63);
        }});
        parseTable.put(TreeNode.Label.printexpr, new HashMap<String, Integer>() {{
            put("ID", 64);
            put("(", 64);
            put("true", 64);
            put("false", 64);
            put("num", 64);
            put("\"", 65);
        }});

        Deque<Pair<TreeNode.Label, String>> stack = new ArrayDeque<>();
        ParseTree tree = new ParseTree();
        tree.setRoot(new TreeNode(TreeNode.Label.prog, null));
        TreeNode currentNode = tree.getRoot();

        stack.push(new Pair<>(TreeNode.Label.prog, ""));

        for (Token token : tokens) {

            if (stack.getFirst().getKey() != currentNode.getLabel()) {
                TreeNode newNode = new TreeNode(stack.getFirst().getKey(), currentNode);
                currentNode.addChild(newNode);
                currentNode = newNode;
            } // ------------------------------------------------------------------------------------------------------- handles if it's a variable change

            if (tokenToString(token).equals(stack.getFirst().getValue())) { // ----------------------------------------- handles if it's a terminal
                currentNode.addChild(new TreeNode(stack.getFirst().getKey(), token, currentNode));
                stack.pop();
            } else { // ------------------------------------------------------------------------------------------------ handles if a rule goes with the variable
                int ruleNum = (int) parseTable.get(currentNode.getLabel()).get(tokenToString(token));
                switch (ruleNum) {
                    case 1:
                        stack.pop();
                        stack.push(new Pair<>(TreeNode.Label.prog, "}"));
                        stack.push(new Pair<>(TreeNode.Label.prog, "}"));
                        stack.push(new Pair<>(TreeNode.Label.los, ""));
                        stack.push(new Pair<>(TreeNode.Label.prog, "{"));
                        stack.push(new Pair<>(TreeNode.Label.prog, ")"));
                        stack.push(new Pair<>(TreeNode.Label.prog, "args"));
                        stack.push(new Pair<>(TreeNode.Label.prog, "String[]"));
                        stack.push(new Pair<>(TreeNode.Label.prog, "("));
                        stack.push(new Pair<>(TreeNode.Label.prog, "main"));
                        stack.push(new Pair<>(TreeNode.Label.prog, "void"));
                        stack.push(new Pair<>(TreeNode.Label.prog, "static"));
                        stack.push(new Pair<>(TreeNode.Label.prog, "public"));
                        stack.push(new Pair<>(TreeNode.Label.prog, "{"));
                        stack.push(new Pair<>(TreeNode.Label.prog, "ID"));
                        stack.push(new Pair<>(TreeNode.Label.prog, "class"));
                        // stack.push("public"); -- this gets popped right away
                        break;
                    case 2:
                        stack.pop();
                        stack.push(new Pair<>(TreeNode.Label.stat, ""));
                        stack.push(new Pair<>(TreeNode.Label.los, ""));
                        break;
                    case 3:
                        stack.pop();
                        break;
                }
            }
        }


//        for (Token token : tokens) {
//
//            if (parseTable.get(state).get(tokenToString(token)) == null)
//                throw new SyntaxException("Rule not found for token and variable combination.");
//
//            int ruleNum = (int) parseTable.get(state).get(tokenToString(token));
//            switch (ruleNum) {
//                case 1:
//                    currentNode.addChild(new TreeNode(TreeNode.Label.terminal, token, currentNode));
//                    stack.push(new Pair<>(currentNode, token));
//            }
//        }


        //Turn the List of Tokens into a ParseTree.
        return tree;
    }

}
