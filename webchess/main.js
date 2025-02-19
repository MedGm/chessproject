document.addEventListener('DOMContentLoaded', function() {
    var board = Chessboard('board', {
        draggable: true,
        position: 'start',
        onDrop: handleDrop,
        onSnapEnd: onSnapEnd,
        pieceTheme: '/chess/img/pieces/wikipedia/{piece}.png'
    });

    var game = new Chess();
    let isComputerTurn = false;

    function handleDrop(source, target) {
        const move = game.move({
            from: source,
            to: target,
            promotion: 'q'
        });

        if (move === null) return 'snapback';

        updateStatus();
        
        if (document.getElementById('gameMode').value === 'computer' && !game.game_over()) {
            setTimeout(makeComputerMove, 250);
        }
    }

    function makeComputerMove() {
        const difficulty = parseInt(document.getElementById('difficulty').value);
        const move = findBestMove(game, difficulty);
        
        if (move) {
            game.move(move);
            board.position(game.fen());
            updateStatus();
        }
    }

    function onSnapEnd() {
        board.position(game.fen());
    }

    function updateStatus() {
        let status = game.turn() === 'w' ? 'White to move' : 'Black to move';
        const gameStatus = document.getElementById('gameStatus');
        
        if (game.in_check()) {
            status += ' (Check!)';
            gameStatus.style.background = '#e53e3e';
        } else if (game.in_checkmate()) {
            status = 'Game Over: ' + (game.turn() === 'w' ? 'Black' : 'White') + ' wins!';
            gameStatus.style.background = '#48bb78';
        } else if (game.in_draw()) {
            status = 'Game Over: Draw';
            gameStatus.style.background = '#4a5568';
        } else {
            gameStatus.style.background = '';
        }

        gameStatus.textContent = status;
        document.getElementById('status').textContent = status;
        document.getElementById('fen').textContent = game.fen();
        document.getElementById('pgn').textContent = game.pgn() || 'No moves yet';
    }

    document.getElementById('clearBtn').addEventListener('click', function() {
        board.clear();
        game.clear();
        updateStatus();
    });

    document.getElementById('startBtn').addEventListener('click', function() {
        board.start();
        game.reset();
        updateStatus();
    });

    document.getElementById('flipBtn').addEventListener('click', function() {
        board.flip();
    });

    window.addEventListener('resize', board.resize);

    document.getElementById('gameMode').addEventListener('change', function() {
        board.start();
        game.reset();
        updateStatus();
        
        if (this.value === 'computer') {
            document.getElementById('difficulty').style.display = 'inline-block';
        } else {
            document.getElementById('difficulty').style.display = 'none';
        }
    });

    updateStatus();
});
