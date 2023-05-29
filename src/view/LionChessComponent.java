package view;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;

//“鼠”棋子的代码
public class LionChessComponent extends AnimalChessComponent {
    private Image LionImage1;
    private Image LionImage2;

    public LionChessComponent(PlayerColor owner, int size) {
        super(owner, size);
        setLocation(0, 0);
        this.animalType = AnimalType.Lion;
        this.LionImage1 = new ImageIcon("resource/Blue-lion.jpg").getImage();
        this.LionImage2 = new ImageIcon("resource/Red-lion.jpg").getImage();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (owner.equals(PlayerColor.BLUE)) g2.drawImage(LionImage1, 5, 5, 62, 62, null);
        else g2.drawImage(LionImage2, 5, 5, 62, 62, null);

        if (isSelected()) { // Highlights the model if selected.
            g.setColor(owner.getColor());
            g.drawOval(0, 0, getWidth(), getHeight());
        }
        revalidate();
    }

}
