import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// ...import other classes as needed (BoardState, Move, MoveValidator, AIController, Player)...
public class Main extends Application {
    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 60;
    
    // Game state and AI controller
    private BoardState boardState = new BoardState();
    private AIController aiController = new AIController();
    private GridPane chessboard; // now stored as a field
    
    // For drag-and-drop movement
    private Text selectedPiece = null;
    private int selectedRow = -1, selectedCol = -1;
    
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        chessboard = initChessboard();
        
        Button aiButton = new Button("AI Move");
        aiButton.setOnAction(e -> processAIMove());
        
        root.setCenter(chessboard);
        root.setBottom(aiButton);
        BorderPane.setAlignment(aiButton, Pos.CENTER);
        
        Scene scene = new Scene(root, SQUARE_SIZE * BOARD_SIZE, SQUARE_SIZE * BOARD_SIZE + 40);
        scene.getStylesheets().add(getClass().getResource("/ressources/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Chess");
        stage.show();
    }
    
    private GridPane initChessboard() {
        GridPane board = new GridPane();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Color squareColor = (row + col) % 2 == 0 ? Color.valueOf("#f0d9b5") : Color.valueOf("#b58863");
                Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE, squareColor);
                square.getStyleClass().add("square");
                
                // Use boardState to get initial piece, otherwise empty string.
                Text piece = getTextForState(boardState.getPiece(row, col));
                StackPane cell = new StackPane();
                cell.setAlignment(Pos.CENTER);
                cell.getChildren().addAll(square, piece);
                
                final int r = row, c = col;
                piece.setOnMousePressed(e -> handlePieceSelection(piece, r, c));
                cell.setOnMouseReleased(e -> handlePieceDrop(r, c));
                
                board.add(cell, col, row);
            }
        }
        return board;
    }
    
    private void handlePieceSelection(Text piece, int row, int col) {
        if (!piece.getText().isEmpty()) {
            selectedPiece = piece;
            selectedRow = row;
            selectedCol = col;
            System.out.println("Selected piece at (" + row + ", " + col + ")");
        }
    }
    
    private void handlePieceDrop(int targetRow, int targetCol) {
        if (selectedPiece != null) {
            movePiece(selectedRow, selectedCol, targetRow, targetCol);
            selectedPiece = null;
        }
    }
    
    private void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Move move = new Move(fromRow, fromCol, toRow, toCol);
        if (MoveValidator.isValidMove(boardState, move)) {
            boardState = boardState.applyMove(move);
            redrawBoard();
            if (boardState.getCurrentPlayer() == Player.BLACK) {
                processAIMove();
            }
        } else {
            System.out.println("Invalid move!");
        }
    }
    
    private void processAIMove() {
        System.out.println("Processing AI move...");
        Move aiMove = aiController.getBestMove(boardState, 3);
        if (aiMove != null) {
            boardState = boardState.applyMove(aiMove);
            redrawBoard();
        }
    }
    
    // Redraw board updating each cell from boardState
    private void redrawBoard() {
        // Iterate over grid cells and update the Text node in each StackPane
        for (var node : chessboard.getChildren()) {
            Integer col = GridPane.getColumnIndex(node);
            Integer row = GridPane.getRowIndex(node);
            if (col == null) col = 0;
            if (row == null) row = 0;
            StackPane cell = (StackPane) node;
            // Assuming second child is the Text node.
            Text text = (Text) cell.getChildren().get(1);
            text.setText(getTextForState(boardState.getPiece(row, col)).getText());
        }
        System.out.println("Board redrawn.");
    }
    
    // Convert Piece to corresponding symbol; empty if null.
    private Text getTextForState(Piece piece) {
        if (piece == null) return new Text("");
        String symbol = switch (piece.getType()) {
            case PAWN -> (piece.getColor() == Player.WHITE) ? "♙" : "♟";
            case ROOK -> (piece.getColor() == Player.WHITE) ? "♖" : "♜";
            case KNIGHT -> (piece.getColor() == Player.WHITE) ? "♘" : "♞";
            case BISHOP -> (piece.getColor() == Player.WHITE) ? "♗" : "♝";
            case QUEEN -> (piece.getColor() == Player.WHITE) ? "♕" : "♛";
            case KING -> (piece.getColor() == Player.WHITE) ? "♔" : "♚";
            default -> "";
        };
        Text text = new Text(symbol);
        text.setStyle("-fx-font-size: 40; -fx-font-family: 'Segoe UI Symbol';");
        text.getStyleClass().add(piece.getColor() == Player.WHITE ? "piece-white" : "piece-black");
        return text;
    }
    
    // ...existing getPieceForPosition, getBackRankPiece, createPiece methods can be removed or repurposed...
    
    public static void main(String[] args) {
        launch(args);
    }
}