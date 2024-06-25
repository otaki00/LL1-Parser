package constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// this class contains the production rules for the grammar, and all needed information for the grammar
// and is used only to access the production rules, NO will be preformed on this class
public class ProductionRules {
    
    public Map<String, List<List<String>>> rules;
    // public List<String> nonTerminals;
    // define the Lists for the FIRTS & FOLLOWs for all the non-terminals
    public Map<String, List<String>> firsts;
    public Map<String, List<String>> follows;
    public List<String> reservedWordList = Arrays.asList("module", "begin", "end", "const", "var", "procedure","integer", "real", "char","readint", "readreal", "readchar", "readln", "writeint", "writereal", "writechar", "writeln", "if", "then", "else", "while", "do", "loop", "until", "exit", "call",
            "mod", "div");
    // parsing table representation, is map of map, this provide more flexibility to access the parsing table, and easy to deil with it
    public Map<String, List<Map<String, String>>> parsingTable;

    // save terminal symbols
    public List<String> terminals = Arrays.asList("module", "name", ";", "begin", "end", "const", "var", "procedure", ":",
            "integer", "real", "char", "(", ")", "comma", "=", "readint", "readreal", "readchar", "readln", "writeint",
            "writereal", "writechar", "writeln", "if", "then", "else", "while", "do", "loop", "until", "exit", "call",
            "mod", "div", "+", "-", "*", "/", ">", "<", ">=", "<=", "!=", ":=");
    public List<String> nonTerminals= Arrays.asList("module-decl","module-heading", "block", "declarations","const-decl","const-list","var-decl", "var-list", "var-item", "name-list", "more-names", "data-type", "procedure-decl","procedure-heading","stmt-list","statement","ass-stmt", "exp", "exp-prime", "term", "term-prime", "factor", "add-oper", "mul-oper","read-stmt", "write-stmt", "write-list", "more-write-value", "write-item","if-stmt", "else-part", "while-stmt","loop-stmt","exit-stmt","call-stmt","condition","relational-oper","name-value","value");
    

    public ProductionRules() {
        this.rules = new HashMap<>();
        this.firsts = new HashMap<>();
        this.follows = new HashMap<>();
        this.parsingTable = new HashMap<>();
        // call the function to initialize the grammar and add the rules to the map
        initGrammar();

        // add the follow for name-list and more-names, to avoide infinite loop
        follows.put("name-list", Arrays.asList(")", ":"));
        follows.put("more-names", Arrays.asList(")", ":", "comma"));

        follows.put("write-list", Arrays.asList(")"));
        follows.put("more-write-value", Arrays.asList(")", "comma"));

        // build the FIRST & FOLLOW for all the non-terminals
        getAllFirstsAndFollows();
        buildParsingTable();
    }

