import javax.swing.*;
import java.awt.*;

public class Card extends JButton {

    private static final ImageIcon[] icons = {
            new ImageIcon("res/0.png"),
            new ImageIcon("res/1.png"),
            new ImageIcon("res/2.png"),
            new ImageIcon("res/3.png"),
            new ImageIcon("res/4.png"),
            new ImageIcon("res/5.png"),
            new ImageIcon("res/6.png"),
            new ImageIcon("res/7.png"),
            new ImageIcon("res/8.png"),
            new ImageIcon("res/9.png"),
            new ImageIcon("res/DrawTwo.png"),
            new ImageIcon("res/Skip.png"),
            new ImageIcon("res/Reverse.png"),
            new ImageIcon("res/Wild.png"),
            new ImageIcon("res/WildDrawFour.png")};
    
    private int color;
    private int type;
    private int value;

    public Card(int color, int type, int value) {
        this.color = color;
        this.type = type;
        this.value = value;

        switch (type) {
            case GameState.TYPE_NUMBER:
                setIcon(icons[value]);
                break;
            case GameState.TYPE_DRAW_TWO:
                setIcon(icons[10]);
                break;
            case GameState.TYPE_SKIP:
                setIcon(icons[11]);
                break;
            case GameState.TYPE_REVERSE:
                setIcon(icons[12]);
                break;
            case GameState.TYPE_WILD:
                setIcon(icons[13]);
                break;
            case GameState.TYPE_WILD_DRAW_FOUR:
                setIcon(icons[14]);
                break;
        }

        switch (color) {
            case GameState.COLOR_RED:
                setBackground(Color.RED);
                break;
            case GameState.COLOR_YELLOW:
                setBackground(Color.YELLOW);
                break;
            case GameState.COLOR_BLUE:
                setBackground(Color.BLUE);
                break;
            case GameState.COLOR_GREEN:
                setBackground(Color.GREEN);
                break;
            case GameState.COLOR_BLACK:
                setBackground(Color.BLACK);
                break;
        }

        setOpaque(true);
        setContentAreaFilled(true);
        setBorderPainted(false);
        setFocusPainted(false);

        setMinimumSize(new Dimension(100, 150));
        setPreferredSize(new Dimension(100, 150));
        setMaximumSize(new Dimension(100, 150));
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        switch (color) {
            case GameState.COLOR_RED:
                setBackground(Color.RED);
                break;
            case GameState.COLOR_YELLOW:
                setBackground(Color.YELLOW);
                break;
            case GameState.COLOR_BLUE:
                setBackground(Color.BLUE);
                break;
            case GameState.COLOR_GREEN:
                setBackground(Color.GREEN);
                break;
            case GameState.COLOR_BLACK:
                setBackground(Color.BLACK);
                break;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        switch (type) {
            case GameState.TYPE_NUMBER:
                setIcon(icons[value<14?value:0]);
                break;
            case GameState.TYPE_DRAW_TWO:
                setIcon(icons[10]);
                break;
            case GameState.TYPE_SKIP:
                setIcon(icons[11]);
                break;
            case GameState.TYPE_REVERSE:
                setIcon(icons[12]);
                break;
            case GameState.TYPE_WILD:
                setIcon(icons[13]);
                break;
            case GameState.TYPE_WILD_DRAW_FOUR:
                setIcon(icons[14]);
                break;
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        switch (type) {
            case GameState.TYPE_NUMBER:
                setIcon(icons[value<14?value:0]);
                break;
            case GameState.TYPE_DRAW_TWO:
                setIcon(icons[10]);
                break;
            case GameState.TYPE_SKIP:
                setIcon(icons[11]);
                break;
            case GameState.TYPE_REVERSE:
                setIcon(icons[12]);
                break;
            case GameState.TYPE_WILD:
                setIcon(icons[13]);
                break;
            case GameState.TYPE_WILD_DRAW_FOUR:
                setIcon(icons[14]);
                break;
        }
    }
}
