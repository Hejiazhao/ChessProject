package view;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;

//“鼠”棋子的代码
public class CatChessComponent extends AnimalChessComponent {
    private Image catImage1;
    private Image catImage2;

    public CatChessComponent(PlayerColor owner, int size) {
        super(owner, size);
        setLocation(1, 5);
        this.animalType = AnimalType.Cat;
        this.catImage1 = new ImageIcon("resource/Blue-cat.jpg").getImage();
        this.catImage2 = new ImageIcon("resource/Red-cat.jpg").getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (owner.equals(PlayerColor.BLUE)) g2.drawImage(catImage1, 5, 5, 62, 62, null);
        else g2.drawImage(catImage2, 5, 5, 62, 62, null);

        if (isSelected()) { // Highlights the model if selected.
            g.setColor(owner.getColor());
            g.drawOval(0, 0, getWidth(), getHeight());
        }
        revalidate();
    }


}
