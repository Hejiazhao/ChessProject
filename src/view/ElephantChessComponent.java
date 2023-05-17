package view;

import model.PlayerColor;
import javax.swing.*;
import java.awt.*;


//将原来“象”棋子的公有功能拆分后，私有的功能
public class ElephantChessComponent extends AnimalChessComponent{
private Image elephantImage;
    public ElephantChessComponent(PlayerColor owner, int size){
        super(owner,size);
        setLocation(2,6);
        this.animalType=AnimalType.Elephant;
        this.elephantImage=new ImageIcon("C:\\Users\\蔡卓茜\\IdeaProjects\\ChessProject\\resource\\Elephant-blue.png").getImage();
    }
@Override
protected void paintComponent(Graphics g){
        super.paintComponents(g);
        Graphics2D g2=(Graphics2D) g;
        g2.drawImage(elephantImage,0,0,null);
}

}


