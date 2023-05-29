package view;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;

//“鼠”棋子的代码
public class LeopardChessComponent extends AnimalChessComponent {
    private Image LeopardImage1;
    private Image LeopardImage2;

    public LeopardChessComponent(PlayerColor owner, int size) {
        super(owner, size);
        setLocation(2, 2);
        this.animalType = AnimalType.Leopard;
        this.LeopardImage1 = new ImageIcon("resource/Blue-leopard.jpg").getImage();
        this.LeopardImage2 = new ImageIcon("resource/Red-leopard.jpg").getImage();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (owner.equals(PlayerColor.BLUE)) g2.drawImage(LeopardImage1, 5, 5, 62, 62, null);
        else g2.drawImage(LeopardImage2, 5, 5, 62, 62, null);

        if (isSelected()) { // Highlights the model if selected.
            g.setColor(owner.getColor());
            g.drawOval(0, 0, getWidth(), getHeight());
        }
        revalidate();
    }

}
