package com.example.tictactoe.mongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jongo.Find;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.net.UnknownHostException;

public class TicTacToeCollection {
    private final String dbName = "tic-tac-toe";
    private final String collectionName = "game";
    private MongoCollection mongoCollection;

    public void setMongoCollection(MongoCollection mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    public MongoCollection getMongoCollection() {
        return mongoCollection;
    }

    public TicTacToeCollection() throws UnknownHostException {
        DB db = new MongoClient().getDB(dbName);
        mongoCollection = new Jongo(db).getCollection(collectionName);
        mongoCollection = new Jongo(db).getCollection(collectionName);
        this.setMongoCollection(mongoCollection);
    }

    public boolean saveTicTacToe(TicTacToeDO ticTacToeDO) {
        try {
            this.getMongoCollection().save(ticTacToeDO);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    public boolean drop() {
        try {
            this.getMongoCollection().drop();
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    public TicTacToeDO read() {
        try {
            Find find = this.getMongoCollection().find();
            return find.sort("{_id:-1}").limit(1).as(TicTacToeDO.class).next();
        } catch (Exception e) {
            return null;
        }
    }
}
