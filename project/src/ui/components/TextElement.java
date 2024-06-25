package ui.components;

// this class is for the text element in javafx
// just for make it easier to add the text element in the gridpane
public class TextElement extends javafx.scene.text.Text{ 
    
    // set the font size of the text elemen
    private int fontSize;
    // set the font family of the text element
    private String fontFamily;
    // set the font color of the text element
    private String fontColor;
    // set the content of the text element
    private String text;
    // set font weight of the text element
    private String fontWeight;
    
    public TextElement(String text, int fontSize, String fontFamily, String fontColor, String fontWeight) {
        super(text);
        this.text = text;
        this.fontSize = fontSize;
        this.fontFamily = fontFamily;
        this.fontColor = fontColor;
        this.fontWeight = fontWeight;
        this.setText(text);
        // this.setFont(javafx.scene.text.Font.font(fontFamily, fontSize));
        this.setFill(javafx.scene.paint.Color.web(fontColor));
        this.setFont(javafx.scene.text.Font.font(fontFamily, javafx.scene.text.FontWeight.valueOf(fontWeight), fontSize));

 

        // Bind the wrapping width property to the width of the window
        // this.wrappingWidthProperty().bind();
    }
    
    public TextElement(String text, int fontSize, String fontFamily, String fontColor, String fontWeight, int wrappingWidth) {
        super(text);
        this.text = text;
        this.fontSize = fontSize;
        this.fontFamily = fontFamily;
        this.fontColor = fontColor;
        this.fontWeight = fontWeight;
        this.setText(text);
        // this.setFont(javafx.scene.text.Font.font(fontFamily, fontSize));
        this.setFill(javafx.scene.paint.Color.web(fontColor));
        this.setFont(
                javafx.scene.text.Font.font(fontFamily, javafx.scene.text.FontWeight.valueOf(fontWeight), fontSize));

        this.setWrappingWidth(wrappingWidth); 

        // Bind the wrapping width property to the width of the window
        // this.wrappingWidthProperty().bind();
    }
}
