package ui.pages;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.Parser.LL1Parser;
import ui.components.TextElement;


// this is the parsing page that will display the parsing process to the user
public class ParsingPage extends Application {

    private LL1Parser parser;


    public ParsingPage(LL1Parser parser) {
        this.parser = parser;
    }

    @Override
    public void start(Stage arg0) {
        Stage stage = new Stage();
        stage.setTitle("Parsing Page");
        GridPane root = new GridPane();
        root.setPadding(new Insets(10)); 
        root.setHgap(10);
        root.setVgap(10);
        
        // Create a label and add it to the top left
        // TextElement label = new TextElement("Parsing Page", 20, "Arial", "#000", "BOLD");
        Label label = new Label("Parsing Page");
        GridPane.setMargin(label, new Insets(10)); 
        root.add(label, 0, 0);

        Label currentLineLabel = new Label("Current Token: ");
        GridPane.setMargin(currentLineLabel, new Insets(10)); 
        root.add(currentLineLabel, 0, 1);
        
        // Create a text area and add it to the bottom left
        TextArea textArea = new TextArea();
        GridPane.setMargin(textArea, new Insets(10)); 
        root.add(textArea, 0, 1);
        
        // Create a StackPane and add it to the right
        StackPane stackView = new StackPane();
        // Add components to the stack view here (if any)
        stackView.setStyle("-fx-border-color: black; -fx-background-color: lightgray; -fx-width: 400px;");
    
        root.add(stackView, 1, 0);
        GridPane.setRowSpan(stackView, 2); 
        GridPane.setMargin(stackView, new Insets(10)); 
        
        // Create a label under the stack view
        Label stackViewLabel = new Label("This is the stack Operation");
        root.add(stackViewLabel, 1, 2);
        GridPane.setMargin(stackViewLabel, new Insets(10));

        Button actionButton = new Button("Start Parsing");
        root.add(actionButton, 1, 3);
        GridPane.setMargin(actionButton, new Insets(10)); 
        GridPane.setRowSpan(actionButton, 1);
        
        Slider speedSlider = new Slider(0.1, 30.0, 1.0);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setMinorTickCount(4);
        speedSlider.setBlockIncrement(0.1);

        root.add(speedSlider, 0, 3);

        // Set the action for the button
        actionButton.setOnAction(event -> {
            parser.parsingOperation(label, stackView, textArea, speedSlider);
        });

        Scene scene = new Scene(root, 700, 400);
        stage.setScene(scene);
        stage.show();
    }
    
}
