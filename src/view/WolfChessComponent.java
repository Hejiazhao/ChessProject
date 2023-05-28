package view;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;

//“鼠”棋子的代码
public class WolfChessComponent extends AnimalChessComponent{
    private Image wolfImage1;
    private Image wolfImage2;

    public WolfChessComponent(PlayerColor owner, int size){
        super(owner,size);
        setLocation(2,4);
        this.animalType=AnimalType.Wolf;
        this.wolfImage1=new ImageIcon("resource/Blue-wolf.jpg").getImage();
        this.wolfImage2=new ImageIcon("resource/Red-wolf.jpg").getImage();

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (owner.equals(PlayerColor.BLUE)) g2.drawImage(wolfImage1,5,5,62,62,null);
        else g2.drawImage(wolfImage2,5,5,62,62,null);

        if (isSelected()) { // Highlights the model if selected.
            g.setColor(owner.getColor());
            g.drawOval(0, 0, getWidth() , getHeight());
        }
        revalidate();
    }

}
