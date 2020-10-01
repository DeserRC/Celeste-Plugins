package com.redeceleste.celesteshop.database;

import java.sql.Connection;

public interface DataBase {
    void openConnection();
    void closeConnection();
    void createTables();
    Boolean isConnect();
    Connection getConnection();
}
