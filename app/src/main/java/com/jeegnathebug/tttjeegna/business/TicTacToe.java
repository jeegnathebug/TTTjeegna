package com.jeegnathebug.tttjeegna.business;

/**
 * Created by jeegna on 19/09/16.
 */
public class TicTacToe {

    private int boardSize = 9;
    private int[] board;

    private boolean isPlayer1Turn;
    private GameMode gameMode;

    private int player1Score;
    private int player2Score;
    private int ties;

    /**
     * Contructs a new TicTacToe game with the given game mode
     *
     * @param gameMode The type of game that should be played
     */
    public TicTacToe(GameMode gameMode) {
        isPlayer1Turn = true;
        setGameMode(gameMode);

        // Start a new game
        newGame();
    }

    /**
     * Resets the board and starts a new game
     */
    public void newGame() {
        restartGame();
    }

    /**
     * Puts a marker on the given position as being played, and disallows that position from being played again
     *
     * @param position The position on the tictactoe grid to be played
     */
    public void play(int position) {
        // Only play if block is empty
        if (board[position] == 0) {
            // Set marker to 1 or 2 based on which player's turn it is
            if (isPlayer1Turn) {
                board[position] = 1;
            } else {
                board[position] = 2;
            }

            // Check if current player won
            if (checkWin()) {
                displayWin();
                endGame();
            } else {
                // Change turn
                changeTurn();
            }
        }
    }

    /**
     * Resets the boards and restarts the game
     */
    public void restartGame() {
        // Create board
        setBoard(new int[boardSize]);
    }

    /**
     * Resets the scores
     */
    public void resetScores() {
        setPlayer1Score(0);
        setPlayer2Score(0);
        setTies(0);
    }

    public int[] getBoard() {
        return board;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public boolean getPlayer1Turn() {
        return isPlayer1Turn;
    }

    public int getTies() {
        return ties;
    }

    public void setBoard(int[] board) {
        this.board = board;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public void setPlayer1Score(int score) {
        player1Score = score;
    }

    public void setPlayer2Score(int score) {
        player2Score = score;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    private void changeTurn() {
        // Change turn
        if (isPlayer1Turn) {
            isPlayer1Turn = false;
        } else {
            isPlayer1Turn = true;
        }
    }

    private boolean checkWin() {
        // Winning format positions in array
        int[] win1 = {0, 1, 2
        };
        int[] win2 = {
                3, 4, 5
        };
        int[] win3 = {
                6, 7, 8
        };
        int[] win4 = {
                0, 3, 6
        };
        int[] win5 = {
                1, 4, 7
        };
        int[] win6 = {
                2, 5, 8
        };
        int[] win7 = {
                0, 4, 8
        };
        int[] win8 = {
                2, 4, 6
        };

        return false;
    }

    private void displayWin() {
        // TODO
    }

    private void endGame() {
        // TODO
    }
}
