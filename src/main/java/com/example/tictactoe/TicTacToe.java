package com.example.tictactoe;

import java.util.Scanner;

public class TicTacToe {

    private static final char O_PLAYER = 'O';
    private static final char X_PLAYER = 'X';

    private Character currentPlayer = '\0';
    private Character[][] chess_board = new Character[][]{{'\0', '\0', '\0'}, {'\0', '\0', '\0'}, {'\0', '\0', '\0'}};

    public String put(int x, int y) {
        checkLocation(x, y);
//        try {
//            checkLocation(x, y);
//        } catch (RuntimeException e) {
//            System.out.println(e.getMessage());
//            return "请重试";
//        }
        Character play = nextPlayer();
        chess_board[x][y] = play;
        return getCurrentGameStatus(x, y);
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

    public static void main(String[] args) {
        TicTacToe ticTacToe = new TicTacToe();
        Scanner input = new Scanner(System.in);
        System.out.println("tictactoe start!");
        while (true) {
            String a = input.nextLine();
            String[] str = a.split(",");
            String result = ticTacToe.put(Integer.parseInt(str[0]), Integer.parseInt(str[1]));

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


            if (!result.equals("游戏中") && !result.equals("请重试")) {
                System.out.println(result + "♨️");
                break;
            }
            System.out.println(result + "继续...");
        }
    }
}
