package ui.pages;

import java.io.File;
import java.io.IOException;

import constants.ProductionRules;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Parser.LL1Parser;
import ui.components.ButtonElement;
import ui.components.TextElement;

// this the main page of the application, that will display the basic information about the LL1 parser, and the options that the user can interact with
public class MainPage  {

    private static Button displayProductionRules;
    private static Button displayParsingTable;
    private static Button displayFirstsAndFollows;

    private static File inputFile;
    
    public static GridPane createMainPage( Stage primaryStage, ProductionRules productionRules) {
        String mainTitle = "LL1 Parser Showcase 👌";
        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(20);
        mainLayout.setVgap(20);

        int wrappingWidth = 800;

        // add the title
        TextElement mainTitleText = new TextElement(mainTitle, 50, "Monospaced", "#f0f0f0", "BOLD");

        String bref = "🙌 In this showcase, I will show you how to use the LL1 parser, and how it works, so please take your time interacting with it.";
        TextElement brefElement = new TextElement(bref, 20, "Ariel", "#fff", "SEMI_BOLD", wrappingWidth);


        // set background
        Image backgroundImage = new Image("C:\\Users\\asus\\Desktop\\term2_2024\\compiler\\LL1-Parser\\project\\resources\\images\\mainBackground.png");
        
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT, // Set NO_REPEAT if you do not want the image to repeat
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(400, 300, false, false, false, false)
        );

        // mainLayout.setBackground(new Background(background));
        mainLayout.setBackground(new Background(new BackgroundFill(Color.rgb(38, 38, 38), CornerRadii.EMPTY, null)));


        // set a quick introduction  about the LL1 parser

        String whatIsLL1parser = " -> LL1 parser is a top-down parser that parses the input from left to right and constructs a leftmost derivation of the sentence. It uses a parsing table to make parsing decisions.";

        String whatParser = " -> Parser in a computer science, a parser is a computer program that reads and checks the syntax of a string of characters, typically written in a programming language, to determine whether it conforms to the rules of a formal grammar.";

        TextElement whatIs = new TextElement(" What is LL1 parser ? 🤔", 25, "Verdana", "#fff", "BOLD", wrappingWidth);

        TextElement whatIsLL1parserElement = new TextElement(whatIsLL1parser, 20, "Verdana", "#faff88", "SEMI_BOLD",
                700);
        TextElement whatParserElement = new TextElement(whatParser, 20, "Verdana", "#faff88", "SEMI_BOLD", 
                wrappingWidth);


        VBox brefBox = new VBox();
        brefBox.getChildren().add(whatIs);
        brefBox.getChildren().add(whatIsLL1parserElement);
        brefBox.getChildren().add(whatParserElement);
        

        brefBox.setSpacing(10);

        // the main needs to make LL1

        TextElement WhatAreTheFirstsAndFollows = new TextElement(" What are the needs to build LL1 Parser? 🤷‍♂️", 25,
                "Verdana", "#fff", "BOLD", wrappingWidth);

        VBox needsContainer = new VBox();
        // set margins
        VBox.setMargin(WhatAreTheFirstsAndFollows, new javafx.geometry.Insets(40, 0, 0, 0));
        needsContainer.setSpacing(20);

        needsContainer.getChildren().add(WhatAreTheFirstsAndFollows);

        HBox needsSection = new HBox();

        needsContainer.getChildren().add(needsSection);

        needsSection.setSpacing(20);
        needsSection.setMaxWidth(800);
        needsSection.setMaxHeight(200);
        needsSection.setPrefWidth(800);

        
        // VBox productionRulesBox = createButtonSection("Production Rules", "See Production Rules");
        // open Production rule page

        VBox productionRulesBox = createButtonSection("Production Rules", "See Firsts and Follows", displayProductionRules);

        Button displayProductionRules = createButton("See Production Rules");
        productionRulesBox.getChildren().add(displayProductionRules);
        ProductionRulesPage productionRulesPage = new ProductionRulesPage();

