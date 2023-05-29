package view;


import model.PlayerColor;

import javax.swing.*;
import java.awt.*;

/**
 * This is the equivalent of the ChessPiece class,
 * but this class only cares how to draw Chess on ChessboardComponent
 */
//改成了父类
public class AnimalChessComponent extends JComponent {
    public AnimalType animalType;
    //添加动物类型用于识别
    PlayerColor owner;
    private boolean selected;
    public AnimalChessComponent(PlayerColor owner, int size) {
        this.owner = owner;
        this.selected = false;
        setSize(size, size);
        setVisible(true);
        revalidate();
    }

    public PlayerColor getOwner() {
        return owner;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(owner.getColor());
        g.drawRoundRect(0, 0, 72, 72, 16, 16);

    }

    public enum AnimalType {
        Elephant, Mouse, Lion, Cat, Dog, Leopard, Tiger, Wolf
    }


}
