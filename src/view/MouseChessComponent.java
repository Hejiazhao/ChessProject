package view;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;

//“鼠”棋子的代码
public class MouseChessComponent extends AnimalChessComponent{
    private Image mouseImage1;
    private Image mouseImage2;
    public MouseChessComponent(PlayerColor owner, int size){
        super(owner,size);
        setLocation(2,0);
        this.animalType=AnimalType.Mouse;
        this.mouseImage1=new ImageIcon("resource/Blue-mouse.jpg").getImage();
        this.mouseImage2=new ImageIcon("resource/Red-mouse.jpg").getImage();

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (owner.equals(PlayerColor.BLUE)) g2.drawImage(mouseImage1,5,5,62,62,null);
        else g2.drawImage(mouseImage2,5,5,62,62,null);

        if (isSelected()) { // Highlights the model if selected.
            g.setColor(owner.getColor());
            g.drawOval(0, 0, getWidth() , getHeight());
        }
        revalidate();
    }
}
