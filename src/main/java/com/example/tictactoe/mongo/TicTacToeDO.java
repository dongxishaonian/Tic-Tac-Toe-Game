package com.example.tictactoe.mongo;

import org.jongo.marshall.jackson.oid.Id;

public class TicTacToeDO {
    @Id
    private int turn;
    private Character[][] chessBoard;
    private Character currentPlayer;
    private Integer x;
    private Integer y;

    public Character[][] getChessBoard() {
        return chessBoard;
    }

    public TicTacToeDO setChessBoard(Character[][] chessBoard) {
        this.chessBoard = chessBoard;
        return this;
    }

    public Character getCurrentPlayer() {
        return currentPlayer;
    }

    public TicTacToeDO setCurrentPlayer(Character currentPlayer) {
        this.currentPlayer = currentPlayer;
        return this;
    }

    public int getTurn() {
        return turn;
    }

    public TicTacToeDO setTurn(int turn) {
        this.turn = turn;
        return this;
    }

    public Integer getX() {
        return x;
    }

    public TicTacToeDO setX(Integer x) {
        this.x = x;
        return this;
    }

    public Integer getY() {
        return y;
    }

    public TicTacToeDO setY(Integer y) {
        this.y = y;
        return this;
    }
}
