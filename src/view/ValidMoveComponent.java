package view;

import javax.swing.*;
import java.awt.*;

public class ValidMoveComponent extends JComponent {
    public ValidMoveComponent(Point chessboardPoint) {
        setSize(72, 72);
        setLocation(chessboardPoint);
        setVisible(true);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.YELLOW);
        g.fillRect(1, 1, this.getWidth()-1, this.getHeight()-1);
        }
    }

