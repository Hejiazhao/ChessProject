package view;

import model.PlayerColor;

import java.awt.*;
import java.lang.reflect.Type;

//将原来“象”棋子的公有功能拆分后，私有的功能
public class ElephantChessComponent extends AnimalChessComponent{

    public ElephantChessComponent(PlayerColor owner, int size){
        super(owner,size);
        setLocation(0,0);
        this.animalType=AnimalType.Elephant;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("楷体", Font.PLAIN, getWidth() / 2);
        g2.setFont(font);
        g2.setColor(owner.getColor());
        g2.drawString("象", getWidth() / 4, getHeight() * 5 / 8); // FIXME: Use library to find the correct offset.
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(owner.getColor());
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
}
