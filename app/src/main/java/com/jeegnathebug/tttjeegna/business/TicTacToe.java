package com.jeegnathebug.tttjeegna.business;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by jeegnathebug on 19/09/16.
 *
 * A {@code TicTacToe} game. This class contains all the state information about an instance of the game
 */
public class TicTacToe implements Serializable {

    private static final long serialVersionUID = 1L;

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

        restartGame();
    }

    /**
     * Changes the turn
     */
    public void changeTurn() {
        isPlayer1Turn = !isPlayer1Turn;
    }

    /**
     * Checks if the current player has won the game
     *
     * @return {@code True} if the player won the game, {@code False} otherwise.
     */
    public boolean checkWin() {
        // Get player marker
        int playerMarker = isPlayer1Turn ? 1 : 2;

        // Winning format positions in array
        int[] win1 = {0, 1, 2};
        int[] win2 = {3, 4, 5};
        int[] win3 = {6, 7, 8};
        int[] win4 = {0, 3, 6};
        int[] win5 = {1, 4, 7};
        int[] win6 = {2, 5, 8};
        int[] win7 = {0, 4, 8};
        int[] win8 = {2, 4, 6};

        int[][] allWins = {win1, win2, win3, win4, win5, win6, win7, win8};

        int counter = 0;

        for (int i = 0; i < allWins.length; i++) {
            for (int j = 0; j < allWins[i].length; j++) {
                // if board at win1[0], win1[1], ... is playerMarker
                if (board[allWins[i][j]] == playerMarker) {
                    counter++;
                    // They have three in a row, so they win
                    if (counter == 3) {
                        return true;
                    }
                } else {
                    // Reset counter
                    counter = 0;
                }
            }
            // Reset counter
            counter = 0;
        }

        return false;
    }

    /**
     * Gets the board array as an int array. The values range from 0 - 2 inclusive, where 0 means the position has not been played, 1 means player 1 has played there, and 2 means player 2 has played there
     *
     * @return The board
     */
    public int[] getBoard() {
        return board;
    }

    /**
     * Gets the GameMode
     *
     * @return the GameMode
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Gets player 1's score
     *
     * @return Player 1's score
     */
    public int getPlayer1Score() {
        return player1Score;
    }

    /**
     * Gets player 2's score
     *
     * @return Player 2's score
     */
    public int getPlayer2Score() {
        return player2Score;
    }

    /**
     * Gets player 1's turn
     *
     * @return {@code True} if it is Player 1's turn, {@code False} otherwise.
     */
    public boolean getPlayer1Turn() {
        return isPlayer1Turn;
    }

    /**
     * Gets the tie counter
     *
     * @return The tie counter
     */
    public int getTies() {
        return ties;
    }

    /**
     * Checks whether the position has already been played or not
     *
     * @param position The position to check
     * @return {@code True} if the position has not yet been played, {@code False} otherwise.
     */
    public boolean isPlayable(int position) {
        return board[position] == 0;
    }

    /**
     * Puts a marker on the given position as being played, and disallows that position from being played again
     *
     * @param position The position on the tictactoe grid to be played
     */
    public void play(int position) {
        // Only play if block is empty
        if (isPlayable(position)) {
            // Set marker to 1 or 2 based on which player's turn it is
            board[position] = isPlayer1Turn ? 1 : 2;
        }
    }

    /**
     * Resets the scores
     */
    public void resetScores() {
        setPlayer1Score(0);
        setPlayer2Score(0);
        setTies(0);
    }

    /**
     * Resets the game
     */
    public void restartGame() {
        // Create new board
        setBoard(new int[boardSize]);
        isPlayer1Turn = true;
    }

    /**
     * Sets the board
     *
     * @param board The new board
     */
    public void setBoard(int[] board) {
        this.board = board;
    }

    /**
     * Sets the GameMode
     *
     * @param gameMode The new GameMode
     */
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Sets player 1's score
     *
     * @param score Player 1's new score
     */
    public void setPlayer1Score(int score) {
        player1Score = score;
    }

    /**
     * Sets player 2's score
     *
     * @param score Player 2's new score
     */
    public void setPlayer2Score(int score) {
        player2Score = score;
    }

    /**
     * Sets player 1's turn
     *
     * @param isPlayer1Turn {@code True} if it is player 1's turn, {@code False} otherwise.
     */
    public void setPlayer1Turn(boolean isPlayer1Turn) {
        this.isPlayer1Turn = isPlayer1Turn;
    }

    /**
     * Sets the tie counter
     *
     * @param ties The new tie counter
     */
    public void setTies(int ties) {
        this.ties = ties;
    }

    /**
     * Updates the score of the given winner
     *
     * @param winner The player who won, where 0 = tie, 1 = player 1, and 2 = player 2.
     */
    public void updateScore(int winner) {
        // Update score
        switch (winner) {
            case 0:
                ties++;
                break;
            case 1:
                player1Score++;
                break;
            case 2:
                player2Score++;
                break;
        }
    }
}
