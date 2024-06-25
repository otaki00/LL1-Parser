package ui.pages;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

import constants.ProductionRules;

// this is the production rules page that will display the production rules to the user
public class ProductionRulesPage {

    private TableView<ProductionRuleRow> tableView;
    private ObservableList<ProductionRuleRow> data;

    public ProductionRulesPage() {
        // this.productionRules = new ProductionRules();
    }

    public void openProductionRulesWindow(ProductionRules productionRules) {
        Stage newWindow = new Stage();
        newWindow.setTitle("Production Rules");

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20));

        // Add a title to the page
        Label pageTitle = new Label("Production Rules");
        pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: darkblue;");
        vbox.getChildren().add(pageTitle);

        // Create the TableView
        tableView = new TableView<>();

        TableColumn<ProductionRuleRow, String> nonTerminalColumn = new TableColumn<>("Non-Terminal");
        nonTerminalColumn.setCellValueFactory(cellData -> cellData.getValue().nonTerminalProperty());

        TableColumn<ProductionRuleRow, String> productionColumn = new TableColumn<>("Production Rule");
        productionColumn.setCellValueFactory(cellData -> cellData.getValue().productionProperty());

        tableView.getColumns().addAll(nonTerminalColumn, productionColumn);

        // Populate table data
        data = FXCollections.observableArrayList();

        List<String> nonTerminals = productionRules.nonTerminals;

        for (String nonTerminal : nonTerminals) {
            List<List<String>> productions = productionRules.rules.get(nonTerminal);

            StringBuilder ruleText = new StringBuilder();
            for (int i = 0; i < productions.size(); i++) {
                List<String> production = productions.get(i);
                if (i > 0) {
                    ruleText.append(" | ");
                }
                ruleText.append(String.join(" ", production.isEmpty() ? List.of("λ") : production));
            }

            data.add(new ProductionRuleRow(nonTerminal, ruleText.toString()));
        }
        tableView.setItems(data);

        // Create search functionality
        TextField searchField = new TextField();
        searchField.setPromptText("Search Non-Terminal");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> searchNonTerminal(searchField.getText()));

        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setPadding(new Insets(10));

        vbox.getChildren().addAll(searchBox, tableView);

        Scene scene = new Scene(vbox, 800, 600);
        newWindow.setScene(scene);
        newWindow.show();
    }

    private void searchNonTerminal(String nonTerminal) {
        if (nonTerminal == null || nonTerminal.trim().isEmpty()) {
            tableView.setItems(data);
            return;
        }

        ObservableList<ProductionRuleRow> filteredData = FXCollections.observableArrayList();
        for (ProductionRuleRow row : data) {
            if (row.getNonTerminal().equals(nonTerminal)) {
                filteredData.add(row);
            }
        }

        tableView.setItems(filteredData);
    }

    public static class ProductionRuleRow {
        private final SimpleStringProperty nonTerminal;
        private final SimpleStringProperty production;

        public ProductionRuleRow(String nonTerminal, String production) {
            this.nonTerminal = new SimpleStringProperty(nonTerminal);
            this.production = new SimpleStringProperty(production);
        }

        public String getNonTerminal() {
            return nonTerminal.get();
        }

        public SimpleStringProperty nonTerminalProperty() {
            return nonTerminal;
        }

        public String getProduction() {
            return production.get();
        }

        public SimpleStringProperty productionProperty() {
            return production;
        }
    }
}