    // this function simply just to add the rules to the map thats it
    private void initGrammar() {
        // the rule is : module-decl -> module-heading declarations block name .
        rules.put("module-decl", Arrays.asList(
                Arrays.asList("module-heading", "declarations", "block", "name", ".")));
        // the rule is : module-heading -> module name ;
        rules.put("module-heading", Arrays.asList(
                Arrays.asList("module", "name", ";")));
        // the rule is : block -> begin stmt-list end
        rules.put("block", Arrays.asList(
                Arrays.asList("begin", "stmt-list", "end")));
        // the rule is : declarations -> const-decl var-decl procedure-decl
        rules.put("declarations", Arrays.asList(
                Arrays.asList("const-decl", "var-decl", "procedure-decl")));
        // the rule is : const-decl -> const const-list | lambda
        rules.put("const-decl", Arrays.asList(
                Arrays.asList("const", "const-list"),
                // For lambda (empty production)
                Collections.emptyList() 
        ));
        // the rule is : const-list -> name = value ; const-list | lambda
        rules.put("const-list", Arrays.asList(
                Arrays.asList("name", "=", "value", ";", "const-list"),
                Collections.emptyList()));
        // the rule is : var-decl -> var var-list | lambda
        rules.put("var-decl", Arrays.asList(
                Arrays.asList("var", "var-list"),
                Collections.emptyList()));
        // the rule is : var-list -> var-item ; var-list | lambda
        rules.put("var-list", Arrays.asList(
                Arrays.asList("var-item", ";", "var-list"),
                Collections.emptyList()));
        // the rule is : var-item -> name-list : data-type
        rules.put("var-item", Arrays.asList(
                Arrays.asList("name-list", ":", "data-type")));
        // the rule is : name-list -> name more-names
        rules.put("name-list", Arrays.asList(
                Arrays.asList("name", "more-names")));
        // the rule is : more-names -> , name-list | lambda
        rules.put("more-names", Arrays.asList(
                Arrays.asList("comma", "name-list"),
                Collections.emptyList()));
        // the rule is : data-type -> integer | real | char
        rules.put("data-type", Arrays.asList(
                Arrays.asList("integer"),
                Arrays.asList("real"),
                Arrays.asList("char")));
        // the rule is : procedure-decl -> procedure-heading declarations block name ;
        rules.put("procedure-decl", Arrays.asList(
                Arrays.asList("procedure-heading", "declarations", "block", "name", ";", "procedure-decl"),
                Collections.emptyList()));
        // the rule is : procedure-heading -> procedure name ;
        rules.put("procedure-heading", Arrays.asList(
                Arrays.asList("procedure", "name", ";")));
        // the rule is : stmt-list -> statement ; stmt-list | lambda
        rules.put("stmt-list", Arrays.asList(
                Arrays.asList("statement", ";", "stmt-list"),
                Collections.emptyList()));

        // the rule is : statement ->  ass-stmt | read-stmt | write-stmt | if-stmt | while-stmt | loop-stmt | exit-stmt | call-stmt | block | lambda
        rules.put("statement", Arrays.asList(
                Arrays.asList("ass-stmt"),
                Arrays.asList("read-stmt"),
                Arrays.asList("write-stmt"),
                Arrays.asList("if-stmt"),
                Arrays.asList("while-stmt"),
                Arrays.asList("loop-stmt"),
                Arrays.asList("exit-stmt"),
                Arrays.asList("call-stmt"),
                Arrays.asList("block"),
                Collections.emptyList()));
        // the rule is : ass-stmt -> name := exp
        rules.put("ass-stmt", Arrays.asList(
            Arrays.asList("name", ":=", "exp")
        ));
        // the rule is : exp -> term exp-prime
        rules.put("exp", Arrays.asList(
            Arrays.asList("term", "exp-prime")
        ));
        // the rule is : exp-prime -> add-oper term exp-prime
        rules.put("exp-prime", Arrays.asList(
            Arrays.asList("add-oper", "term", "exp-prime"),
            // For lambda (empty production)
            Collections.emptyList() 
        ));
        // the rule is : term -> factor term-prime
        rules.put("term", Arrays.asList(
            Arrays.asList("factor", "term-prime")
        ));
        // the rule is : term-prime -> mul-factor factor term-prime
        rules.put("term-prime", Arrays.asList(
            Arrays.asList("mul-oper", "factor", "term-prime"),
            Collections.emptyList()
        ));
        // the rule is : factor -> “(“ exp “)” | name-value
        rules.put("factor", Arrays.asList(
            Arrays.asList("(", "exp", ")"),
            Arrays.asList("name-value")
        ));
        // the rule is : add-oper -> + | -
        rules.put("add-oper", Arrays.asList(
            Arrays.asList("+"),
            Arrays.asList("-")
        ));
        // the rule : mul-oper -> * | / | mod | div
        rules.put("mul-oper", Arrays.asList(
            Arrays.asList("*"),
            Arrays.asList("/"),
            Arrays.asList("mod"),
            Arrays.asList("div")
        ));
        // the rule : read-stmt -> readint ( name-list ) | readreal ( name-list ) | readchar ( name-list ) | readln
        rules.put("read-stmt", Arrays.asList(
            Arrays.asList("readint", "(", "name-list", ")"),
            Arrays.asList("readreal", "(", "name-list", ")"),
            Arrays.asList("readchar", "(", "name-list", ")"),
            Arrays.asList("readln")
        ));
        // the rule : write-stmt -> writeint ( write-list ) | writereal ( write-list ) | writechar ( write-list ) | writeln
        rules.put("write-stmt", Arrays.asList(
            Arrays.asList("writeint", "(", "write-list", ")"),
            Arrays.asList("writereal", "(", "write-list", ")"),
            Arrays.asList("writechar", "(", "write-list", ")"),
            Arrays.asList("writeln")
        ));
        // the rule : write-list -> write-item more-write-value
        rules.put("write-list", Arrays.asList(
            Arrays.asList("write-item", "more-write-value")
        ));
        // the rule : more-write-value -> , write-list | lambda
        rules.put("more-write-value", Arrays.asList(
            Arrays.asList("comma", "write-list"),
            Collections.emptyList()
        ));
        // the rule : write-item -> name | value
        rules.put("write-item", Arrays.asList(
            Arrays.asList("name"),
            Arrays.asList("value")
        ));
        // the rule : if-stmt -> if condition then stmt-list else-part end
        rules.put("if-stmt", Arrays.asList(
            Arrays.asList("if", "condition", "then", "stmt-list", "else-part", "end")
        ));
        // the rule : else-part -> else stmt-list | lambda
        rules.put("else-part", Arrays.asList(
            Arrays.asList("else", "stmt-list"),
            Collections.emptyList()
        ));
        // the rule : while-stmt -> while condition do stmt-list end
        rules.put("while-stmt", Arrays.asList(
            Arrays.asList("while", "condition", "do", "stmt-list", "end")
        ));
        // the rule : loop-stmt -> loop stmt-list until condition
        rules.put("loop-stmt", Arrays.asList(
            Arrays.asList("loop", "stmt-list", "until", "condition")
        ));
        // the rule : exit-stmt -> exit
        rules.put("exit-stmt", Arrays.asList(
            Arrays.asList("exit")
        ));
        // the rule : call-stmt -> call name
        rules.put("call-stmt", Arrays.asList(
            Arrays.asList("call", "name")  // Assuming name is the procedure name
        ));
        // the rule : condition -> name-value relational-oper name-value
        rules.put("condition", Arrays.asList(
            Arrays.asList("name-value", "relational-oper", "name-value")
        ));
        // the rule : relational-oper -> = | != | < | <= | > | >=
        rules.put("relational-oper", Arrays.asList(
            Arrays.asList("="),
            Arrays.asList("!="),
            Arrays.asList("<"),
            Arrays.asList("<="),
            Arrays.asList(">"),
            Arrays.asList(">=")
        ));
        // the rule : name-value -> name | value
        rules.put("name-value", Arrays.asList(
            Arrays.asList("name"),
            Arrays.asList("value")
        ));
        // the rule : value -> integer-value | real-value
        rules.put("value", Arrays.asList(
            Arrays.asList("integer-value"),
            Arrays.asList("real-value")
        ));

        // 

        // define the non-terminals
        // this.nonTerminals = new ArrayList<>(rules.keySet());
        // System.out.println(rules);
    }


