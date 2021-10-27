import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class LexicalAnalyser {

    public static List<Token> analyse(String sourceCode) throws LexicalException {
        // Turn the input String into a list of Tokens!
        String[] splitList = sourceCode.split(" ");
        List<String> tokenCase = Arrays.asList(new String[]{
                "\"", "'", "(", ")", "{", "}",
                ";", "+", "-", "*", "/", "%"
        });
        List<String> furtherSplitList = new ArrayList<String>();
        List<Token> tokenList = new ArrayList<Token>();

        for (String word : splitList) {
            String s = "";
            for (int i = 0; i < word.length(); i++) {
                // System.out.println(word.charAt(i));
                String c = Character.toString(word.charAt(i));
                if (tokenCase.contains(c)) {
                    furtherSplitList.add(s);
                    furtherSplitList.add(c);
                    s = "";
                } else {
                    s += c;
                }
            }
            furtherSplitList.add(s);
        }
        // System.out.println(furtherSplitList);
        for (int i = 0; i < furtherSplitList.size(); i++) {
            String s = furtherSplitList.get(i);
            if (s.length() > 0) {
                try {
//                     System.out.println(s);
                    if (i > 0 && furtherSplitList.get(i - 1).matches("\"")) {
                        tokenList.add(tokenTypeStringLit(s).get());
                    } else if (i > 0 && furtherSplitList.get(i - 1).matches("'")) {
                        tokenList.add(tokenTypeCharLit(s).get());
                    } else if (i < furtherSplitList.size() - 1 && furtherSplitList.get(i).matches(".[=]$")) {
                        // System.out.println(furtherSplitList.get(i));
                        if (furtherSplitList.get(i).matches("^[>].*")) {
                            tokenList.add(tokenTypeGE(s).get());
                        } else if (furtherSplitList.get(i).matches("^[<].*")) {
                            tokenList.add(tokenTypeLE(s).get());
                        } else if (furtherSplitList.get(i).matches("^[!].*")) {
                            tokenList.add(tokenTypeNE(s).get());
                        } else if (furtherSplitList.get(i).matches("^[=].*")) {
                            tokenList.add(tokenTypeEqual(s).get());
                        } else { tokenList.add(tokenTypeEqual(s).get()); }
                    } else {tokenList.add(tokenFromString(s).get()); }
                } catch (NoSuchElementException e) {
                    // tokenList.add(Optional.empty());
                    // System.out.print("Token not found: " + e + "\n");
                    throw new LexicalException("Token not found: " + e);
                } catch (Exception e) {
                    System.out.print(e);
                }
            }
        }
        return tokenList;
    }

    private static Optional<Token> tokenFromString(String t) {
        Optional<Token.TokenType> type = tokenTypeOf(t);
        if (type.isPresent())
            return Optional.of(new Token(type.get(), t));
        return Optional.empty();
    }

    private static Optional<Token> tokenTypeStringLit(String t) {
        Optional<Token.TokenType> type = Optional.of(Token.TokenType.STRINGLIT);
        if (type.isPresent())
            return Optional.of(new Token(type.get(), t));
        return Optional.empty();
    }

    private static Optional<Token> tokenTypeCharLit(String t) {
        Optional<Token.TokenType> type = Optional.of(Token.TokenType.CHARLIT);
        if (type.isPresent())
            return Optional.of(new Token(type.get(), t));
        return Optional.empty();
    }

    private static Optional<Token> tokenTypeGE(String t) {
        Optional<Token.TokenType> type = Optional.of(Token.TokenType.GE);
        if (type.isPresent())
            return Optional.of(new Token(type.get(), t));
        return Optional.empty();
    }

    private static Optional<Token> tokenTypeLE(String t) {
        Optional<Token.TokenType> type = Optional.of(Token.TokenType.LE);
        if (type.isPresent())
            return Optional.of(new Token(type.get(), t));
        return Optional.empty();
    }

    private static Optional<Token> tokenTypeEqual(String t) {
        Optional<Token.TokenType> type = Optional.of(Token.TokenType.EQUAL);
        if (type.isPresent())
            return Optional.of(new Token(type.get(), t));
        return Optional.empty();
    }

    private static Optional<Token> tokenTypeNE(String t) {
        Optional<Token.TokenType> type = Optional.of(Token.TokenType.NEQUAL);
        if (type.isPresent())
            return Optional.of(new Token(type.get(), t));
        return Optional.empty();
    }

    private static Optional<Token.TokenType> tokenTypeOf(String t) {
        switch (t) {
            case "public":
                return Optional.of(Token.TokenType.PUBLIC);
            case "class":
                return Optional.of(Token.TokenType.CLASS);
            case "static":
                return Optional.of(Token.TokenType.STATIC);
            case "main":
                return Optional.of(Token.TokenType.MAIN);
            case "{":
                return Optional.of(Token.TokenType.LBRACE);
            case "void":
                return Optional.of(Token.TokenType.VOID);
            case "(":
                return Optional.of(Token.TokenType.LPAREN);
            case "String[]":
                return Optional.of(Token.TokenType.STRINGARR);
            case "args":
                return Optional.of(Token.TokenType.ARGS);
            case ")":
                return Optional.of(Token.TokenType.RPAREN);
            case "int":
            case "char":
            case "boolean":
                return Optional.of(Token.TokenType.TYPE);
            case "=":
                return Optional.of(Token.TokenType.ASSIGN);
            case ";":
                return Optional.of(Token.TokenType.SEMICOLON);
            case "if":
                return Optional.of(Token.TokenType.IF);
            case "for":
                return Optional.of(Token.TokenType.FOR);
            case "while":
                return Optional.of(Token.TokenType.WHILE);
            case "==":
                return Optional.of(Token.TokenType.EQUAL);
            case "+":
                return Optional.of(Token.TokenType.PLUS);
            case "-":
                return Optional.of(Token.TokenType.MINUS);
            case "*":
                return Optional.of(Token.TokenType.TIMES);
            case "/":
                return Optional.of(Token.TokenType.DIVIDE);
            case "%":
                return Optional.of(Token.TokenType.MOD);
            case "}":
                return Optional.of(Token.TokenType.RBRACE);
            case "else":
                return Optional.of(Token.TokenType.ELSE);
            case "System.out.println":
                return Optional.of(Token.TokenType.PRINT);
            case "||":
                return Optional.of(Token.TokenType.OR);
            case "&&":
                return Optional.of(Token.TokenType.AND);
            case "true":
                return Optional.of(Token.TokenType.TRUE);
            case "false":
                return Optional.of(Token.TokenType.FALSE);
            case "<":
                return Optional.of(Token.TokenType.LT);
            case ">":
                return Optional.of(Token.TokenType.GT);
            case "\"":
                return Optional.of(Token.TokenType.DQUOTE);
            case "'":
                return Optional.of(Token.TokenType.SQUOTE);
        }

        if (t.matches("\\d+"))
            return Optional.of(Token.TokenType.NUM);
        if (Character.isAlphabetic(t.charAt(0)) && t.matches("[\\d|\\w]+")) {
            return Optional.of(Token.TokenType.ID);
        }
        return Optional.empty();
    }

}
