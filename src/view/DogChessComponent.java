package view;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;

//“鼠”棋子的代码
public class DogChessComponent extends AnimalChessComponent {
    private Image DogImage1;
    private Image DogImage2;

    public DogChessComponent(PlayerColor owner, int size) {
        super(owner, size);
        setLocation(1, 1);
        this.animalType = AnimalType.Dog;
        this.DogImage1 = new ImageIcon("resource/Blue-dog.jpg").getImage();
        this.DogImage2 = new ImageIcon("resource/Red-dog.jpg").getImage();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (owner.equals(PlayerColor.BLUE)) g2.drawImage(DogImage1, 5, 5, 62, 62, null);
        else g2.drawImage(DogImage2, 5, 5, 62, 62, null);

        if (isSelected()) { // Highlights the model if selected.
            g.setColor(owner.getColor());
            g.drawOval(0, 0, getWidth(), getHeight());
        }
        revalidate();
    }

}

