const pieceValues = {
    'p': 100,
    'n': 320,
    'b': 330,
    'r': 500,
    'q': 900,
    'k': 20000
};

// Piece-Square Tables for positional evaluation
const pawnTable = [
    0,  0,  0,  0,  0,  0,  0,  0,
    50, 50, 50, 50, 50, 50, 50, 50,
    10, 10, 20, 30, 30, 20, 10, 10,
    5,  5, 10, 25, 25, 10,  5,  5,
    0,  0,  0, 20, 20,  0,  0,  0,
    5, -5,-10,  0,  0,-10, -5,  5,
    5, 10, 10,-20,-20, 10, 10,  5,
    0,  0,  0,  0,  0,  0,  0,  0
];

function evaluateBoard(game) {
    let totalEvaluation = 0;
    
    // Get position as FEN and parse it
    const fen = game.fen().split(' ')[0];
    const position = fen.split('/');
    
    for (let i = 0; i < 8; i++) {
        let j = 0;
        for (const char of position[i]) {
            if (isNaN(char)) {
                // It's a piece
                let piece = char.toLowerCase();
                // Basic material value
                let value = pieceValues[piece];
                
                // Position bonus for pawns
                if (piece === 'p') {
                    const index = char === 'P' ? ((7-i) * 8 + j) : (i * 8 + j);
                    value += pawnTable[index];
                }
                
                totalEvaluation += char === char.toUpperCase() ? value : -value;
                j++;
            } else {
                // Skip empty squares
                j += parseInt(char);
            }
        }
    }
    
    return totalEvaluation;
}

function minimax(game, depth, alpha, beta, isMaximizingPlayer) {
    if (depth === 0 || game.game_over()) {
        return evaluateBoard(game);
    }

    const moves = game.moves();

    if (isMaximizingPlayer) {
        let maxEval = -Infinity;
        for (const move of moves) {
            game.move(move);
            const evaluation = minimax(game, depth - 1, alpha, beta, false);
            game.undo();
            maxEval = Math.max(maxEval, evaluation);
            alpha = Math.max(alpha, evaluation);
            if (beta <= alpha) break;
        }
        return maxEval;
    } else {
        let minEval = Infinity;
        for (const move of moves) {
            game.move(move);
            const evaluation = minimax(game, depth - 1, alpha, beta, true);
            game.undo();
            minEval = Math.min(minEval, evaluation);
            beta = Math.min(beta, evaluation);
            if (beta <= alpha) break;
        }
        return minEval;
    }
}

function findBestMove(game, depth = 3) {
    let bestMove = null;
    let bestEvaluation = -Infinity;
    const moves = game.moves();
    
    for (const move of moves) {
        game.move(move);
        const evaluation = minimax(game, depth - 1, -Infinity, Infinity, false);
        game.undo();
        
        if (evaluation > bestEvaluation) {
            bestEvaluation = evaluation;
            bestMove = move;
        }
    }
    
    return bestMove;
}
