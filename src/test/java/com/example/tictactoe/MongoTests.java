package com.example.tictactoe;

import com.example.tictactoe.mongo.TicTacToeCollection;
import com.example.tictactoe.mongo.TicTacToeDO;
import org.jongo.MongoCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MongoTests {
    @Spy
    private TicTacToeCollection tictacToeCollection;
    @Mock
    private MongoCollection mongoCollection;
    private static final String dbName = "tic-tac-toe";
    private static final String collectionName = "game";
    private TicTacToeDO ticTacToeDO;

    @BeforeEach
    public void setUp() {
        ticTacToeDO = new TicTacToeDO();
        ticTacToeDO.setCurrentPlayer('O');
        Character[][] chess_board = new Character[][]{{'O', '\0', '\0'}, {'\0', '\0', '\0'}, {'\0', '\0', '\0'}};
        ticTacToeDO.setChessBoard(chess_board);
    }

    @Test
    public void mongo的集合名称符合预期() {
        assertEquals(collectionName, tictacToeCollection.getMongoCollection().getName());
    }

    @Test
    public void mongo的数据库名称符合预期() {
        assertEquals(dbName, tictacToeCollection.getMongoCollection().getDBCollection().getDB().getName());
    }

    @Test
    public void 插入数据时调用了mongo的save方法() {
        when(tictacToeCollection.getMongoCollection()).thenReturn(mongoCollection);
        tictacToeCollection.saveTicTacToe(ticTacToeDO);
        verify(mongoCollection, times(1)).save(any(TicTacToeDO.class));
    }

    @Test
    public void 插入数据时成功() {
        when(tictacToeCollection.getMongoCollection()).thenReturn(mongoCollection);
        boolean res = tictacToeCollection.saveTicTacToe(ticTacToeDO);
        assertTrue(res);
    }

    @Test
    public void 插入数据时失败() {
        when(mongoCollection.save(any(TicTacToeDO.class))).thenThrow(new RuntimeException());
        when(tictacToeCollection.getMongoCollection()).thenReturn(mongoCollection);
        boolean res = tictacToeCollection.saveTicTacToe(ticTacToeDO);
        assertFalse(res);
    }

    @Test
    public void 删除数据时执行collection的drop方法() {
        when(tictacToeCollection.getMongoCollection()).thenReturn(mongoCollection);
        tictacToeCollection.drop();
        verify(mongoCollection, times(1)).drop();
    }

    @Test
    public void 删除数据时成功() {
        when(tictacToeCollection.getMongoCollection()).thenReturn(mongoCollection);
        boolean res = tictacToeCollection.drop();
        assertTrue(res);
    }

    @Test
    public void 删除数据时失败() {
        doThrow(new RuntimeException()).when(mongoCollection).drop();
        when(tictacToeCollection.getMongoCollection()).thenReturn(mongoCollection);
        boolean res = tictacToeCollection.drop();
        assertFalse(res);
    }

    @Test
    public void 读取数据成功() {
        when(tictacToeCollection.getMongoCollection()).thenReturn(mongoCollection);
        tictacToeCollection.read();
        verify(mongoCollection, times(1)).find();
    }

    @Test
    public void 读取数据失败() {
        doThrow(new RuntimeException()).when(mongoCollection).find();
        when(tictacToeCollection.getMongoCollection()).thenReturn(mongoCollection);
        TicTacToeDO ticTacToeDO = tictacToeCollection.read();
        assertNull(ticTacToeDO);
    }
}
