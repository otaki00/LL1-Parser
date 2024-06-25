package models.Scanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import constants.ProductionRules;
import models.ErrorHandler.ErrorHandler;
import models.ErrorHandler.ErrorMsg;
import models.ErrorHandler.TokenMsg;

import java.util.List;

// this class will be used to scan the source code and return the tokens
// its important class and one of the maon parts of the compiler
// what i do is scan the code char by char, then when i rread name, number, reserved word, or special symbols,
// i return the key of this token like (name = 100, numbers =200, etc)
// the input is tokenList that containt the keys of the source code
public class LexicalAnalyzer {


    // define the needed values 
    private ProductionRules productionRules;
    Map<String, Integer> reservedWordsIndexTable;
    public List<Integer> tokenList;
    public Map<Integer, String> tokenIndexTable;
    // the list contain all the needed about the token like the value, the line number, the column number, and the type of the token to use later when we need to show the error
    public List<TokenMsg> tokensData;

    // this class is for resolve the syntax errors
    private ErrorHandler errorHandler;

    private String fileUrl;

    public LexicalAnalyzer( String fileUrl) throws IOException {
        this.productionRules = new ProductionRules();
        this.fileUrl = fileUrl;
        tokenList = new ArrayList<Integer>();
        tokensData = new ArrayList<TokenMsg>();
        this.errorHandler = new ErrorHandler();

    buildReservedWordsIndexTable();

    scanCode();
    }


