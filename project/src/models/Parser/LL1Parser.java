package models.Parser;

import java.io.IOException;
import java.util.List;

import constants.ProductionRules;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import models.ErrorHandler.ErrorHandler;
import models.ErrorHandler.ErrorMsg;
import models.ErrorHandler.TokenMsg;
import models.Scanner.LexicalAnalyzer;
import models.Stack.StackOperations;


// this class contians all the methods needs to parse the input ffile, comes along with simple simulation for the parsing process
// this class depend on the lexical analyzer to get the tokens data
//  and on StackOperations to handle the stack operations
// they way it work is first initialize the scanner that will scan the input file and get the tokens data
// for that we need the filepath that we will get from file chooser in the UI
// then after that we will initialize the stack and the production rules
// then we will start the parsing operation by adding the start symbol to the stack and so on tell the end or if there's an error
public class LL1Parser {
    // define the the needed attributes
    LexicalAnalyzer LxA; 
    public List<Integer> tokenList;
    public StackOperations parsingStack;
    public ProductionRules productionRules;
    public List<TokenMsg> tokenMsgs;
    private ErrorHandler errorHandler;
    private TokenMsg previousLine = null;

    public LL1Parser(String FilePath) throws IOException {
        // initialize the attributes of our class
        this.LxA = new LexicalAnalyzer(FilePath);
        this.parsingStack = new StackOperations();
        this.productionRules = new ProductionRules();
        this.errorHandler = LxA.getErrorHandler();

        this.tokenList = LxA.tokenList;
        this.tokenMsgs = LxA.tokensData;

        // define the initial tokenMsg in case there's error in line one
        this.previousLine = tokenMsgs.get(0);
    }


    // this method will add the production rules in reverse order to the stack
    // based in pasring algorithm 
    private void addStringInReverse(String str) {
    // first check if the string is empty or has only one character
    if (str.length() <= 1) return; 
    // the production rule come like this ->  [rule1, rule2, rule3]
    // remove the square brackets that come from the production rules list 
    str = str.substring(1, str.length() - 1);
    // then split the string based on the comma
    String[] rule = str.split(",");
    // then loop over the rule and add it to the stack in reverse order
    for (int i = rule.length - 1; i >= 0; i--) {
        String temp = rule[i];
        // remove the white spaces
        temp = temp.replaceAll("\\s", "");
        // System.out.println("Adding: " + temp);
        parsingStack.push(temp);
    }
}

    // this method is important for the pasrsing algorithm, it will  geth the rules from the parsing table based on the current token and the top of the stack (non-terminal and the terminal) and return it 
    public boolean addRulesForCurrentToken(String token, String terminal, TokenMsg tokenMsg, TextArea textArea, Timeline timeline) {
        // first get the rule from the productionRules class that hold the parssing table
        String tempRule = productionRules.getTheProductionRuleForSpecificTokens(token, terminal);
        // then check if the rule is null  which means there's an error
        if (tempRule == null ) {
            // print the error message and stop the process
            textArea.appendText(
                    "Error: Invalid token In { " + previousLine.getLineContent() + " } at line "
                            + previousLine.getLineNo() + " In : " + terminal + "\n");
            timeline.stop();
            return false;
        // check if the rule is lambda which means the rule is empty in this case we will return true, just to let the parsing know we had lambda and we need to continue continue 
        }else if (tempRule.equals("[]")) {
            return true;
        }
        // in this case there's a rule and we will add it to the stack
        this.addStringInReverse(tempRule);
        // this.currentRule = tempRule;
        return false;
        
    }