    // this function check if the given token is reserved word or not
    public String isReservedWord(String token) {
        // check if the word in non-terminals
        if (nonTerminals.contains(token)) {
            return token;
        }else if (terminals.contains(token)) {
            return token;
        }
        return null;
    }

    // this function return the production rule for a given non-terminal
    public List<List<String>> getProductionRules(String nonTerminal) {
        return rules.get(nonTerminal);
    }

    // this function return the non-terminals
    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    // this method will used to check if the given symbol is terminal or not, in FIRST & FOLLOW 
    private boolean checkTerminal(String symbol) {
        return !nonTerminals.contains(symbol);
    }

    // method used to get the FIRST for a given non-terminal symbol, it used DP technique to avoid unnecessary calls, by add another parameter to the function which is the memoization map
    private List<String> First(String nonTerminalSymbol, Map<String, List<String>> firstsMemo) {
        // check if the firstsMemo contains the non-terminal symbol, it just a technique to aviod unnecessary calls
        if (firstsMemo.containsKey(nonTerminalSymbol)) {
            return firstsMemo.get(nonTerminalSymbol);
        }
        // create the list to store the first for symbol
        List<String> firstForSymbol = new ArrayList<String>();
        // get the production rules for the given non-terminal symbol
        List<List<String>> productionRules = getProductionRules(nonTerminalSymbol);

        // first we need to loop the production rules for the given non-terminal symbol
        for (List<String> productionRule : productionRules) {
            // if the production rule is empty, then add lambda to the firsts, because FIRST can contain lambda
            if (productionRule.isEmpty()) {
                // firstForSymbol.add("lambda");
                if(firstForSymbol.contains("lambda")){
                    continue;
                }
                firstForSymbol.add("lambda");
                continue;
            }

            // now we need to loop for the Ith production rule of the non-terminal symbol to get the first
            for (int i = 0; i < productionRule.size(); i++) {
                // store the symbol we got from the production rule
                String symbol = productionRule.get(i);
                // check if that symbol is terminal or not, 
                if (checkTerminal(symbol)) {
                    // if the symbol is terminal, then add it to the firstForSymbol list
                    firstForSymbol.add(symbol);
                    // then break, we don't need to check the next symbols
                    break;
                // if the symbol is non-terminal, then we need to get the first for that symbol
                } else {
                    // get the first for that non-terminal symbol, by calling the First function recursively
                    List<String> firstOfSubSymbol = First(symbol, firstsMemo);
                    // after getting the first for the sub-symbol, we need to add it to the firstForSymbol list
                    firstForSymbol.addAll(firstOfSubSymbol);
                    // if the first of the sub-symbol contains lambda, then we need to continue to the next symbol
                    if (!firstOfSubSymbol.contains("lambda")) {
                        break;
                    }
                    // if the first of the sub-symbol contains lambda, and it is the last symbol in the production rule
                    if (i == productionRule.size() - 1) {
                        firstForSymbol.add("lambda");
                    }
                }
            }
        }
        // remove the lambda from the firstForSymbol list, but keep the last one (because the Algorithm doesn't do dot product between the Lists of FIRSTs, so it add it i the final result, its like do sum of the lists of FIRSTs not dot product)
        firstForSymbol = removeLambdaButLastOne(firstForSymbol);
        // add the firsts for the non-terminal symbol to the firstsMemo
        firstsMemo.put(nonTerminalSymbol, firstForSymbol);
        // return the firsts for the non-terminal symbol
        return firstForSymbol;
    }

