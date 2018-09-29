/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avn.researchproject.service;

import com.avn.researchproject.entity.Attendance;
import org.json.JSONObject;

/**
 *
 * @author chandima
 */
public interface AttendanceService {

    public JSONObject markAttendance(Attendance attendance);

}
