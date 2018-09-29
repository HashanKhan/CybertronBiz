/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avn.researchproject.dao;

import com.avn.researchproject.entity.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author chandima
 */
@Repository("registerDAO")
public class RegisterDAOImpl implements RegisterDAO {

    @Autowired
    private DataSource dataSource;

    @Override
    public int registerFinger(Student student) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int ID = 0;
        try {
            String sql = "INSERT INTO `register`(`itnumber`, `fingerprintcode`) VALUES (?,?)";
            String[] generatedColumns = {"id"};
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql, generatedColumns);
            statement.setString(1, student.getItnumber());
            statement.setInt(2, student.getFingerPrint());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                ID = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return ID;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int getLastId() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int ID = 0;
        try {
            String sql = "SELECT COUNT(*) FROM `register`";
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                ID = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return ID;
        } catch (Exception e) {
            return 0;
        }
    }

}
