import java.util.ArrayList;
import java.util.List;

public class AIController {
    public Move getBestMove(BoardState state, int depth) {
        EvaluatedMove evalMove = minimax(state, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        return evalMove.move;
    }
    
    private EvaluatedMove minimax(BoardState state, int depth, int alpha, int beta, boolean maximizing) {
        if (depth == 0 || state.isGameOver()) {
            return new EvaluatedMove(evaluate(state), null);
        }
        List<Move> legalMoves = generateAllMoves(state);
        Move bestMove = null;
        int bestEval = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        
        for (Move move : legalMoves) {
            BoardState newState = state.applyMove(move);
            int eval = minimax(newState, depth - 1, alpha, beta, !maximizing).eval;
            if (maximizing && eval > bestEval) {
                bestEval = eval;
                bestMove = move;
                alpha = Math.max(alpha, eval);
            } else if (!maximizing && eval < bestEval) {
                bestEval = eval;
                bestMove = move;
                beta = Math.min(beta, eval);
            }
            if (beta <= alpha) break;
        }
        return new EvaluatedMove(bestEval, bestMove);
    }
    
    private int evaluate(BoardState state) {
        // A dummy evaluation: return 0 for now
        return 0;
    }
    
    // Minimal stub: Iterate board and add moves for pieces of current player.
    private List<Move> generateAllMoves(BoardState state) {
        List<Move> moves = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = state.getPiece(row, col);
                if (piece != null && piece.getColor() == state.getCurrentPlayer()) {
                    // Stub: generate simple moves (for example, one-square in any direction)
                    // You'll later add rules per piece
                    int[][] directions = { {1,0}, {-1,0}, {0,1}, {0,-1} };
                    for (int[] d : directions) {
                        int newRow = row + d[0];
                        int newCol = col + d[1];
                        if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                            moves.add(new Move(row, col, newRow, newCol));
                        }
                    }
                }
            }
        }
        return moves;
    }
    
    private static class EvaluatedMove {
        int eval;
        Move move;
        EvaluatedMove(int eval, Move move) {
            this.eval = eval;
            this.move = move;
        }
    }
}
