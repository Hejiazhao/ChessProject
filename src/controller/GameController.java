package controller;


import listener.GameListener;
import model.*;
import view.AnimalChessComponent;
import view.CellComponent;
import view.ChessboardComponent;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;


/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece()]
 */
public class GameController implements GameListener {


    private Chessboard model;

    public Chessboard getModel() {
        return model;
    }

    private ChessboardComponent view;
    private PlayerColor currentPlayer;

    public void setSelectedPoint(ChessboardPoint selectedPoint) {
        this.selectedPoint = selectedPoint;
    }

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;
    private ChessboardPoint RedDen = new ChessboardPoint(0, 3);
    private ChessboardPoint BlueDen = new ChessboardPoint(8, 3);
    private int ValidRedChess = 8;
    private int ValidBlueChess = 8;
    private ChessboardPoint BeforeMove;
    private ChessboardPoint AfterMove;
    private ChessPiece ChessBeforeMove;
    private ChessPiece ChessAfterMove;
    private AnimalChessComponent ateAnimal;
    private boolean canUndo = false;
    private boolean PlayEffect = false;

    private Clip clip;

    public AnimalChessComponent getSelectedComponent() {
        return selectedComponent;
    }

    public void setSelectedComponent(AnimalChessComponent selectedComponent) {
        this.selectedComponent = selectedComponent;
    }

    private AnimalChessComponent selectedComponent;

    public GameController(ChessboardComponent view, Chessboard model) {
        this.view = view;
        this.model = model;
        this.currentPlayer = PlayerColor.BLUE;
        view.registerController(this);
        view.initiateChessComponent(model);
        initialize();
        view.repaint();
    }

