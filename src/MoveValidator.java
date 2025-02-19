public class MoveValidator {
    public static boolean isValidMove(BoardState state, Move move) {
        if (move.getFromRow() == move.getToRow() && move.getFromCol() == move.getToCol()) return false;
        Piece piece = state.getPiece(move.getFromRow(), move.getFromCol());
        if (piece == null) return false;
        // Basic example for pawn and knight; extend rules as needed.
        switch (piece.getType()) {
            case PAWN:
                return validatePawnMove(state, move, piece.getColor());
            case KNIGHT:
                return validateKnightMove(move);
            default:
                // ...other pieces...
                return true;
        }
    }
    
    private static boolean validatePawnMove(BoardState state, Move move, Player color) {
        // Simplified pawn move validation
        int direction = (color == Player.WHITE) ? -1 : 1;
        if (move.getToRow() - move.getFromRow() == direction) return true;
        return false;
    }
    
    private static boolean validateKnightMove(Move move) {
        int dRow = Math.abs(move.getToRow() - move.getFromRow());
        int dCol = Math.abs(move.getToCol() - move.getFromCol());
        return (dRow == 2 && dCol == 1) || (dRow == 1 && dCol == 2);
    }
}
