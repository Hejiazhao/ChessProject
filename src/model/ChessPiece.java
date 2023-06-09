package model;


public class ChessPiece {
    // the owner of the chess
    private PlayerColor owner;

    // Elephant? Cat? Dog? ...
    private String name;
    private int rank;

    public ChessPiece(PlayerColor owner, String name, int rank) {
        this.owner = owner;
        this.name = name;
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }


    public boolean canCapture(ChessPiece target) {
        // TODO: Finish this method!
        if (this.rank == 1 && target.rank == 8 && this.getOwner() != target.getOwner()) return true;
        else if (target.rank == 1 && this.rank == 8 && this.getOwner() != target.getOwner()) return false;
        else return (this.rank >= target.rank) && (this.getOwner() != target.getOwner());
        //已完成
    }

    public String getName() {
        return name;
    }

    public PlayerColor getOwner() {
        return owner;
    }
}
