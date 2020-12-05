package com.example.tictactoe;

import com.example.tictactoe.mongo.TicTacToeCollection;
import com.example.tictactoe.mongo.TicTacToeDO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TicTacToeTests {
    @Mock
    private TicTacToeCollection ticTacToeCollection;
    @InjectMocks
    private TicTacToe ticTacToe;


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

    //需求四：持久化棋盘
    //情况一：每一步都持久化保存
    @Test
    public void 落子后保存棋局成功() {
        ticTacToe.put(1, 1);
        verify(ticTacToeCollection, times(1)).saveTicTacToe(any(TicTacToeDO.class));
    }

    //情况二：出久化失败
    @Test
    public void 落子后保存棋局失败() {
        when(ticTacToeCollection.saveTicTacToe(any(TicTacToeDO.class))).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> ticTacToe.put(1, 1));
    }

    //需求四:游戏结束，删除数据
    //情况一：删除成功
    @Test
    public void 游戏结束后删除数据() {
        ticTacToe.put(0, 0);//O
        ticTacToe.put(2, 0);//X
        ticTacToe.put(1, 1);//O
        ticTacToe.put(1, 0);//X
        ticTacToe.put(2, 2);//O
        verify(ticTacToeCollection, times(1)).drop();
    }

    //情况一：删除失败
    @Test
    public void 游戏结束后删除数据失败() {
        when(ticTacToeCollection.drop()).thenThrow(new RuntimeException());
        ticTacToe.put(0, 0);//O
        ticTacToe.put(2, 0);//X
        ticTacToe.put(1, 1);//O
        ticTacToe.put(1, 0);//X
        assertThrows(RuntimeException.class, () -> ticTacToe.put(2, 2));
    }

    //需求五：初始化游戏
    @Test
    public void 初始化游戏成功() {
        TicTacToeDO ticTacToeDO = new TicTacToeDO();
        ticTacToeDO.setCurrentPlayer('O');
        ticTacToeDO.setX(0);
        ticTacToeDO.setY(0);
        ticTacToeDO.setTurn(1);
        Character[][] chess_board = new Character[][]{{'O', '\0', '\0'}, {'\0', '\0', '\0'}, {'\0', '\0', '\0'}};
        ticTacToeDO.setChessBoard(chess_board);
        when(ticTacToeCollection.read()).thenReturn(ticTacToeDO);

        ticTacToe.initChessBoard();
        verify(ticTacToeCollection, times(1)).read();
        assertEquals(chess_board, ticTacToe.getChess_board());
        assertEquals(ticTacToeDO.getTurn(), ticTacToe.getTurn());
        assertEquals(ticTacToeDO.getCurrentPlayer(), ticTacToe.getCurrentPlayer());
    }

    @Test
    public void 无数据无需初始化() throws JsonProcessingException {
        when(ticTacToeCollection.read()).thenReturn(null);

        ticTacToe.initChessBoard();
        verify(ticTacToeCollection, times(1)).read();
        Character[][] chess_board = new Character[][]{{'\0', '\0', '\0'}, {'\0', '\0', '\0'}, {'\0', '\0', '\0'}};
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(chess_board));
        assertEquals(objectMapper.writeValueAsString(chess_board), objectMapper.writeValueAsString(ticTacToe.getChess_board()));
        assertEquals(0, ticTacToe.getTurn());
        assertEquals('\0',ticTacToe.getCurrentPlayer());
    }

}
