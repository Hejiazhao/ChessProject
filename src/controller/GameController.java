package controller;


import listener.GameListener;
import model.*;
import view.CellComponent;
import view.AnimalChessComponent;
import view.ChessboardComponent;

import javax.swing.*;
import java.util.Objects;



/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece()]
 *
*/
public class GameController implements GameListener {


    private Chessboard model;
    public Chessboard getModel(){return model;}
    private ChessboardComponent view;
    private PlayerColor currentPlayer;

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;
    private ChessboardPoint RedDen=new ChessboardPoint(0,3);
    private ChessboardPoint BlueDen=new ChessboardPoint(8,3);
    private int ValidRedChess=8;
    private int ValidBlueChess=8;

    public GameController(ChessboardComponent view, Chessboard model) {
        this.view = view;
        this.model = model;
        this.currentPlayer = PlayerColor.BLUE;
        view.registerController(this);
        initialize();
        view.initiateChessComponent(model);
        view.repaint();
    }



    private void initialize() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {

            }
        }
    }

    // after a valid move swap the player
    private void swapColor() {
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
    }

    private PlayerColor win() {
        // TODO: Check the board if there is a winner
        if (model.getChessPieceAt(RedDen)!=null&&Objects.equals(PlayerColor.BLUE, model.getChessPieceAt(RedDen).getOwner())){System.out.println("Blue");return PlayerColor.BLUE;}
        else if (model.getChessPieceAt(BlueDen)!=null&&Objects.equals(PlayerColor.RED,model.getChessPieceAt(BlueDen).getOwner())) return PlayerColor.RED;
        else if (ValidBlueChess==0) return PlayerColor.RED;
        else if (ValidRedChess==0) return PlayerColor.BLUE;
        else return null;

    }


    public  void Save (Cell[][] grid,String name){

        }


    // click an empty cell
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
        if (selectedPoint != null && model.isValidMove(selectedPoint, point)) {
            model.moveChessPiece(selectedPoint, point);
            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            selectedPoint = null;
            swapColor();
            view.repaint();
            ifWin();
            // TODO: if the chess enter Dens or Traps and so on


        }

    }
    private void ifWin(){
        String RedWin="红方获胜";
        String BlueWin="蓝方获胜";
        if (Objects.equals(win(), PlayerColor.BLUE)){
            JOptionPane.showMessageDialog(null, BlueWin);this.currentPlayer = PlayerColor.BLUE;
           }
        else if (Objects.equals(win(), PlayerColor.RED)){
            JOptionPane.showMessageDialog(null, RedWin);this.currentPlayer = PlayerColor.BLUE;

        }
    }
    private void restartGame(){
        Chessboard chessboard=new Chessboard();
        view.registerController(this);
        initialize();
        view.initiateChessComponent(chessboard);
        view.repaint();
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

            }
        }
        else if (selectedPoint.equals(point)) {

            selectedPoint = null;
            component.setSelected(false);
            component.repaint();
        }
        else if (model.isValidCapture(selectedPoint,point)&&model.getChessPieceOwner(selectedPoint).equals(currentPlayer)&& model.isValidMove(selectedPoint,point)){
            model.captureChessPiece(selectedPoint,point);
            if (model.getChessPieceOwner(point).equals(PlayerColor.BLUE))ValidBlueChess--;
            else if (model.getChessPieceOwner(point).equals(PlayerColor.RED)) ValidRedChess--;
            view.removeChessComponentAtGrid(point);
            AnimalChessComponent component1=view.removeChessComponentAtGrid(selectedPoint);
            view.setChessComponentAtGrid(point,component1);
            component1.setSelected(false);
            selectedPoint=null;
            ifWin();
            swapColor();
            view.repaint();

        }

        // TODO: Implement capture function；
        // 正在debug
        //debug完成

    }
}
