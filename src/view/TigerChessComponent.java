package view;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;

//“鼠”棋子的代码
public class TigerChessComponent extends AnimalChessComponent{
    private Image tigerImage1;
    private Image tigerImage2;
    public TigerChessComponent(PlayerColor owner, int size){
        super(owner,size);
        setLocation(0,6);
        this.animalType=AnimalType.Tiger;
        this.tigerImage1=new ImageIcon("resource/Blue-tight.jpg").getImage();
        this.tigerImage2=new ImageIcon("resource/Red-tight.jpg").getImage();

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(owner.getColor());
        if (owner.equals(PlayerColor.BLUE)) g2.drawImage(tigerImage1,0,0,72,72,null);
        else g2.drawImage(tigerImage2,0,0,72,72,null);

        if (isSelected()) { // Highlights the model if selected.
            g.setColor(owner.getColor());
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }

}
