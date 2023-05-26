package view;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;


//将原来“象”棋子的公有功能拆分后，私有的功能
public class ElephantChessComponent extends AnimalChessComponent{
private Image elephantImage1;
    private Image elephantImage2;

    public ElephantChessComponent(PlayerColor owner, int size){
        super(owner,size);
        setLocation(2,6);
        this.animalType=AnimalType.Elephant;
        this.elephantImage1= new ImageIcon("resource/Blue-elephant.jpg").getImage();
        this.elephantImage2=new ImageIcon("resource/Red-elephant.jpg").getImage();
        }
@Override
protected void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(owner.getColor());
    if (owner.equals(PlayerColor.BLUE)) g2.drawImage(elephantImage1,5,5,62,62,null);
    else g2.drawImage(elephantImage2,5,5,62,62,null);
    g.drawRoundRect(0,0,72,72,16,16);
    if (isSelected()) { // Highlights the model if selected.
        g.setColor(owner.getColor());
        g.drawOval(0, 0, getWidth() , getHeight());
    }
}

}


