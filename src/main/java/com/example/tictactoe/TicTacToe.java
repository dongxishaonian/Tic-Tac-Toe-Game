package com.example.tictactoe;

import com.example.tictactoe.mongo.TicTacToeCollection;
import com.example.tictactoe.mongo.TicTacToeDO;

import java.net.UnknownHostException;
import java.util.Scanner;

public class TicTacToe {
    private static final char O_PLAYER = 'O';
    private static final char X_PLAYER = 'X';

    private TicTacToeCollection ticTacToeCollection;
    private int turn;
    private Character currentPlayer = '\0';
    private Character[][] chess_board = new Character[][]{{'\0', '\0', '\0'}, {'\0', '\0', '\0'}, {'\0', '\0', '\0'}};

    public TicTacToe() throws UnknownHostException {
        this.ticTacToeCollection = new TicTacToeCollection();
    }


    public String put(int x, int y) {
        checkLocation(x, y);
        Character play = nextPlayer();
        chess_board[x][y] = play;
        String currentGameStatus = getCurrentGameStatus(x, y);

        handlerChessBoardDb(x, y, currentGameStatus);

        return currentGameStatus;
    }

    private void handlerChessBoardDb(int x, int y, String currentGameStatus) {
        ticTacToeCollection.saveTicTacToe(new TicTacToeDO()
                .setChessBoard(chess_board)
                .setCurrentPlayer(currentPlayer)
                .setX(x)
                .setTurn(++turn)
                .setY(y));

        if (!currentGameStatus.equals("游戏中")) {
            ticTacToeCollection.drop();
        }
    }

    public void initChessBoard() {
        TicTacToeDO ticTacToeDO = ticTacToeCollection.read();
        if (ticTacToeDO != null) {
            this.chess_board = ticTacToeDO.getChessBoard();
            this.currentPlayer = ticTacToeDO.getCurrentPlayer();
            this.turn = ticTacToeDO.getTurn();
        }
    }

    private void checkLocation(int x, int y) {
        checkXLocation(x);
        checkYLocation(y);
        checkExistLocation(x, y);
    }

    private String getCurrentGameStatus(int x, int y) {
        String result = checkWin(x, y);
        if (result != null) {
            return result;
        }

        if (isDraw()) {
            return "平局";
        }

        return "游戏中";
    }

    private String checkWin(int x, int y) {
        int exceptWinResult = currentPlayer * 3;
        int sp = 0, cz = 0, yx = 0, zx = 0;
        for (int i = 0; i < 3; i++) {
            sp += chess_board[i][y];
            cz += chess_board[x][i];
            yx += chess_board[i][i];
            zx += chess_board[i][2 - i];
        }

        if (sp == exceptWinResult) {
            return currentPlayer + "在水平方向上取胜";
        }
        if (cz == exceptWinResult) {
            return currentPlayer + "在垂直方向上取胜";
        }
        if (yx == exceptWinResult) {
            return currentPlayer + "在右下方向上取胜";
        }
        if (zx == exceptWinResult) {
            return currentPlayer + "在左下方向上取胜";
        }
        return null;
    }

    private boolean isDraw() {
        int pj1 = O_PLAYER * 4 + X_PLAYER * 5;
        int pj2 = O_PLAYER * 5 + X_PLAYER * 4;
        int pj = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                pj += chess_board[i][j];
            }
        }
        return pj == pj1 || pj == pj2;
    }

    private void checkExistLocation(int x, int y) {
        if (chess_board[x][y] != '\0') {
            throw new RuntimeException("当前位置已有其他棋子，无效！");
        }
    }

    private void checkYLocation(int y) {
        if (y < 0 || y > 2) {
            throw new RuntimeException("当前位置在y轴以外，无效！");
        }
    }

    private void checkXLocation(int x) {
        if (x < 0 || x > 2) {
            throw new RuntimeException("当前位置在x轴以外，无效！");
        }
    }

    public Character nextPlayer() {
        if (currentPlayer == '\0' || currentPlayer != O_PLAYER) {
            currentPlayer = O_PLAYER;
            return O_PLAYER;
        }
        currentPlayer = X_PLAYER;
        return X_PLAYER;
    }

    public TicTacToeCollection getTicTacToeCollection() {
        return ticTacToeCollection;
    }

    public TicTacToe setTicTacToeCollection(TicTacToeCollection ticTacToeCollection) {
        this.ticTacToeCollection = ticTacToeCollection;
        return this;
    }

    public int getTurn() {
        return turn;
    }

    public TicTacToe setTurn(int turn) {
        this.turn = turn;
        return this;
    }

    public Character getCurrentPlayer() {
        return currentPlayer;
    }

    public TicTacToe setCurrentPlayer(Character currentPlayer) {
        this.currentPlayer = currentPlayer;
        return this;
    }

    public Character[][] getChess_board() {
        return chess_board;
    }

    public TicTacToe setChess_board(Character[][] chess_board) {
        this.chess_board = chess_board;
        return this;
    }

    public static void main(String[] args) throws UnknownHostException {
        TicTacToe ticTacToe = new TicTacToe();
        ticTacToe.initChessBoard();
        Scanner input = new Scanner(System.in);
        System.out.println("tictactoe start!");
        printChessBoard(ticTacToe);
        while (true) {
            String a = input.nextLine();
            String[] str = a.split(",");
            String result = ticTacToe.put(Integer.parseInt(str[0]), Integer.parseInt(str[1]));

            printChessBoard(ticTacToe);


            if (!result.equals("游戏中") && !result.equals("请重试")) {
                System.out.println(result + "♨️");
                break;
            }
            System.out.println(result + "继续...");
        }

        input.close();
    }

    private static void printChessBoard(TicTacToe ticTacToe) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Character character = ticTacToe.chess_board[j][i];
                if (character.equals('\0')) {
                    System.out.print("🐻" + " ");
                } else {
                    System.out.print(character + " ");
                }
            }
            System.out.println();
        }
    }
}
