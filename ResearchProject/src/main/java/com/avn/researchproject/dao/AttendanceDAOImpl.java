/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avn.researchproject.dao;

import com.avn.researchproject.entity.Attendance;
import com.avn.researchproject.entity.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author chandima
 */
@Repository("attendanceDAO")
public class AttendanceDAOImpl implements AttendanceDAO {

    @Autowired
    private DataSource dataSource;

    @Override
    public int markAttendance(Attendance attendance) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int ID = 0;
        try {
            String sql = "INSERT INTO `attendance`(`subject`, `hall`, `itnumber`, `fingerprint`) VALUES (?,?,?,?)";
            String[] generatedColumns = {"id"};
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql, generatedColumns);
            statement.setString(1, attendance.getSubject());
            statement.setString(2, attendance.getHall());
            statement.setString(3, attendance.getItnumber());
            statement.setInt(4, attendance.getFingerprint());
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
    public Student getItnumberByFingerprint(int fingerPrint) {
        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "SELECT `id`, `itnumber`, `fingerprintcode` FROM `register` WHERE fingerprintcode  = '" + fingerPrint +"'";
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
                student.setId(rs.getInt("id"));
                student.setItnumber(rs.getString("itnumber"));
                student.setFingerPrint(rs.getInt("fingerprintcode"));
                return student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Student();
    }

}
