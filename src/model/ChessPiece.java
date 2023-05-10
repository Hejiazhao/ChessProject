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
    public int getRank(){return rank;}


    public boolean canCapture(ChessPiece target) {
        // TODO: Finish this method!
        if (this.rank==1&&target.rank==8 )   return true;
        else return this.rank>=target.rank;}

    public String getName() {
        return name;
    }

    public PlayerColor getOwner() {
        return owner;
    }
}
