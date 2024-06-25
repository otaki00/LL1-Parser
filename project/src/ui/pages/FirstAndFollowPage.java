package ui.pages;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

import constants.ProductionRules;

import java.util.HashMap;

// this is the first and follow page that will display the first and follow sets to the user
public class FirstAndFollowPage extends Application {

    // Example first and follow sets data structure
    public Map<String, List<String>> firsts = new HashMap<>();
    public Map<String, List<String>> follows = new HashMap<>();

    private TableView<FirstAndFollowRow> firstTableView;
    private TableView<FirstAndFollowRow> followTableView;
    private ObservableList<FirstAndFollowRow> firstData;
    private ObservableList<FirstAndFollowRow> followData;
    private ProductionRules productionRules;

    public FirstAndFollowPage(ProductionRules productionRules) {
        this.productionRules = productionRules;
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize production rules
        this.firsts = productionRules.firsts;
        this.follows = productionRules.follows;

        Stage newWindow = new Stage();
        newWindow.setTitle("FIRST and FOLLOW Sets");

        // Create TableViews
        firstTableView = new TableView<>();
        followTableView = new TableView<>();

        // Create columns for FIRST table
        TableColumn<FirstAndFollowRow, String> nonTerminalFirstColumn = new TableColumn<>("Non-Terminal");
        nonTerminalFirstColumn.setCellValueFactory(new PropertyValueFactory<>("nonTerminal"));

        TableColumn<FirstAndFollowRow, String> firstSetColumn = new TableColumn<>("FIRST Set");
        firstSetColumn.setCellValueFactory(new PropertyValueFactory<>("set"));

        firstTableView.getColumns().addAll(nonTerminalFirstColumn, firstSetColumn);

        // Create columns for FOLLOW table
        TableColumn<FirstAndFollowRow, String> nonTerminalFollowColumn = new TableColumn<>("Non-Terminal");
        nonTerminalFollowColumn.setCellValueFactory(new PropertyValueFactory<>("nonTerminal"));

        TableColumn<FirstAndFollowRow, String> followSetColumn = new TableColumn<>("FOLLOW Set");
        followSetColumn.setCellValueFactory(new PropertyValueFactory<>("set"));

        followTableView.getColumns().addAll(nonTerminalFollowColumn, followSetColumn);

        // Populate table data
        firstData = FXCollections.observableArrayList();
        for (Map.Entry<String, List<String>> entry : firsts.entrySet()) {
            firstData.add(new FirstAndFollowRow(entry.getKey(), String.join(", ", entry.getValue())));
        }
        firstTableView.setItems(firstData);

        followData = FXCollections.observableArrayList();
        for (Map.Entry<String, List<String>> entry : follows.entrySet()) {
            followData.add(new FirstAndFollowRow(entry.getKey(), String.join(", ", entry.getValue())));
        }
        followTableView.setItems(followData);

        // Search functionality
        TextField firstSearchField = new TextField();
        firstSearchField.setPromptText("Search FIRST Non-Terminal");
        Button firstSearchButton = new Button("Search");
        firstSearchButton.setOnAction(e -> searchFirstNonTerminal(firstSearchField.getText()));

        TextField followSearchField = new TextField();
        followSearchField.setPromptText("Search FOLLOW Non-Terminal");
        Button followSearchButton = new Button("Search");
        followSearchButton.setOnAction(e -> searchFollowNonTerminal(followSearchField.getText()));

        HBox firstSearchBox = new HBox(10, firstSearchField, firstSearchButton);
        HBox followSearchBox = new HBox(10, followSearchField, followSearchButton);

        firstSearchBox.setPadding(new Insets(10));
        followSearchBox.setPadding(new Insets(10));

        VBox vbox = new VBox(20, new Label("FIRST Sets"), firstSearchBox, firstTableView, new Label("FOLLOW Sets"),
                followSearchBox, followTableView);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 800, 600);
        newWindow.setScene(scene);
        newWindow.show();
    }

    private void searchFirstNonTerminal(String nonTerminal) {
        if (nonTerminal == null || nonTerminal.trim().isEmpty()) {
            firstTableView.setItems(firstData);
            return;
        }

        ObservableList<FirstAndFollowRow> filteredData = FXCollections.observableArrayList();
        for (FirstAndFollowRow row : firstData) {
            if (row.getNonTerminal().equals(nonTerminal)) {
                filteredData.add(row);
            }
        }

        firstTableView.setItems(filteredData);
    }

    private void searchFollowNonTerminal(String nonTerminal) {
        if (nonTerminal == null || nonTerminal.trim().isEmpty()) {
            followTableView.setItems(followData);
            return;
        }

        ObservableList<FirstAndFollowRow> filteredData = FXCollections.observableArrayList();
        for (FirstAndFollowRow row : followData) {
            if (row.getNonTerminal().equals(nonTerminal)) {
                filteredData.add(row);
            }
        }

        followTableView.setItems(filteredData);
    }

    public static void main(String[] args) {
        launch(args);
    }

    // FirstAndFollowRow class
    public static class FirstAndFollowRow {
        private final String nonTerminal;
        private final String set;

        public FirstAndFollowRow(String nonTerminal, String set) {
            this.nonTerminal = nonTerminal;
            this.set = set;
        }

        public String getNonTerminal() {
            return nonTerminal;
        }

        public String getSet() {
            return set;
        }
    }
}