    public void PlayEat() {
        if (PlayEffect) {
            try {
                if (clip == null || !clip.isOpen()) {
                    File newfile = new File("resource/音效2.wav");
                    PlayInBoard(newfile);
                    clip = null;
                } else {
                    clip.stop();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void PlayInBoard(File newfile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        InputStream is = new BufferedInputStream(new FileInputStream(newfile));
        AudioInputStream ais = AudioSystem.getAudioInputStream(is);
        clip = AudioSystem.getClip();
        clip.open(ais);
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                clip.close();
                clip = null;
            }
        });
        clip.start();
    }

    public boolean isPlayEffect() {
        return PlayEffect;
    }

    public void setPlayEffect(boolean playEffect) {
        PlayEffect = playEffect;
    }

    public void PlayWin() {
        if (PlayEffect) {
            try {
                if (clip == null || !clip.isOpen()) {
                    File newfile = new File("resource/音效1.wav");
                    PlayInBoard(newfile);
                    clip = null;
                } else {
                    clip.stop();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public ChessboardPoint getBeforeMove() {
        return BeforeMove;
    }

    public ChessboardPoint getAfterMove() {
        return AfterMove;
    }

    public ChessPiece getChessBeforeMove() {
        return ChessBeforeMove;
    }

    public ChessPiece getChessAfterMove() {
        return ChessAfterMove;
    }

    public AnimalChessComponent getAteAnimal() {
        return ateAnimal;
    }

    public void setModel(Chessboard model) {
        this.model = model;
    }

    public void setView(ChessboardComponent view) {
        this.view = view;
    }

    public ChessboardComponent getView() {
        return view;
    }


    public void initialize() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                this.view.getGridComponents()[i][j].repaint();
            }
        }
    }

    // after a valid move swap the player
    private void swapColor() {
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
    }

    private PlayerColor win() {
        // TODO: Check the board if there is a winner
        if (model.getChessPieceAt(RedDen) != null && Objects.equals(PlayerColor.BLUE, model.getChessPieceAt(RedDen).getOwner())) {
            System.out.println("Blue");
            return PlayerColor.BLUE;
        } else if (model.getChessPieceAt(BlueDen) != null && Objects.equals(PlayerColor.RED, model.getChessPieceAt(BlueDen).getOwner()))
            return PlayerColor.RED;
        else if (ValidBlueChess == 0) return PlayerColor.RED;
        else if (ValidRedChess == 0) return PlayerColor.BLUE;
        else return null;

    }


    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getValidRedChess() {
        return ValidRedChess;
    }

    public int getValidBlueChess() {
        return ValidBlueChess;
    }


    // click an empty cell
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
        if (selectedPoint != null && model.isValidMove(selectedPoint, point)) {
            beforeMove(selectedPoint, point);
            model.moveChessPiece(selectedPoint, point);
            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            selectedPoint = null;
            swapColor();

            view.repaint();
            ifWin();
            // TODO: if the chess enter Dens or Traps and so on
        }
    }

    private void ifWin() {
        String RedWin = "红方获胜";
        String BlueWin = "蓝方获胜";
        if (Objects.equals(win(), PlayerColor.BLUE)) {
            JOptionPane.showMessageDialog(null, BlueWin);
            restartGame();
            PlayWin();
        } else if (Objects.equals(win(), PlayerColor.RED)) {
            JOptionPane.showMessageDialog(null, RedWin);
            restartGame();
            PlayWin();
        }
    }

    public void restartGame() {
        this.currentPlayer = PlayerColor.BLUE;
        Chessboard chessboard = new Chessboard();
        this.model = chessboard;
        this.view.initiateChessComponent(chessboard);
        this.view.repaint();
        initialize();
        canUndo = false;
    }

    public void UndoMove() {
        if (canUndo) {
            canUndo = false;
            if (ateAnimal != null) {
                AnimalChessComponent animalChessComponent1 = view.removeChessComponentAtGrid(AfterMove);
                view.setChessComponentAtGrid(BeforeMove, animalChessComponent1);
                view.setChessComponentAtGrid(AfterMove, ateAnimal);
                model.setChessPiece(AfterMove, ChessAfterMove);
                model.setChessPiece(BeforeMove, ChessBeforeMove);
                animalChessComponent1.setSelected(false);
                switch (ateAnimal.getOwner()) {
                    case BLUE -> ValidBlueChess++;
                    case RED -> ValidRedChess++;
                }
            } else {
                model.moveChessPiece(AfterMove, BeforeMove);
                view.setChessComponentAtGrid(BeforeMove, view.removeChessComponentAtGrid(AfterMove));
            }
            swapColor();
            selectedPoint = null;
            view.repaint();
        } else JOptionPane.showMessageDialog(null, "现在不能悔棋！");
    }

    private void beforeMove(ChessboardPoint selectedPoint, ChessboardPoint point) {
        canUndo = true;
        BeforeMove = selectedPoint;
        AfterMove = point;
        ChessBeforeMove = model.getChessPieceAt(selectedPoint);
        ChessAfterMove = model.getChessPieceAt(point);
        if (model.getChessPieceAt(point) == null) ateAnimal = null;
        view.repaint();
    }

    public ArrayList<ChessboardPoint> showValidMove() {
        ArrayList<ChessboardPoint> chessboardPointArrayList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                ChessboardPoint point = new ChessboardPoint(i, j);
                if (model.getChessPieceAt(point) == null && selectedPoint != null && model.isValidMove(selectedPoint, point)) {
                    chessboardPointArrayList.add(point);
                } else if (model.getChessPieceAt(point) != null && model.isValidCapture(selectedPoint, point) && model.getChessPieceOwner(selectedPoint).equals(currentPlayer) && model.isValidMove(selectedPoint, point)) {
                    chessboardPointArrayList.add(point);
                }
            }
        }
        return chessboardPointArrayList;

    }


    // click a cell with a chess
    @Override
    public void onPlayerClickChessPiece(ChessboardPoint point, AnimalChessComponent component) {
        if (selectedPoint == null) {
            if (currentPlayer.equals(model.getChessPieceOwner(point))) {
                selectedPoint = point;
                ifWin();
                component.setSelected(true);
                component.repaint();
                setSelectedComponent(component);
            }
        } else if (!selectedPoint.equals(point) && currentPlayer.equals(model.getChessPieceOwner(point))) {
            selectedPoint = point;
            getSelectedComponent().setSelected(false);
            component.setSelected(true);
            getSelectedComponent().repaint();
            component.repaint();
            setSelectedComponent(component);
        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            component.setSelected(false);
            component.repaint();
        } else if (model.isValidCapture(selectedPoint, point) && model.getChessPieceOwner(selectedPoint).equals(currentPlayer) && model.isValidMove(selectedPoint, point)) {
            beforeMove(selectedPoint, point);
            model.captureChessPiece(selectedPoint, point);
            if (model.getChessPieceOwner(point).equals(PlayerColor.BLUE)) ValidBlueChess--;
            else if (model.getChessPieceOwner(point).equals(PlayerColor.RED)) ValidRedChess--;
            ateAnimal = view.removeChessComponentAtGrid(point);
            AnimalChessComponent component1 = view.removeChessComponentAtGrid(selectedPoint);
            view.setChessComponentAtGrid(point, component1);
            component1.setSelected(false);
            selectedPoint = null;
            PlayEat();
            ifWin();
            swapColor();
            view.repaint();

        }

        // TODO: Implement capture function；
        // 正在debug
        //debug完成

    }
}