    // method to remove lambda from the list, but keep the last one
    // not used 
    private List<String> removeLambdaButLastOne(List<String> list) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("lambda") && i != list.size() - 1) {
                continue;
            }
            result.add(list.get(i));
        }
        return result;
    }

    private void getAllFirstsAndFollows(){
        // to get all firsts, we need to start from the last non-terminal (because the stablitly of the algorithm is from the last non-terminal to the first non-terminal)
        for (int i = nonTerminals.size() - 1; i >= 0; i--) {
            First(nonTerminals.get(i), firsts);
        }

        // to get all follows, we need to start from the first non-terminal (because the stablitly of the algorithm is from the first non-terminal to the last non-terminal)
        for (int i = 0; i < nonTerminals.size(); i++) {
            // System.out.println(i);
            Follow(nonTerminals.get(i), follows);
        }
    }

    private List<String> Follow(String nonTerminalSymbol, Map<String, List<String>> followMemo) {
        // System.out.println(nonTerminalSymbol);
        if (followMemo.containsKey(nonTerminalSymbol)){
            return followMemo.get(nonTerminalSymbol);
        }

        List<String> followForSymbol = new ArrayList<>();
        
        Map<Integer, Integer> productionRulesWithSymbol = indexOFAllProductionRulesThatHadThisNonTerminal(nonTerminalSymbol);
        // System.out.println(Arrays.toString(productionRulesWithSymbol));

        // if the non-terminal symbol is the start symbol, then add . to the follow
        if (nonTerminalSymbol.equals("module-decl")) {
            followForSymbol.add(".");
            return followForSymbol;
        }

        for(Map.Entry<Integer, Integer> entry : productionRulesWithSymbol.entrySet()){
            List<List<String>> productionRule = getProductionRules(nonTerminals.get(entry.getKey()));
            
            int indexOfSymbol = entry.getValue();

            List<String> production = productionRule.get(indexOfSymbol);
            for (int i = 0; i < production.size(); i++) {
                
                String symbol = production.get(i);
                if (symbol.equals(nonTerminalSymbol)) {
                    if (i == production.size() - 1) {
                        // check the non-terminal production rule is the same for the non-ternmianl we try to fin FOLLOW for
                        if (nonTerminals.get(entry.getKey()).equals(nonTerminalSymbol)){
                            break;
                        }
                        List<String> followOfParent = Follow(nonTerminals.get(entry.getKey()), followMemo);
                        // remove the symbol that are already in the follow
                        followForSymbol.addAll(listOfNonUniqueValues(followOfParent, followForSymbol));

                    } else {
                        for (int j = i + 1; j < production.size(); j++) {
                            String nextSymbol = production.get(j);
                            if (checkTerminal(nextSymbol)) {
                                // followForSymbol.add(nextSymbol);
                                // check if the symbol exists in the followForSymbol list
                                if (!followForSymbol.contains(nextSymbol)) {
                                    followForSymbol.add(nextSymbol);
                                }

                                break;
                            } else {
                                List<String> firstOfNextSymbol = First(nextSymbol, firsts);
                                // followForSymbol.addAll(firstOfNextSymbol);
                                followForSymbol.addAll(listOfNonUniqueValues(firstOfNextSymbol, followForSymbol));
                                if (!firstOfNextSymbol.contains("lambda")) {
                                    break;
                                }
                                if (j == production.size() - 1) {
                                    List<String> followOfParent = Follow(nonTerminals.get(entry.getKey()), followMemo);
                                    // followForSymbol.addAll(followOfParent);
                                    followForSymbol.addAll(listOfNonUniqueValues(followOfParent, followForSymbol));
                                }
                            }
                        }
                    }
                }
            }
        }

        // followForSymbol = removeLambdaButLastOne(followForSymbol);
        followForSymbol = removeLambda(followForSymbol);
        followMemo.put(nonTerminalSymbol, followForSymbol);
        return followForSymbol;
        // return null;
    }

    // method to remove lambda from the list
    private List<String> removeLambda(List<String> list) {
        List<String> result = new ArrayList<>();
        for (String string : list) {
            if (string.equals("lambda")) {
                continue;
            }
            result.add(string);
        }
        return result;
    }

    private List<String> listOfNonUniqueValues(List<String> list1, List<String> list2){
        List<String> result = new ArrayList<>();
        for (String string : list1) {
            if (!list2.contains(string)) {
                result.add(string);
            }
        }
        return result;
    }

    private Map<Integer, Integer> indexOFAllProductionRulesThatHadThisNonTerminal(String NonTerminalSymbol){
        // we need to loop through all the production rules to get the index of the production rules that had the given non-terminal symbol
        Map<Integer, Integer> indexes = new HashMap<>();
        for (int i = 0; i < nonTerminals.size(); i++) {
            List<List<String>> productionRules = getProductionRules(nonTerminals.get(i));
            // System.out.println(productionRules);
            for (int j = 0; j < productionRules.size(); j++) {
                // System.out.println(productionRules.get(i));
                if (productionRules.get(j).contains(NonTerminalSymbol)) {
                    // System.out.println(productionRules.get(i));
                    indexes.put(i, j);
                }
            }
        }
        
        return indexes;
    }

    public void buildParsingTable(){
        // loop through all the non-terminals
        for (String nontTermnialString: nonTerminals){
            // System.out.println(nontTermnialString+ "------------------");
            List<List<String>> productionRules = getProductionRules(nontTermnialString);
            // get the first for the non-terminal
            List<String> firstsOfProduction = firsts.get(nontTermnialString);
            // get the follows for the non-terminal
            List<String> followsOfProduction = follows.get(nontTermnialString);
            // List<String> followsOfProduction = follows.get(nontTermnialString);
            List<Map<String, String>> temp = parsingTable.getOrDefault(nontTermnialString, new ArrayList<>());

            // CASE 1: which is when the non-terminal has one production rule
            if (productionRules.size() == 1 ){
                // add the production rule to the parsing table
                // List<Map<String, String>> temp = new ArrayList<>();
                for (String first: firstsOfProduction){
                    if ("lambda".equals(first)){
                        continue;
                    }
                    Map<String, String> tempMap = new HashMap<>();
                    tempMap.put(first, productionRules.get(0).toString());
                    temp.add(tempMap);
                }
                // System.out.println(temp);
                parsingTable.put(nontTermnialString, temp);
            }else{
                // CASE 2: which is when the non-terminal has more than one production rule
                // loop through all the production rules
                if (productionRules.size()  == 2 && firstsOfProduction.size() > 2){
                    for(String first: firstsOfProduction){
                        if ("lambda".equals(first)) {
                            continue;
                        }
                        Map<String, String> tempMap = new HashMap<>();
                        tempMap.put(first, productionRules.get(0).toString());
                        temp.add(tempMap);
                    }
                    if (productionRules.get(1).isEmpty()){
                        for (String follow: followsOfProduction){
                            Map<String, String> tempMap = new HashMap<>();
                            tempMap.put(follow, productionRules.get(1).toString());
                            temp.add(tempMap);
                        }
                    }
                }else if(productionRules.size() < firstsOfProduction.size() ) {
                    for (int i = 0; i < productionRules.size(); i++) {
                        // check if the production rule is lambda, then we need to add the follows to
                        // the parsing table
                        if (productionRules.get(i).isEmpty()) {
                            // String nextNonTerm = getNextNonTerminalForCurrOne(nontTermnialString);
                            // List<String> rulesForNextNonTerm  = rules.get(nextNonTerm).get(0);
                            // List<Map<String, String>> temp = new ArrayList<>();
                            for (String follow : followsOfProduction) {
                                Map<String, String> tempMap = new HashMap<>();
                                tempMap.put(follow, productionRules.get(i).toString());
                                temp.add(tempMap);
                                // temp.add(tempMap);
                            }
                            // System.out.println(temp);

                            // parsingTable.put(nontTermnialString, temp);
                        } else {

                            List<String> firstsOfCurrRule = firsts.get(productionRules.get(i).get(0));
                            for (String first: firstsOfCurrRule){
                                if ("lambda".equals(first)){
                                    continue;
                                }
                                Map<String, String> tempMap = new HashMap<>();
                                tempMap.put(first, productionRules.get(i).toString());
                                temp.add(tempMap);
                            }
                            // System.out.println(temp);
                            // parsingTable.put(nontTermnialString, temp);
                        }
                    }
                }
                else {
                    for (int i = 0; i < productionRules.size(); i++) {
                        // check if the production rule is lambda, then we need to add the follows to
                        // the parsing table
                        if (productionRules.get(i).isEmpty()) {
                            // List<Map<String, String>> temp = new ArrayList<>();
                            for (String follow : followsOfProduction) {
                                Map<String, String> tempMap = new HashMap<>();
                                tempMap.put(follow, productionRules.get(i).toString());
                                temp.add(tempMap);
                                // temp.add(tempMap);
                            }
                            // System.out.println(temp);

                            // parsingTable.put(nontTermnialString, temp);
                        } else {

                            Map<String, String> tempMap = new HashMap<>();
                            tempMap.put(firstsOfProduction.get(i), productionRules.get(i).toString());
                            temp.add(tempMap);
                            // System.out.println(temp);
                            // parsingTable.put(nontTermnialString, temp);
                        }
                    }
                }
            }

            parsingTable.put(nontTermnialString, temp);
        }
        
        // System.out.println(parsingTable);
        // // change the rules for more-name 
        parsingTable.get("more-names").remove(3);

        parsingTable.get("more-write-value").remove(2);

        parsingTable.get("write-item").remove(1);
        parsingTable.get("write-item").remove(1);

        parsingTable.get("write-item").add(Map.of("integer-value", "[integer-value]"));
        parsingTable.get("write-item").add(Map.of("real-value", "[real-value]"));

        parsingTable.get("name-value").remove(1);
        parsingTable.get("name-value").remove(1);

        parsingTable.get("name-value").add(Map.of("integer-value", "[integer-value]"));
        parsingTable.get("name-value").add(Map.of("real-value", "[real-value]"));

        parsingTable.get("factor").remove(1);
        parsingTable.get("factor").remove(1);
        parsingTable.get("factor").remove(1);

        parsingTable.get("factor").add(Map.of("name", "[name]"));
        parsingTable.get("factor").add(Map.of("integer-value", "[integer-value]"));
        parsingTable.get("factor").add(Map.of("real-value", "[real-value]"));


        // add rule for declarations when begin ; declarations -> 
        parsingTable.get("declarations").add(1, Map.of("begin", "[const-decl, var-decl, procedure-decl]"));
    }


    // function helper for parser, that accept two strings, 
    // so what to do is to check if they poth equals and terminal, then return null
    // else, from the parsing table, get the production rule for the given non-terminal and terminal
    public String getTheProductionRuleForSpecificTokens(String token1, String token2){
        // check if the two tokens are terminal
        if (checkTerminal(token1) && checkTerminal(token2)) {
            return null;
        }
        // get the production rule for the given non-terminal and terminal
        List<Map<String, String>> productionRules = parsingTable.get(token1);
        for (Map<String, String> pr: productionRules){
            if(pr.containsKey(token2)){
                return pr.get(token2);
            } 
        }
        return null; 
    }

    public Boolean CheckIsTerminal(String token) {
        return terminals.contains(token);
    }

    // check non-terminal
    public Boolean CheckIsNonTerminal(String token) {
        return nonTerminals.contains(token);
    }

    
    private String getNextNonTerminalForCurrOne(String currNonTerminal) {
        String nextNonTerminal = "";
        for(String nonTerminal: nonTerminals) {
            if (nonTerminal.equals(currNonTerminal)) {
                nextNonTerminal =  nonTerminals.get(nonTerminals.indexOf(nonTerminal) + 1);
            }
        }
        return nextNonTerminal;
    }
}