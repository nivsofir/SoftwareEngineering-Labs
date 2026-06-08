package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class GameMessage implements Serializable {
    private String type;
    private int row;
    private int col;
    private String symbol;
    private String text;

    public GameMessage(String type) {
        this.type = type;
    }

    public GameMessage(String type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
    }

    public GameMessage(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public GameMessage(String type, int row, int col, String symbol, String text) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.symbol = symbol;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getText() {
        return text;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setText(String text) {
        this.text = text;
    }
}