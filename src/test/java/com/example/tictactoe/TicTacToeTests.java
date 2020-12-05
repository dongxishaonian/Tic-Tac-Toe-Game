package com.example.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TicTacToeTests {
    private TicTacToe ticTacToe;

    @BeforeEach
    public void setUp() {
        ticTacToe = new TicTacToe();
    }

    //需求一：考虑棋子放在哪些地方非法
    //情况一：棋子放在x轴以外时非法，抛出异常
    @Test
    public void 给定一个棋子当放在x轴以外时抛出异常() {
        assertThrows(RuntimeException.class, () -> ticTacToe.put(3, 1));
    }

    //情况二：棋子放在y轴以外时非法，抛出异常
    @Test
    public void 给定一个棋子当放在y轴以外时抛出异常() {
        assertThrows(RuntimeException.class, () -> ticTacToe.put(1, 3));
    }

    //情况三：棋子放在非空闲位置时，抛出异常
    @Test
    public void 给定一个棋子当放在非空闲位置时抛出异常() {
        ticTacToe.put(1, 1);

        assertThrows(RuntimeException.class, () -> ticTacToe.put(1, 1));
    }

    //需求二：放置棋子的顺序
    @Test
    public void 先放棋子的人是0() {
        assertEquals('O', ticTacToe.nextPlayer());
    }

    @Test
    public void 后放棋子的人是1() {
        ticTacToe.nextPlayer();
        assertEquals('X', ticTacToe.nextPlayer());
    }

    //需求三：处理谁赢的问题
    //情况一：结局还没定，还在游戏中
    @Test
    public void 还在游戏中结局还没定() {
        String result = ticTacToe.put(1, 1);
        assertEquals(result, "游戏中");
    }

    //情况二：在水平方向上取得胜利
    @Test
    public void 在水平方向上取得胜利() {
        ticTacToe.put(1, 1);//O
        ticTacToe.put(0, 0);//X
        ticTacToe.put(2, 1);//O
        ticTacToe.put(2, 0);//X
        String result = ticTacToe.put(0, 1);//O
        assertEquals(result, "O在水平方向上取胜");
    }

    //情况三：在垂直方向上取得胜利
    @Test
    public void 在垂直方向上取得胜利() {
        ticTacToe.put(1, 1);//O
        ticTacToe.put(0, 0);//X
        ticTacToe.put(1, 2);//O
        ticTacToe.put(2, 0);//X
        String result = ticTacToe.put(1, 0);//O
        assertEquals(result, "O在垂直方向上取胜");
    }

    //情况四：在右下方向上取得胜利
    @Test
    public void 在右下方向上取得胜利() {
        ticTacToe.put(0, 0);//O
        ticTacToe.put(2, 0);//X
        ticTacToe.put(1, 1);//O
        ticTacToe.put(1, 0);//X
        String result = ticTacToe.put(2, 2);//O
        assertEquals(result, "O在右下方向上取胜");
    }

    //情况五：在左下方向上取得胜利
    @Test
    public void 在左下方向上取得胜利() {
        ticTacToe.put(2, 0);//O
        ticTacToe.put(1, 2);//X
        ticTacToe.put(1, 1);//O
        ticTacToe.put(1, 0);//X
        String result = ticTacToe.put(0, 2);//O
        assertEquals(result, "O在左下方向上取胜");
    }

    //情况六：棋盘已经全部被填满，平局
    @Test
    public void 棋盘全部填满并平局() {
        ticTacToe.put(0, 0);//O
        ticTacToe.put(1, 0);//X
        ticTacToe.put(2, 0);//O
        ticTacToe.put(2, 1);//X
        ticTacToe.put(0, 1);//O
        ticTacToe.put(0, 2);//X
        ticTacToe.put(1, 1);//O
        ticTacToe.put(2, 2);//X
        String result = ticTacToe.put(1, 2);//O
        assertEquals(result, "平局");
    }
}
