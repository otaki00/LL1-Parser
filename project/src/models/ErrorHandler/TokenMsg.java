package models.ErrorHandler;

// this will represent the data that will be used by the error handler to display the error message 
public class TokenMsg {

    private String token;
    private String lineContent;
    private int lineNo;
    private int colNo;
    private String type;

    public TokenMsg(String token, String lineContent, int lineNo, int colNo, String type) {
        this.token = token;
        this.lineContent = lineContent;
        this.lineNo = lineNo;
        this.colNo = colNo;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public String getLineContent() {
        return lineContent;
    }

    public int getColNo() {
        return colNo;
    }

    public String getType() {
        return type;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    @Override
    public String toString() {
        return "TokenMsg{" +
                "token='" + token + '\'' +
                ", lineContent='" + lineContent + '\'' +
                ", colNo=" + colNo +
                ", type='" + type + '\'' +
                '}';
    }
    
}