    // this the core method for the project that will start the parsing process
    // we need to pass for it some UI element just for simulation porpuses( not really simulation but just to show the user the parsing process in the UI), the current token label, the stack view, the text area to show the output and the speed slider to control the speed of the parsing process
    public void parsingOperation(Label currentTokenLabel, StackPane stackView, TextArea textArea, Slider speedSlider) {
    // Initialize the start rule in the stack (we need to add the start rule which is module-decl)
    // check the first token if equal to the start symbol
    // but check if the first token is not equal to the start symbol
    if(tokenMsgs.get(0).getToken().equals("module")){
        this.addRulesForCurrentToken("module-decl", "module", null, textArea, null);    
    }else{
        // show error message
        textArea.appendText("Error: Invalid token In { " + previousLine.getLineContent() + " } at line "
                + previousLine.getLineNo() +  "\n");
        return;
    }

    // Add the stack to the stack view
    // this method in StackOperations class that will update the stack view in the UI
    parsingStack.addStackToStackView(stackView);

    // Create a Timeline for smooth animation
    // timeline is a class in javafx that will help us to control the speed of the parsing process
    Timeline timeline = new Timeline();
    // set the cycle count to indefinite, which means the parsing process will continue until the end or if there's an error, cycle count is the number of times the timeline will repeat, timeline will repeat indefinitely
    timeline.setCycleCount(Timeline.INDEFINITE);

    

    // Define the KeyFrame, which will be executed every 1000 milliseconds, or 1 second
    // its like for loop that will be executed every 1 second until the end or if there's an error
    KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), event -> {
        // if the stack is empty and still there's tokens in the list, then there's an error
        if (tokenList.isEmpty()) {
            timeline.stop();
            textArea.appendText("Error: Unexpected end of token list at \n");
            return;
        }

        // those to handle the errors from Lexical Analyzer 
        if (errorHandler.hasErrors()) {
            timeline.stop();
            textArea.appendText(errorHandler.returnTheFirstErrorString() + "\n");
            return;
        }

        // get the current token and the current index of the token, by remove the first token from the list(beacuse we use list this possible so we dont need for loop, and for that the simulation process will be more easy and fast)
        int currentIndex = tokenList.remove(0);
        TokenMsg tokenMsg = tokenMsgs.remove(0);
        String currentToken = getCurrentToken(currentIndex);
        textArea.appendText("Current Line: " + tokenMsg.getLineContent() + "\n");
        // convert the ( , ) to comma, just when split the string it will be removed
        if (",".equals(currentToken)) currentToken = "comma";
        currentTokenLabel.setText("Current Token: " + currentToken);

        // Check if the top of the stack is non-terminal
        // in this case we need to loop until we find the rule for the current token and the top of the stack
        // which in this case the top will be terminal 
        if (productionRules.CheckIsNonTerminal(parsingStack.top())) {
            // just a flag to check if rule for current token and the top of the stack is found
            boolean isRuleFound = false;
            while (!isRuleFound && !parsingStack.isEmpty()) {
                // get the top of the stack
                String getRuleTop = parsingStack.pop();

                // Check if the top of the stack is a non-terminal
                if (productionRules.CheckIsNonTerminal(getRuleTop)) {
                    // get the rule for the current token and the top of the stack
                    boolean isLambda = addRulesForCurrentToken(getRuleTop, currentToken, 
                            previousLine, textArea, timeline);
                            // if the rule is not lambda, then we need to check if the current token is equal to the top of the stack
                    if (!isLambda) {
                        if (parsingStack.top().equals(currentToken)) {
                            // in this just pop the top 
                            parsingStack.pop();
                            // and set the flag to true
                            isRuleFound = true;
                        }
                    } else {
                        // if the rule is lambda, then we need to continue the process
                        continue;
                    }
                    // add the stack to the stack view
                    parsingStack.addStackToStackView(stackView);
                } else {
                    // if the top of the stack is terminal, then we need to check if the current token is equal to the top of the stack
                    if (currentToken.equals(getRuleTop)) {
                        // just set it to true
                        isRuleFound = true;
                    } else {
                        // error
                        textArea.appendText(
                                "Error: Invalid token In { " + previousLine.getLineContent() + " } at line "
                                        + previousLine.getLineNo() + " In : " + parsingStack.top() + "\n");
                        timeline.stop();
                        return;
                    }
                }
            }
        } else {
            // if the top of the stack is terminal, then we need to check if the current token is equal to the top of the stack
            if (checkIfTopEqualsString(currentToken, textArea ,tokenMsg, timeline)) {
                // pop and update the view
                parsingStack.pop();
                parsingStack.addStackToStackView(stackView);
            } else {
                // error
                textArea.appendText("Error: Invalid token In { " + previousLine.getLineContent() + " } at line "
                        + previousLine.getLineNo() + " In : "+ currentToken+"\n");
                timeline.stop();
                return;
            }
        }

        // now in each loop we need to check if the stack is empty and the token list is empty and the current token is equal to the last token in the list
        if (parsingStack.size() == 1 &&  parsingStack.top().equals(".") && tokenList.isEmpty() && currentToken.equals(".")) {
            // then we done an all good
            textArea.appendText("Parsing Successful\n");
            timeline.stop();
        }
        previousLine = tokenMsg;
        
    });


    // Add the KeyFrame to the Timeline, and start the Timeline
    timeline.getKeyFrames().add(keyFrame);
    
    // Bind the rate of the Timeline to the value of the speed slider
    timeline.rateProperty().bind(speedSlider.valueProperty());
    timeline.play();
}


    //  this is simple method just to check if the top of the stack is equal to the string token
    public Boolean checkIfTopEqualsString(String str, TextArea textArea ,TokenMsg tokenMsg, Timeline timeline) {
        if (parsingStack.isEmpty()) {
            textArea.appendText(new ErrorMsg(tokenMsg.getLineNo(), tokenMsg.getColNo(), "Error: Expected " + str + " but found nothing").toString());
            timeline.stop();

            return false;
        }
        if (str.equals(parsingStack.top())){ 
            return true;
        } else {
            return false;
        }
    }

    // get the token string by its id from the tokenIndexTable
    public String getCurrentToken(int indexForToken) {
        return LxA.tokenIndexTable.get(indexForToken);
    }


    public ProductionRules getProductionRules() {
        return this.productionRules;
    }
}