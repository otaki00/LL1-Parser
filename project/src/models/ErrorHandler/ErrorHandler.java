package models.ErrorHandler;

import java.util.ArrayList;
import java.util.List;

import constants.ProductionRules;
import models.Stack.StackOperations;

// this class will be used to collect data about the source code (input code), and handle the errors with clear messages
public class ErrorHandler {

    private List<ErrorMsg> tokensErrors;
    private ProductionRules productionRules;

    
    public ErrorHandler() {
        this.tokensErrors = new ArrayList<>();
        this.productionRules = new ProductionRules();
    }

    // add the tokens data to the error handler
    public void addTokensData(ErrorMsg errorMsg) {
        this.tokensErrors.add(errorMsg);
    }

    public boolean hasErrors() {
        return this.tokensErrors.size() > 0;
    }

    public String returnTheFirstErrorString(){
        return tokensErrors.get(0).toString();
    }

    // public String getTheRightErrorMsg(String currentToken, String topOfStack, int lineNo) {

    //     // case 1: if the top of the stack is a terminal and the current token is not the same as the terminal
    //     if (productionRules.CheckIsNonTerminal(topOfStack) == false && !topOfStack.equals(currentToken)) {
    //         return "Error: Expected " + topOfStack + " but found " + currentToken + " before line " + lineNo;
    //     }

    //     // case 2: if the top of the stack is a non-terminal and the current token is not the same as the terminal
    //     // get the firsts of the top of the stack
    //     List<String> firsts = productionRules.firsts.get(topOfStack);
    //     // check if firts contains lambda(empty list)
    //     if (firsts.contains("[]")) {
    //         // get the follows of the top of the stack
    //         List<String> follows = productionRules.follows.get(topOfStack);
    //         // check if follows contains the current token
    //         if (!follows.contains(currentToken)) {
    //             return "Error: Expected " + follows.toString() + " or " + firsts.toString() + " but found " + currentToken + " before line " + lineNo;
    //         }
    //     } else {
    //         // check if firsts contains the current token
    //         if (!firsts.contains(currentToken)) {
    //             return "Error: Expected " + firsts + " but found " + currentToken + " before line " + lineNo;
    //         }
    //     }

    //     return null;
        
    // }

}
