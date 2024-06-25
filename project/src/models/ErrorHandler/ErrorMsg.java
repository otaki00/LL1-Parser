package models.ErrorHandler;

public class ErrorMsg {
    
    private int lineNo;
    private int colNo;
    private String errorType;

    public ErrorMsg(int lineNo, int colNo, String errorType) {
        this.lineNo = lineNo;
        this.colNo = colNo;
        this.errorType = errorType;
    }

    @Override
    public String toString() {
        return errorType + " at line " + lineNo + " and column " + colNo;
    }
    
}
