package view;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;

//“鼠”棋子的代码
public class LeopardChessComponent extends AnimalChessComponent{
    private Image LeopardImage1;
    private Image LeopardImage2;
    public LeopardChessComponent(PlayerColor owner, int size){
        super(owner,size);
        setLocation(2,2);
        this.animalType=AnimalType.Leopard;
        this.LeopardImage1=new ImageIcon("resource/Blue-leopard.jpg").getImage();
        this.LeopardImage2=new ImageIcon("resource/Red-leopard.jpg").getImage();

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(owner.getColor());
        if (owner.equals(PlayerColor.BLUE)) g2.drawImage(LeopardImage1,0,0,72,72,null);
        else g2.drawImage(LeopardImage2,0,0,72,72,null);

        if (isSelected()) { // Highlights the model if selected.
            g.setColor(owner.getColor());
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }

}
