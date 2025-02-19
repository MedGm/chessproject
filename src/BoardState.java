public class BoardState {
    private Piece[][] board;
    private Player currentPlayer;

    public BoardState() {
        board = new Piece[8][8];
        initializeBoard();
        currentPlayer = Player.WHITE;
    }
    
    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Piece(PieceType.PAWN, Player.BLACK);
            board[6][i] = new Piece(PieceType.PAWN, Player.WHITE);
        }
        board[0][0] = new Piece(PieceType.ROOK, Player.BLACK);
        board[0][7] = new Piece(PieceType.ROOK, Player.BLACK);
        board[7][0] = new Piece(PieceType.ROOK, Player.WHITE);
        board[7][7] = new Piece(PieceType.ROOK, Player.WHITE);
        board[0][1] = new Piece(PieceType.KNIGHT, Player.BLACK);
        board[0][6] = new Piece(PieceType.KNIGHT, Player.BLACK);
        board[7][1] = new Piece(PieceType.KNIGHT, Player.WHITE);
        board[7][6] = new Piece(PieceType.KNIGHT, Player.WHITE);
        board[0][2] = new Piece(PieceType.BISHOP, Player.BLACK);
        board[0][5] = new Piece(PieceType.BISHOP, Player.BLACK);
        board[7][2] = new Piece(PieceType.BISHOP, Player.WHITE);
        board[7][5] = new Piece(PieceType.BISHOP, Player.WHITE);
        board[0][3] = new Piece(PieceType.QUEEN, Player.BLACK);
        board[7][3] = new Piece(PieceType.QUEEN, Player.WHITE);
        board[0][4] = new Piece(PieceType.KING, Player.BLACK);
        board[7][4] = new Piece(PieceType.KING, Player.WHITE);
        //additional setup (castling rights, en passant, etc.)
    }
    
    // Return a new BoardState after applying a valid move.
    public BoardState applyMove(Move move) {
        BoardState newState = new BoardState();
        newState.board = copyBoard();
        newState.board[move.getToRow()][move.getToCol()] = newState.board[move.getFromRow()][move.getFromCol()];
        newState.board[move.getFromRow()][move.getFromCol()] = null;
        newState.currentPlayer = (this.currentPlayer == Player.WHITE) ? Player.BLACK : Player.WHITE;
        // ...additional state updates (castling rights, en passant, etc.)...
        return newState;
    }
    
    private Piece[][] copyBoard() {
        Piece[][] copy = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 8);
        }
        return copy;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    public boolean isGameOver() {
        // ...stub logic...
        return false;
    }
    
    public Piece getPiece(int row, int col) {
        return board[row][col];
    }
}
