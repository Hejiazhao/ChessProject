import controller.GameController;
import model.Chessboard;
import view.ChessGameFrame;
import view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);
            GameController gameController = new GameController(mainFrame.getChessboardComponent(), new Chessboard());
            mainFrame.setVisible(true);
            mainFrame.addButton(gameController);
            MainFrame mainFrame1 = new MainFrame(1100, 810);
            mainFrame1.setVisible(true);
            mainFrame1.addReadButton(gameController);
            mainFrame1.addStartButton(gameController);
        });
    }
}
