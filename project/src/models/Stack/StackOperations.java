package models.Stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


// this is to preforme the stack operations that we need in the parsing process 
public class StackOperations {
    

    // create the stack
    private Stack<String> stack = new Stack<String>();

    // push the string to the stack
    public void push(String str) {
        stack.push(str);
    }

    // pop the string from the stack
    public String pop() {
        return stack.pop();
    }

    // get the top of the stack
    public String top() {
        return stack.peek();
    }

    // check if the stack is empty
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    // get the size of the stack
    public int size() {
        return stack.size();
    }


    // print the stack
    public void printStack() {
        System.out.println(stack);
    }

    public List<String> getStackContents() {
        return new ArrayList<>(stack);
    }

    public void addStackToStackView(StackPane stackView) {
    // First clear the stack view
    stackView.getChildren().clear();
    
    // Create a VBox to hold the stack elements
    VBox vbox = new VBox();
    vbox.setAlignment(Pos.BOTTOM_CENTER);
    
    // Add the stack elements to the VBox
    for (int i = stack.size() - 1; i >= 0; i--) {
        String temp = stack.get(i);
        Label label = new Label(temp.toString());
        label.setStyle("-fx-padding: 5px;");
        vbox.getChildren().add(label);
    }

    // Add the VBox to the StackPane
    stackView.getChildren().add(vbox);
}
    
}
