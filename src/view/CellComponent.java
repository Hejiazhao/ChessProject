package view;

import javax.swing.*;
import java.awt.*;

/**
 * This is the equivalent of the Cell class,
 * but this class only cares how to draw Cells on ChessboardComponent
 */

public class CellComponent extends JPanel {
    private String FileName;
    private boolean seeUI;
    private int size;

    public CellComponent(String FileName, Point location, int size) {
        setLayout(new GridLayout(1, 1));
        setLocation(location);
        setSize(size, size);
        this.size = size;
        setPreferredSize(new Dimension(size, size));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        this.FileName = FileName;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        String File = null;
        if (seeUI) {
            File = "resource/绿色动图.gif";

        } else {

            switch (FileName) {
                case "Green" -> File = "resource/星空.jpg";
                case "BlueTrap" -> File = "resource/红色陷阱.jpg";
                case "RedTrap" -> File = "resource/蓝色陷阱.jpg";
                case "RedDen", "BlueDen" -> File = "resource/兽穴.png";
                case "River" -> File = "resource/深海.jpg";
            }
        }
        ImageIcon icon = new ImageIcon(File);
        g.setColor(new Color(0, 0, 0));
        g.drawImage(icon.getImage(), 0, 0, size, size, null);
    }

    public void setSeeUI(boolean seeUI) {
        this.seeUI = seeUI;
    }
}
