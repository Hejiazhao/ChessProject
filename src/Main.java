import controller.GameController;
import model.Chessboard;
import view.ChessGameFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);
            GameController gameController = new GameController(mainFrame.getChessboardComponent(), new Chessboard());
            mainFrame.setVisible(true);
            mainFrame.addSaveButton(gameController);
            mainFrame.addUndoButton(gameController);
            mainFrame.addRestartButton(gameController);
            mainFrame.addMusicButton(gameController);

        });
    }
}
