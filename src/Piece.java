public class Piece {
    private PieceType type;
    private Player color;
    
    public Piece(PieceType type, Player color) {
        this.type = type;
        this.color = color;
    }
    
    public PieceType getType() {
        return type;
    }
    
    public Player getColor() {
        return color;
    }
}
