/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avn.researchproject.service;

import com.avn.researchproject.dao.AttendanceDAO;
import com.avn.researchproject.entity.Attendance;
import com.avn.researchproject.entity.Student;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author chandima
 */
@Service("attendanceService")
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceDAO attendanceDAO;

    @Override
    public JSONObject markAttendance(Attendance attendance) {
        JSONObject response = new JSONObject();

        Student student = attendanceDAO.getItnumberByFingerprint(attendance.getFingerprint());
        if (student.getItnumber() != null) {
            attendance.setItnumber(student.getItnumber());
            //attendance.setHall("A501");
            attendance.setSubject("ML");
            attendanceDAO.markAttendance(attendance);

            response.put("status", "SUCCESS");
            response.put("message", "Response recieved success");
            response.put("success", true);
            response.put("data", student.getItnumber());
        } else {
            response.put("status", "SUCCESS");
            response.put("message", "Response recieved success");
            response.put("success", true);
            response.put("data", "No such record");
        }
        return response;
    }


}
