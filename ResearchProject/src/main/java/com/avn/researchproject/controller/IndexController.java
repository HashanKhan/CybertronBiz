/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avn.researchproject.controller;

import com.avn.researchproject.entity.Attendance;
import com.avn.researchproject.entity.Student;
import com.avn.researchproject.service.AttendanceService;
import com.avn.researchproject.service.RegisterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author chandima
 */
@Controller
@RequestMapping("/register")
public class IndexController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private AttendanceService attendanceService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String logout(@RequestParam(value = "auth", required = false) String auth, ModelMap model) {
        model.put("StudentRegisterForm", new Student());
        return "index";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public @ResponseBody
    String insertStudent(HttpServletRequest request, @RequestParam("action") String selectFunction) throws IOException {
        String fid = request.getParameter("fingerid");
        int fingerPrint = Integer.parseInt(fid);
        String sid = request.getParameter("studentid");
        Student student = new Student();
        student.setFingerPrint(fingerPrint);
        student.setItnumber(sid);
        JSONObject response = registerService.registerFinger(student);
        return response.toString();
    }
    
    @RequestMapping(value = "/getlastid", method = RequestMethod.GET)
    public @ResponseBody
    String getLastId(HttpServletRequest request, @RequestParam("action") String selectFunction) throws IOException {
        JSONObject response = registerService.getLastId();
        return response.toString();
    }

    @RequestMapping(value = "/scanfinger", method = RequestMethod.POST)
    public @ResponseBody
    String markAttendance(HttpServletRequest request, @RequestParam("action") String selectFunction) throws IOException {
        String sectiondata = request.getParameter("attendance_info");
        String lecturehall = request.getParameter("lecture_hall");
        //Attendance attendance = (Attendance) new ObjectMapper().readValue(sectiondata, Attendance.class);
        Attendance attendance = new Attendance();
        int fingerprint = Integer.parseInt(sectiondata);
        attendance.setFingerprint(fingerprint);
        attendance.setHall(lecturehall);
        JSONObject response = attendanceService.markAttendance(attendance);
        return response.toString();
    }
}
