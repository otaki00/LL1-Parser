package ui.components;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class ButtonElement {

    private Button button;

    // Constructor with text and default size
    public ButtonElement(String text) {
        this.button = new Button(text);
    }

    // Constructor with text and preferred size
    public ButtonElement(String text, double width, double height) {
        this.button = new Button(text);
        this.button.setPrefSize(width, height);
    }

    // Method to set an action on button click
    public void setOnAction(EventHandler<? super MouseEvent> eventHandler) {
        this.button.setOnMouseClicked(eventHandler);
    }

    // Method to set a tooltip
    public void setTooltip(String tooltipText) {
        this.button.setTooltip(new Tooltip(tooltipText));
    }

    // Method to set a style
    public void setStyle(String style) {
        this.button.setStyle(style);
    }

    // Method to set an icon
    public void setIcon(String imagePath, double width, double height) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        this.button.setGraphic(imageView);
    }

    // Method to get the button instance
    public Button getButton() {
        return this.button;
    }
}