        displayProductionRules.setOnAction(e -> productionRulesPage.openProductionRulesWindow(productionRules));


        VBox firstsAndFollowsBox = createButtonSection("Firsts and Follows", "See Firsts and Follows", displayFirstsAndFollows);

        Button displayFirstsAndFollows = createButton("See Firsts and Follows");
        firstsAndFollowsBox.getChildren().add(displayFirstsAndFollows);

        displayFirstsAndFollows.setOnAction(e -> {
            FirstAndFollowPage firstAndFollowPage = new FirstAndFollowPage(productionRules);
            firstAndFollowPage.start(null);
        });
        
        

        VBox parsingTableBox = createButtonSection("Parsing Table", "See Parsing Table", displayParsingTable);
        
        Button displayParsingTable = createButton("See Parsing Table");
        parsingTableBox.getChildren().add(displayParsingTable);
        ParsingTablePage parsingTablePage = new ParsingTablePage(productionRules);
        displayParsingTable.setOnAction(e -> parsingTablePage.start(null));

        needsSection.getChildren().addAll(productionRulesBox, firstsAndFollowsBox, parsingTableBox);

        // get the button from the productionRulesBox
        

        // choose file section 
        HBox chooseFileSection = new HBox();
        chooseFileSection.setSpacing(20);

        

        TextElement chooseFileText = new TextElement("Now, If you want to start Using Our LL1 parser, plaese Choose input File: ", 15, "Verdana", "#fff", "BOLD",650);
        chooseFileSection.getChildren().add(chooseFileText);

        ButtonElement chooseFileButton = new ButtonElement("Choose File", 150, 50);
        chooseFileSection.getChildren().add(chooseFileButton.getButton());

        String buttonStyle = "-fx-background-color:  #383838; -fx-text-fill: #fff; -fx-font-size: 13px; -fx-font-family: Monospaced; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-border-color: #fff; -fx-border-width: 1px; -fx-cursor: hand;";
        chooseFileButton.setStyle(buttonStyle);
        // add all elements

        HBox.setMargin(chooseFileText, new javafx.geometry.Insets(20, 0, 0, 0));

        chooseFileButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showOpenDialog(primaryStage);

            if (file != null) {
                inputFile = file;
                try {
                    LL1Parser ll1Parser = new LL1Parser(inputFile.getAbsolutePath());
                    ParsingPage parsingPage = new ParsingPage(ll1Parser);

                    parsingPage.start(null);
                    
                    // simulationPage.start(new Stage());
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        // add all elements 
        mainLayout.add(mainTitleText, 1,1);
        mainLayout.add(brefElement, 1, 2);
        mainLayout.add(brefBox, 1, 3);
        mainLayout.add(needsContainer, 1, 4);
        mainLayout.add(chooseFileSection, 1, 5);
        return mainLayout;
    }

    private static VBox createButtonSection(String titleText, String buttonText, Button buttonAction) {
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setMaxWidth(200);
        vbox.setPrefWidth(250);

        TextElement title = new TextElement(titleText, 14, "Verdana", "#fff", "BOLD");
        // ButtonElement button = new ButtonElement(buttonText, 170, 50);
        // String buttonStyle = "-fx-background-color: #383838; -fx-text-fill: #fff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-border-color: #fff; -fx-border-width: 1px; -fx-cursor: hand;";
        // button.setStyle(buttonStyle);

        vbox.getChildren().add(title);
        // vbox.getChildren().add(button.getButton());
        // buttonAction = button.getButton();

        return vbox;
    }


    private static Button createButton (String text) {
        ButtonElement button = new ButtonElement(text, 170, 50);
        String buttonStyle = "-fx-background-color: #383838; -fx-text-fill: #fff; -fx-font-size: 14px; -fx-font-family: Arial;-fx-font-weight: bold;-fx-border-radius: 10px; -fx-border-color: #fff; -fx-border-width: 1px;-fx-cursor: hand;";
        button.setStyle(buttonStyle);

        return button.getButton();
    }

    
}
