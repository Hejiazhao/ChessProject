package model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class store the real chess information.
 * The Chessboard has 9*7 cells, and each cell has a position for chess
 */
public class Chessboard {
    private final Set<ChessboardPoint> riverCell = new HashSet<>();
    private final Set<ChessboardPoint> BlueTrap = new HashSet<>();
    private final Set<ChessboardPoint> RedTrap = new HashSet<>();
    private final Set<ChessboardPoint[]> AroundRiverCell = new HashSet<>();
    private Cell[][] grid;
    private ChessboardPoint RedDen = new ChessboardPoint(0, 3);
    private ChessboardPoint BlueDen = new ChessboardPoint(8, 3);

    public Chessboard() {
        this.grid =
                new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];//19X19
        initGrid();
        initPieces();
        riverCell.add(new ChessboardPoint(3, 1));
        riverCell.add(new ChessboardPoint(3, 2));
        riverCell.add(new ChessboardPoint(4, 1));
        riverCell.add(new ChessboardPoint(4, 2));
        riverCell.add(new ChessboardPoint(5, 1));
        riverCell.add(new ChessboardPoint(5, 2));

        riverCell.add(new ChessboardPoint(3, 4));
        riverCell.add(new ChessboardPoint(3, 5));
        riverCell.add(new ChessboardPoint(4, 4));
        riverCell.add(new ChessboardPoint(4, 5));
        riverCell.add(new ChessboardPoint(5, 4));
        riverCell.add(new ChessboardPoint(5, 5));

        RedTrap.add(new ChessboardPoint(0, 2));
        RedTrap.add(new ChessboardPoint(1, 3));
        RedTrap.add(new ChessboardPoint(0, 4));

        BlueTrap.add(new ChessboardPoint(8, 2));
        BlueTrap.add(new ChessboardPoint(8, 4));
        BlueTrap.add(new ChessboardPoint(7, 3));

