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
    public enum AnimalType{
    Elephant,Mouse
    }
    //添加动物类型用于识别
    PlayerColor owner;
    public AnimalType animalType;

    private boolean selected;

    public AnimalChessComponent(PlayerColor owner, int size) {
        this.owner = owner;
        this.selected = false;
        setSize(size, size);
        setVisible(true);
        revalidate();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public AnimalType getAnimalType(){return animalType;}





}