    // funciton to get pruduction rule for a non-terminal
    public List<List<String>> getProductionRule(String nonTerminal) {
        return productionRules.getProductionRules(nonTerminal);
    }


    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }


    // this method is used to build the reserved words index table that contain the key of the token and the value of the token
    private void buildReservedWordsIndexTable() {
        // create a map to store the reserved words and their index
        reservedWordsIndexTable = new HashMap<String, Integer>();
        this.tokenIndexTable = new HashMap<Integer, String>();

        // get the length of the non-terminal symbols and terminal symbols
        int length = productionRules.nonTerminals.size() + productionRules.terminals.size();


        // loop through the non-terminal symbols and terminal symbols from the production rules
        for (int i = 0; i < length; i++) {
            // reservedWordsIndexTable.put(productionRules.nonTerminals.get(i), i);
            if (i < productionRules.nonTerminals.size()) {
                reservedWordsIndexTable.put(productionRules.nonTerminals.get(i), i);
                tokenIndexTable.put(i, productionRules.nonTerminals.get(i));
            } else {
                reservedWordsIndexTable.put(productionRules.terminals.get(i - productionRules.nonTerminals.size()), i);
                tokenIndexTable.put(i, productionRules.terminals.get(i - productionRules.nonTerminals.size()));
            }
        }


        // add index for user defined identifiers
        reservedWordsIndexTable.put("name", 100);
        tokenIndexTable.put(100, "name");
        reservedWordsIndexTable.put("integer-value", 200);
        tokenIndexTable.put(200, "integer-value");
        reservedWordsIndexTable.put("real-value", 300);
        tokenIndexTable.put(300, "real-value");
    }


    // the main method to scan the code and return the tokens
    public void scanCode() throws IOException {
        // check if the file url is set
        if (this.fileUrl == null) {
            System.out.println("File URL is not set");
            return;
        }

        
        try (// create a scanner object
            FileInputStream fileInputStream = new FileInputStream(this.fileUrl);
            Scanner scanner = new Scanner(fileInputStream)) {
            int lineNo = 1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                for (int i = 0; i < line.length(); i++) {
                    int colNo = i + 1;
                    // check if the character is a space
                    if (line.charAt(i) == ' ') {
                        continue;
                    }

                    // check new line
                    if (line.charAt(i) == '\n') {
                        continue;
                    }

                    // check if the character is a letter
                    if (Character.isLetter(line.charAt(i))) {
                        String word = "";
                        while (i < line.length() && (Character.isLetter(line.charAt(i)) || Character.isDigit(line.charAt(i))) ) {
                            word += line.charAt(i);
                            i++;
                        }
                        i--;
                        if (productionRules.reservedWordList.contains(word)) {
                            tokenList.add(reservedWordsIndexTable.get(word));
                            tokensData.add(new TokenMsg(word, line, lineNo, colNo, "reserved"));
                            
                        } else {
                            tokenList.add(reservedWordsIndexTable.get("name"));
                            tokensData.add(new TokenMsg(word, line, lineNo, colNo, "name"));

                        }
                    } else if (Character.isDigit(line.charAt(i))) {
                        String number = "";
                        while (i < line.length() && (Character.isDigit(line.charAt(i)) || line.charAt(i) == '.')) {
                            number += line.charAt(i);
                            i++;
                        }
                        i--;
                        if (number.contains(".")) {
                            // check if contains more than one dot
                            if (number.indexOf('.') != number.lastIndexOf('.')) {
                                // error
                                // Error error = new Error("Invalid token at line " + i + " : " + number + "!");
                                // System.out.println(error.getMessage());
                                // // break;
                                // // exit program
                                // System.exit(0);
                                errorHandler.addTokensData(new ErrorMsg(lineNo, colNo, "Sytax Error: Invalid token  " + number + " ! "));
                            }
                            tokenList.add(reservedWordsIndexTable.get("real-value"));
                            tokensData.add(new TokenMsg(number, line, lineNo, colNo, "real-value"));
                        } else {
                            tokenList.add(reservedWordsIndexTable.get("integer-value"));
                            tokensData.add(new TokenMsg(number, line, lineNo, colNo, "integer-value"));
                        }
                        
                    } 
                    // check if <
                    else if (line.charAt(i) == '<') {
                        if (line.charAt(i + 1) == '=') {
                            tokenList.add(reservedWordsIndexTable.get("<="));
                            tokensData.add(new TokenMsg("<=", line, lineNo, colNo, "<="));
                            i++;
                        } else {
                            tokenList.add(reservedWordsIndexTable.get("<"));
                            tokensData.add(new TokenMsg("<", line, lineNo, colNo, "<"));
                        }
                    }
                    // check if >
                    else if (line.charAt(i) == '>') {
                        if (line.charAt(i + 1) == '=') {
                            tokenList.add(reservedWordsIndexTable.get(">="));
                            tokensData.add(new TokenMsg(">=", line, lineNo, colNo, ">="));
                            i++;
                        } else {
                            tokenList.add(reservedWordsIndexTable.get(">"));
                            tokensData.add(new TokenMsg(">", line, lineNo, colNo, ">"));
                        }
                    }
                    // check if =
                    else if (line.charAt(i) == '=') {
                        if (line.charAt(i + 1) == '=') {
                            // tokenList.add(reservedWordsIndexTable.get("=="));
                            errorHandler.addTokensData(new ErrorMsg(lineNo, colNo, "Syntax Error: Invalid token : " + " ==! "));
                            i++;
                        } else {
                            tokenList.add(reservedWordsIndexTable.get("="));
                            tokensData.add(new TokenMsg("=", line, lineNo, colNo, "="));
                        }
                    }

                    // check if !
                    else if (line.charAt(i) == '!') {
                        if (line.charAt(i + 1) == '=') {
                            tokenList.add(reservedWordsIndexTable.get("!="));
                            tokensData.add(new TokenMsg("!=", line, lineNo, colNo, "!="));
                            i++;
                        } else {
                            // error
                            errorHandler.addTokensData(new ErrorMsg(lineNo, colNo, "Syntax Error: Invalid token  : " + " !=! "));
                        }
                    }

                    // check if +
                    else if (line.charAt(i) == '+') {
                        tokenList.add(reservedWordsIndexTable.get("+"));
                        tokensData.add(new TokenMsg("+", line, lineNo, colNo, "+"));
                    }

                    // check if -
                    else if (line.charAt(i) == '-') {
                        tokenList.add(reservedWordsIndexTable.get("-"));
                        tokensData.add(new TokenMsg("-", line, lineNo, colNo, "-"));
                    }

                    // check if *
                    else if (line.charAt(i) == '*') {
                        tokenList.add(reservedWordsIndexTable.get("*"));
                        tokensData.add(new TokenMsg("*", line, lineNo, colNo, "*"));
                    }

                    // check if /
                    else if (line.charAt(i) == '/') {
                        tokenList.add(reservedWordsIndexTable.get("/"));
                        tokensData.add(new TokenMsg("/", line, lineNo, colNo, "/"));
                    }

                    // check if (
                    else if (line.charAt(i) == '(') {
                        tokenList.add(reservedWordsIndexTable.get("("));
                        tokensData.add(new TokenMsg("(", line, lineNo, colNo, "("));
                    }

                    // check if )
                    else if (line.charAt(i) == ')') {
                        tokenList.add(reservedWordsIndexTable.get(")"));
                        tokensData.add(new TokenMsg(")", line, lineNo, colNo, ")"));
                    }

                    else if (line.charAt(i) == ':') {
                        if (line.charAt(i + 1) == '=') {
                            tokenList.add(reservedWordsIndexTable.get(":="));
                            tokensData.add(new TokenMsg(":=", line, lineNo, colNo, ":="));
                            i++;
                        } else {
                            tokenList.add(reservedWordsIndexTable.get(":"));
                            tokensData.add(new TokenMsg(":", line, lineNo, colNo, ":"));
                        }
                        // tokenList.add(reservedWordsIndexTable.get(":"));
                    }

                    else if (line.charAt(i) == ';') {
                        tokenList.add(reservedWordsIndexTable.get(";"));
                        tokensData.add(new TokenMsg(";", line, lineNo, colNo, ";"));
                    }

                    else if (line.charAt(i) == ',') {
                        tokenList.add(reservedWordsIndexTable.get("comma"));
                        tokensData.add(new TokenMsg("comma", line, lineNo, colNo, "comma"));
                    }
                }
                lineNo++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
    }

    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }
}
