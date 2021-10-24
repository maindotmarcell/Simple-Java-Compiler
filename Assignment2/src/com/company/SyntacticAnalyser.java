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
                return "int";
            case PRINT:
                return "System.out.println";
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


    public static TreeNode createNode(TreeNode parent, TreeNode.Label childLabel) {
        TreeNode newNode = new TreeNode(childLabel, parent);
        parent.addChild(newNode);
        return newNode;
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
//            put("", 2);
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

        Deque<Pair<TreeNode, String>> stack = new ArrayDeque<>();
        ParseTree tree = new ParseTree();
        tree.setRoot(new TreeNode(TreeNode.Label.prog, null));
        TreeNode currentNode = tree.getRoot();

        stack.push(new Pair<>(currentNode, ""));

        for (Token token : tokens) {

            if (stack.getFirst().getValue().equals("")) { // ------- if the we are at a variable we start checking for rules
                boolean terminalAvailable = false;
                while (!terminalAvailable) { // ---- makes sure that we don't go onto the next token until we process it as a terminal

                    if (!stack.getFirst().equals(currentNode)) { // --------------------------- handles if there's a variable change aka current != top of stack
                        currentNode = stack.getFirst().getKey();
                    }

                    int ruleNum = (int) parseTable.get(currentNode.getLabel()).get(tokenToString(token)); // ----------- checks if a rule goes with the variable
                    switch (ruleNum) {
                        case 1:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "}"));
                            stack.push(new Pair<>(currentNode, "}"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.los), ""));
                            stack.push(new Pair<>(currentNode, "{"));
                            stack.push(new Pair<>(currentNode, ")"));
                            stack.push(new Pair<>(currentNode, "args"));
                            stack.push(new Pair<>(currentNode, "String[]"));
                            stack.push(new Pair<>(currentNode, "("));
                            stack.push(new Pair<>(currentNode, "main"));
                            stack.push(new Pair<>(currentNode, "void"));
                            stack.push(new Pair<>(currentNode, "static"));
                            stack.push(new Pair<>(currentNode, "public"));
                            stack.push(new Pair<>(currentNode, "{"));
                            stack.push(new Pair<>(currentNode, "ID"));
                            stack.push(new Pair<>(currentNode, "class"));
                            // stack.push("public"); -- this gets popped right away
                            break;
                        case 2:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.los), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.stat), ""));
                            break;
                        case 3:
                            stack.pop();
                            break;
                        case 4:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.whilestat), ""));
                            break;
                        case 5:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.forstat), ""));
                            break;
                        case 6:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.ifstat), ""));
                            break;
                        case 7:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, ";"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.assign), ""));
                            break;
                        case 8:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, ";"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.decl), ""));
                            break;
                        case 9:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, ";"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.print), ""));
                            break;
                        case 10:
                            stack.pop();
                            //stack.push(new Pair<TreeNode.Label.epsilon>, "");
                            break;
                        case 11:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "}"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.los), ""));
                            stack.push(new Pair<>(currentNode, "{"));
                            stack.push(new Pair<>(currentNode, ")"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.boolexpr), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.relexpr), ""));
                            stack.push(new Pair<>(currentNode, "("));
                            stack.push(new Pair<>(currentNode, "while"));
                            break;
                        case 12:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "}"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.los), ""));
                            stack.push(new Pair<>(currentNode, "{"));
                            stack.push(new Pair<>(currentNode, ")"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.forarith), ""));
                            stack.push(new Pair<>(currentNode, ";"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.boolexpr), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.relexpr), ""));
                            stack.push(new Pair<>(currentNode, ";"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.forstart), ""));
                            stack.push(new Pair<>(currentNode, "("));
                            stack.push(new Pair<>(currentNode, "if"));
                            break;
                        case 13:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.decl), ""));
                            break;
                        case 14:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.assign), ""));
                            break;
                        case 15:
                            stack.pop();
                            //stack.push(new Pair<TreeNode.Label.epsilon>, "");
                            break;
                        case 16:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.arithexpr), ""));
                            break;
                        case 17:
                            stack.pop();
                            //stack.push(new Pair<TreeNode.Label.epsilon>, "");
                            break;
                        case 18:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.elseifstat), ""));
                            stack.push(new Pair<>(currentNode, "}"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.los), ""));
                            stack.push(new Pair<>(currentNode, "{"));
                            stack.push(new Pair<>(currentNode, ")"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.boolexpr), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.relexpr), ""));
                            stack.push(new Pair<>(currentNode, "("));
                            stack.push(new Pair<>(currentNode, "if"));
                            break;
                        case 19:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.elseifstat), ""));
                            stack.push(new Pair<>(currentNode, "}"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.los), ""));
                            stack.push(new Pair<>(currentNode, "{"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.elseorelseif), ""));
                            break;
                        case 20:
                            stack.pop();
                            //stack.push(new Pair<>(currentNode, ""));
                            break;
                        case 21:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.possif), ""));
                            stack.push(new Pair<>(currentNode, "else"));
                            break;
                        case 22:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, ")"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.boolexpr), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.relexpr), ""));
                            stack.push(new Pair<>(currentNode, "("));
                            stack.push(new Pair<>(currentNode, "if"));
                            break;
                        case 23:
                            stack.pop();
                            //stack.push(new Pair<>(currentNode, ""));
                            break;
                        case 24:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.expr), ""));
                            stack.push(new Pair<>(currentNode, "="));
                            stack.push(new Pair<>(currentNode, "ID"));
                            break;
                        case 25:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.possassign), ""));
                            stack.push(new Pair<>(currentNode, "ID"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.type), ""));
                            break;
                        case 26:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.expr), ""));
                            stack.push(new Pair<>(currentNode, "="));
                            break;
                        case 27:
                            stack.pop();
                            //stack.push(new Pair<>(currentNode, ""));
                            break;
                        case 28:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "System.out.println("));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.printexpr), ""));
                            stack.push(new Pair<>(currentNode, ")"));
                            break;
                        case 29:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "int"));
                            break;
                        case 30:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "char"));
                            break;
                        case 31:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "boolean"));
                            break;
                        case 32:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.boolexpr), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.relexpr), ""));
                            break;
                        case 33:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.charexpr), ""));
                            break;
                        case 34:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "'"));
                            stack.push(new Pair<>(currentNode, "Char"));
                            stack.push(new Pair<>(currentNode, "'"));
                            break;
                        case 35:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.boolexpr), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.relexpr), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.boolop), ""));
                            break;
                        case 36:
                            stack.pop();
                            //stack.push(new Pair<>(currentNode, ""));
                            break;
                        case 37:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.booleq), ""));
                            break;
                        case 38:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.boollog), ""));
                            break;
                        case 39:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "=="));
                            break;
                        case 40:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "!="));
                            break;
                        case 41:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "&&"));
                            break;
                        case 42:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "||"));
                            break;
                        case 43:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.relexprprime), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.arithexpr), ""));
                            break;
                        case 44:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "true"));
                            break;
                        case 45:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "false"));
                            break;
                        case 46:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.arithexpr), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.relop), ""));
                            break;
                        case 47:
                            stack.pop();
                            //stack.push(new Pair<>(currentNode, ""));
                            break;
                        case 48:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "<"));
                            break;
                        case 49:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "<="));
                            break;
                        case 50:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, ">"));
                            break;
                        case 51:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, ">="));
                            break;
                        case 52:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.arithexprprime), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.term), ""));
                            break;
                        case 53:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.arithexprprime), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.term), ""));
                            stack.push(new Pair<>(currentNode, "+"));
                            break;
                        case 54:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.arithexprprime), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.term), ""));
                            stack.push(new Pair<>(currentNode, "-"));
                            break;
                        case 55:
                            stack.pop();
                            //stack.push(new Pair<>(currentNode, ""));
                            break;
                        case 56:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.termprime), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.factor), ""));
                            break;
                        case 57:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.termprime), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.factor), ""));
                            stack.push(new Pair<>(currentNode, "*"));
                            break;
                        case 58:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.termprime), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.factor), ""));
                            stack.push(new Pair<>(currentNode, "/"));
                            break;
                        case 59:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.termprime), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.factor), ""));
                            stack.push(new Pair<>(currentNode, "%"));
                            break;
                        case 60:
                            stack.pop();
                            //stack.push(new Pair<>(currentNode, ""));
                            break;
                        case 61:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, ")"));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.arithexpr), ""));
                            stack.push(new Pair<>(currentNode, "("));
                            break;
                        case 62:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "ID"));
                            break;
                        case 63:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "num"));
                            break;
                        case 64:
                            stack.pop();
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.boolexpr), ""));
                            stack.push(new Pair<>(createNode(currentNode, TreeNode.Label.relexpr), ""));
                            break;
                        case 65:
                            stack.pop();
                            stack.push(new Pair<>(currentNode, "\""));
                            stack.push(new Pair<>(currentNode, "StringLit"));
                            stack.push(new Pair<>(currentNode, "\""));
                            break;
                    }
                    if (!stack.getFirst().getValue().equals(""))
                        terminalAvailable = true;
                }
            }
            if (tokenToString(token).equals(stack.getFirst().getValue())) { // ----------------------------------------- handles if it's a terminal
                currentNode.addChild(new TreeNode(stack.getFirst().getKey().getLabel(), token, currentNode));
                stack.pop();
            }

        }





        //Turn the List of Tokens into a ParseTree.
        return tree;
    }

}
