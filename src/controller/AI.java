package controller;

import model.ChessboardPoint;
import view.ChessboardComponent;

import java.util.ArrayList;

public class AI {
    private GameController gameController;
    ArrayList<ChessboardPoint>RedChess=new ArrayList<>();
    ArrayList<ChessboardPoint>BlueChess=new ArrayList<>();
    private ChessboardComponent chessboardComponent;
    public AI(GameController gameController){
        this.gameController=gameController;
    }


}
