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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import constants.ProductionRules;

import java.util.HashMap;

// this is the parsing table page that will display the parsing table to the user
public class ParsingTablePage extends Application {

    // Example parsing table data structure
    // public Map<String, List<Map<String, String>>> parsingTable = new HashMap<>();

    private TableView<ParsingTableRow> tableView;
    private ObservableList<ParsingTableRow> data;
    private Map<String, List<Map<String, String>>> parsingTable;
    private ProductionRules productionRules;

    public ParsingTablePage(ProductionRules productionRules) {
        this.productionRules = productionRules;
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize production rules and parsing table
        this.parsingTable = productionRules.parsingTable;

        Stage newWindow = new Stage();
        newWindow.setTitle("Parsing Table");

        // Create a TableView
        tableView = new TableView<>();

        // Create columns
        TableColumn<ParsingTableRow, String> nonTerminalColumn = new TableColumn<>("Non-Terminal");
        nonTerminalColumn.setCellValueFactory(new PropertyValueFactory<>("nonTerminal"));

        TableColumn<ParsingTableRow, String> terminalColumn = new TableColumn<>("Terminal");
        terminalColumn.setCellValueFactory(new PropertyValueFactory<>("terminal"));

        TableColumn<ParsingTableRow, String> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));

        tableView.getColumns().addAll(nonTerminalColumn, terminalColumn, actionColumn);

        // Populate table data
        data = FXCollections.observableArrayList();
        List<String> nonTerminals = productionRules.nonTerminals;
        for (String nonTerminal : nonTerminals) {
            // String nonTerminal = entry.getKey();
            for (Map<String, String> actionMap : parsingTable.get(nonTerminal)) {
                for (Map.Entry<String, String> actionEntry : actionMap.entrySet()) {
                    data.add(new ParsingTableRow(nonTerminal, actionEntry.getKey(), actionEntry.getValue()));
                }
            }
        }
        tableView.setItems(data);

        // Search functionality
        TextField searchField = new TextField();
        searchField.setPromptText("Search Non-Terminal");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchNonTerminal(searchField.getText()));

        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setPadding(new Insets(10));

        VBox vbox = new VBox(10, searchBox, tableView);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 800, 600);
        newWindow.setScene(scene);
        newWindow.show();
    }

    private void searchNonTerminal(String nonTerminal) {
        if (nonTerminal == null || nonTerminal.trim().isEmpty()) {
            tableView.setItems(data);
            return;
        }

        ObservableList<ParsingTableRow> filteredData = FXCollections.observableArrayList();
        for (ParsingTableRow row : data) {
            if (row.getNonTerminal().equals(nonTerminal)) {
                filteredData.add(row);
            }
        }

        tableView.setItems(filteredData);
    }

    public static void main(String[] args) {
        launch(args);
    }

    // ParsingTableRow class
    public static class ParsingTableRow {
        private final String nonTerminal;
        private final String terminal;
        private final String action;

        public ParsingTableRow(String nonTerminal, String terminal, String action) {
            this.nonTerminal = nonTerminal;
            this.terminal = terminal;
            this.action = action;
        }

        public String getNonTerminal() {
            return nonTerminal;
        }

        public String getTerminal() {
            return terminal;
        }

        public String getAction() {
            return action;
        }
    }
}
