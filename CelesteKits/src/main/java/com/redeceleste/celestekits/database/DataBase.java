package com.redeceleste.celestekits.database;

import java.sql.Connection;

public interface DataBase {
    void openConnection();
    void closeConnection();
    void createTables();
    boolean isConnect();
    Connection getConnection();
}