        for (int i = 3; i < 6; i++) {
            ChessboardPoint[] C = {new ChessboardPoint(i, 0), new ChessboardPoint(i, 3)};
            AroundRiverCell.add(C);
            ChessboardPoint[] d = {new ChessboardPoint(i, 6), new ChessboardPoint(i, 3)};
            AroundRiverCell.add(d);
        }
        for (int j = 1; j < 6; j++) {
            if (j == 3) j++;
            ChessboardPoint[] F = {new ChessboardPoint(2, j), new ChessboardPoint(6, j)};
            AroundRiverCell.add(F);
        }
    }

    private void initGrid() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    private void initPieces() {

        grid[6][0].setPiece(new ChessPiece(PlayerColor.BLUE, "Elephant", 8));
        grid[2][6].setPiece(new ChessPiece(PlayerColor.RED, "Elephant", 8));
        grid[6][6].setPiece(new ChessPiece(PlayerColor.BLUE, "Mouse", 1));
        grid[2][0].setPiece(new ChessPiece(PlayerColor.RED, "Mouse", 1));
        grid[7][1].setPiece(new ChessPiece(PlayerColor.BLUE, "Cat", 2));
        grid[1][5].setPiece(new ChessPiece(PlayerColor.RED, "Cat", 2));
        grid[7][5].setPiece(new ChessPiece(PlayerColor.BLUE, "Dog", 3));
        grid[1][1].setPiece(new ChessPiece(PlayerColor.RED, "Dog", 3));
        grid[6][4].setPiece(new ChessPiece(PlayerColor.BLUE, "Leopard", 5));
        grid[2][2].setPiece(new ChessPiece(PlayerColor.RED, "Leopard", 5));
        grid[8][6].setPiece(new ChessPiece(PlayerColor.RED, "Leopard", 5));
        grid[8][0].setPiece(new ChessPiece(PlayerColor.BLUE, "Tiger", 6));
        grid[0][6].setPiece(new ChessPiece(PlayerColor.RED, "Tiger", 6));
        grid[6][2].setPiece(new ChessPiece(PlayerColor.BLUE, "Wolf", 4));
        grid[2][4].setPiece(new ChessPiece(PlayerColor.RED, "Wolf", 4));
        grid[8][6].setPiece(new ChessPiece(PlayerColor.BLUE, "Lion", 7));
        grid[0][0].setPiece(new ChessPiece(PlayerColor.RED, "Lion", 7));
        //在棋格中添加了鼠鼠
    }


    public ChessPiece getChessPieceAt(ChessboardPoint point) {
        return getGridAt(point).getPiece();
    }

    private Cell getGridAt(ChessboardPoint point) {
        return grid[point.getRow()][point.getCol()];
    }

    private int calculateDistance(ChessboardPoint src, ChessboardPoint dest) {
        return Math.abs(src.getRow() - dest.getRow()) + Math.abs(src.getCol() - dest.getCol());
    }

    public ChessPiece removeChessPiece(ChessboardPoint point) {
        ChessPiece chessPiece = getChessPieceAt(point);
        getGridAt(point).removePiece();
        return chessPiece;
    }

    public void setChessPiece(ChessboardPoint point, ChessPiece chessPiece) {
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
        } else {
            setChessPiece(dest, getChessPieceAt(src));
            removeChessPiece(src);
        }
        // 捕捉功能待添加


    }

    public Cell[][] getGrid() {
        return grid;
    }

    public PlayerColor getChessPieceOwner(ChessboardPoint point) {
        if (getChessPieceAt(point) == null) return null;
        else return getGridAt(point).getPiece().getOwner();
    }

    private boolean aroundRiverCell(ChessboardPoint src, ChessboardPoint dest) {
        boolean judge = false;
        if (getChessPieceAt(src).getRank() == 6 || getChessPieceAt(src).getRank() == 7) {
            for (ChessboardPoint[] C : AroundRiverCell) {
                if ((C[0].equals(src) && C[1].equals(dest)) || (C[1].equals(src) && C[0].equals(dest))) {
                    if (C[0].getRow() == C[1].getRow()) {
                        judge = true;
                        for (ChessboardPoint D : riverCell) {
                            if (D.getRow() == C[0].getRow() && getChessPieceAt(D) != null && (((D.getCol() < C[0].getCol()) && (D.getCol() > C[1].getCol())) || ((D.getCol() > C[0].getCol()) && (D.getCol() < C[1].getCol()))))
                                judge = false;
                        }
                    }
                    if (C[0].getCol() == C[1].getCol()) {
                        judge = true;
                        for (ChessboardPoint D : riverCell) {
                            if (D.getCol() == C[0].getCol() && getChessPieceAt(D) != null) judge = false;
                        }
                    }
                }

            }
        }
        return judge;
    }
    //能否跳河判定

    public boolean isValidMove(ChessboardPoint src, ChessboardPoint dest) {

        if (getChessPieceAt(src) == null) {
            return false;
        } else if ((dest.equals(RedDen) && getChessPieceAt(src).getOwner().equals(PlayerColor.RED)) || (dest.equals(BlueDen) && getChessPieceAt(src).getOwner().equals(PlayerColor.BLUE)))
            return false;

            //添加了&&后的判断
        else if ((getChessPieceAt(dest) != null) && !isValidCapture(src, dest)) {
            return false;
        } else if (riverCell.contains(dest) && getChessPieceAt(src).getRank() != 1) return false;
        else if (aroundRiverCell(src, dest)) {
            return true;
        } else if (getChessPieceAt(dest) != null && inTrap(getChessPieceAt(dest)) && calculateDistance(src, dest) == 1)
            return true;
        return calculateDistance(src, dest) == 1;

    }

    public boolean inRiverCell(ChessPiece chessPiece) {
        boolean judge = false;
        for (ChessboardPoint P : this.riverCell) if (Objects.equals(chessPiece, getChessPieceAt(P))) judge = true;
        return judge;
    }

    public boolean inTrap(ChessPiece chessPiece) {
        System.out.println(chessPiece.getOwner());
        boolean judge = false;
        if (chessPiece.getOwner().equals(PlayerColor.BLUE))
            for (ChessboardPoint P : this.RedTrap) {
                if (chessPiece.equals(getChessPieceAt(P))) judge = true;
            }
        else if (chessPiece.getOwner().equals(PlayerColor.RED)) {
            for (ChessboardPoint P : this.BlueTrap) {
                if (chessPiece.equals(getChessPieceAt(P))) judge = true;
            }
        }
        return judge;
    }


    public boolean isValidCapture(ChessboardPoint src, ChessboardPoint dest) {
        if (getChessPieceAt(dest) != null && (!inTrap(getChessPieceAt(dest)) && !inRiverCell(getChessPieceAt(dest)))){
            if (inRiverCell(getChessPieceAt(src))&&getChessPieceAt(src).getRank()==1&&getChessPieceAt(dest).getRank()==8){return false;}
            else return getChessPieceAt(src).canCapture(getChessPieceAt(dest));}
        else if (getChessPieceAt(dest) != null && inTrap(getChessPieceAt(dest))) {
            return true;

        }

        else if (inRiverCell(getChessPieceAt(dest)) && getChessPieceAt(dest).getRank() == 1 && getChessPieceAt(src).getRank() != 1)
            return false;
        else
            return inRiverCell(getChessPieceAt(dest)) && getChessPieceAt(dest).getRank() == 1 && getChessPieceAt(src).getRank() == 1;

        //捕捉功能还没做好
        // TODO:Fix this method；
    }

}
