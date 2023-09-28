package com.org.investechtransaction.db;

import com.org.investechtransaction.connection.DbConnection;
import org.example.exceptions.SqlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TransactionDbDao {
    Logger logger = LoggerFactory.getLogger(TransactionDbDao.class);
    DbConnection dbConnection;

    @Autowired
    public TransactionDbDao(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<String> getTransactionByUser(String userAlias) {
        List<String> stocksByUuid = new ArrayList<>();
        String GET_HOLDING_BY_USER = "SELECT stock_uuid " +
                "from transaction where user_alias = ?;";
        ResultSet rs;
        try(Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(GET_HOLDING_BY_USER)) {
            preparedStatement.setString(1, userAlias);
            rs = preparedStatement.executeQuery();
            while(rs.next()) {
                stocksByUuid.add(rs.getString(1));
            }
            return stocksByUuid;
        } catch (Exception e) {
            throw new SqlException("Error while getting stock holdings:  " + e);
        }
    }
}
