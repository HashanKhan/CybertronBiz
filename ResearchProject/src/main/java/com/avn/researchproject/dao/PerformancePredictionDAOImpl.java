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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author chandima
 */
@Repository("performancePredictionDAO")
public class PerformancePredictionDAOImpl implements PerformancePredictionDAO {

    @Autowired
    private DataSource dataSource;

    @Override
    public Student getPredictedPerformance(String id) {
        Connection connection = null;
        PreparedStatement statement = null;
        //String sql = "SELECT `performancelevel` FROM `performance` WHERE itnumber  = '" + id + "'";
        String sql = "SELECT `performlevelgrp` FROM `researchdataset` WHERE studentid  = '" + id + "'";
       // String sql = "SELECT * FROM `performance` WHERE itnumber  = '" + id + "'";

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery(sql);
            Student student = mapRow(result);
            result.close();
            statement.close();
            connection.close();
            return student;
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Student();
        }
    }

    private Student mapRow(ResultSet rs) {
        Student student = new Student();
        try {
            if (rs.next()) {
                //student.setMonth1(rs.getInt("month1"));
                //student.setMonth2(rs.getInt("month2"));
                student.setPerformanceLevel(rs.getInt("performlevelgrp"));
                return student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Student();
    }

    @Override
    public Student getAttendancePerformance(String id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "SELECT (`attendance1`+ `attendance2`+ `attendance3`+ `attendance4` + `attendance4`) AS sumattendance FROM `researchdataset` WHERE studentid = '" + id + "'";
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery(sql);
            Student student = mapRowAttendance(result);
            result.close();
            statement.close();
            connection.close();
            return student;
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Student();
        }
    }

    private Student mapRowAttendance(ResultSet rs) {
        Student student = new Student();
        try {
            if (rs.next()) {
                student.setSumAttendance(rs.getInt("sumattendance"));
                return student;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PerformancePredictionDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Student();
    }

}
