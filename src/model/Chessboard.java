package model;

import view.ChessboardComponent;
import view.ChessboardComponent;
import java.util.HashSet;
import java.util.Set;

/**
 * This class store the real chess information.
 * The Chessboard has 9*7 cells, and each cell has a position for chess
 */
public class Chessboard {
    private Cell[][] grid;
    private final Set<ChessboardPoint> riverCell = new HashSet<>();


    public Chessboard() {
        this.grid =
                new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];//19X19
        initGrid();
        initPieces();
        riverCell.add(new ChessboardPoint(3,1));
        riverCell.add(new ChessboardPoint(3,2));
        riverCell.add(new ChessboardPoint(4,1));
        riverCell.add(new ChessboardPoint(4,2));
        riverCell.add(new ChessboardPoint(5,1));
        riverCell.add(new ChessboardPoint(5,2));

        riverCell.add(new ChessboardPoint(3,4));
        riverCell.add(new ChessboardPoint(3,5));
        riverCell.add(new ChessboardPoint(4,4));
        riverCell.add(new ChessboardPoint(4,5));
        riverCell.add(new ChessboardPoint(5,4));
        riverCell.add(new ChessboardPoint(5,5));
    }

    private void initGrid() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    private void initPieces() {
        grid[0][0].setPiece(new ChessPiece(PlayerColor.BLUE, "Elephant",8));
        grid[8][6].setPiece(new ChessPiece(PlayerColor.RED, "Elephant",8));
        grid[0][1].setPiece(new ChessPiece(PlayerColor.BLUE,"Mouse",1));
        grid[8][5].setPiece(new ChessPiece(PlayerColor.RED,"Mouse",1));
        //在棋格中添加了鼠鼠
    }



   private ChessPiece getChessPieceAt(ChessboardPoint point) {
        return getGridAt(point).getPiece();
    }

    private Cell getGridAt(ChessboardPoint point) {
        return grid[point.getRow()][point.getCol()];
    }

    private int calculateDistance(ChessboardPoint src, ChessboardPoint dest) {
        return Math.abs(src.getRow() - dest.getRow()) + Math.abs(src.getCol() - dest.getCol());
    }

    private ChessPiece removeChessPiece(ChessboardPoint point) {
        ChessPiece chessPiece = getChessPieceAt(point);
        getGridAt(point).removePiece();
        return chessPiece;
    }

    private void setChessPiece(ChessboardPoint point, ChessPiece chessPiece) {
        getGridAt(point).setPiece(chessPiece);
    }

    public void moveChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidMove(src, dest)) {
            throw new IllegalArgumentException("Illegal chess move!");
        }
        setChessPiece(dest, removeChessPiece(src));
    }

    public void captureChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidCapture(src, dest)) {
            throw new IllegalArgumentException("Illegal chess capture!");
        }
        else {
            setChessPiece(dest,getChessPieceAt(src));
            removeChessPiece(src);
        }
        // 捕捉功能待添加
        // TODO: Finish the method.

    }

    public Cell[][] getGrid() {
        return grid;
    }
    public PlayerColor getChessPieceOwner(ChessboardPoint point) {
        return getGridAt(point).getPiece().getOwner();
    }

    public boolean isValidMove(ChessboardPoint src, ChessboardPoint dest) {
        if (getChessPieceAt(src) == null ) {
            return false;
        }
        //添加了&&后的判断
        else if ((getChessPieceAt(dest) != null)&&!isValidCapture(src,dest)){
            return false;
        }

        return calculateDistance(src, dest) == 1;
    }
    public boolean inRiverCell(ChessPiece chessPiece){
        boolean judge=false;
        for (ChessboardPoint P:this.riverCell ){
            if (chessPiece.equals(getChessPieceAt(P)))judge=true;
        }
        return judge;
    }


    public boolean isValidCapture(ChessboardPoint src, ChessboardPoint dest) {
      if (getChessPieceAt(dest)!=null&&(!inRiverCell(getChessPieceAt(dest))||getChessPieceAt(dest).getRank()!=1)) return getChessPieceAt(src).canCapture(getChessPieceAt(dest));
      else if (inRiverCell(getChessPieceAt(dest))&&getChessPieceAt(dest).getRank()==1&&getChessPieceAt(src).getRank()!=1)return false;
      else if (inRiverCell(getChessPieceAt(dest))&&getChessPieceAt(dest).getRank()==1&&getChessPieceAt(src).getRank()==1)return true;
      else return true;
        //捕捉功能还没做好
        // TODO:Fix this method；
    }

}
