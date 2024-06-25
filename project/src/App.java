
import java.nio.file.Paths;

import constants.ProductionRules;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Parser.LL1Parser;
import ui.pages.MainPage;

public class App extends Application {
    public static void main(String[] args) throws Exception {

        // lunch the fx application
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String mainTitle = " LL1 Parser Showcase";
        stage.setTitle(mainTitle);
        // String currentPath = Paths.get("").toAbsolutePath().toString();
        // System.out.println("Current absolute path: " + currentPath);
        // LL1Parser parser = new LL1Parser(
        //         currentPath + "\\src\\input2.txt");
        ProductionRules productionRules = new ProductionRules();


        // main layout
        GridPane mainLayout = MainPage.createMainPage(stage, productionRules);
        // add the scene to the stage
        Scene mainScene = new Scene(mainLayout, 900, 700);
        stage.setScene(mainScene);
        stage.show();
    }
}
