/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avn.researchproject.dao;

import com.avn.researchproject.entity.Student;

/**
 *
 * @author chandima
 */
public interface PerformancePredictionDAO {
    public Student getPredictedPerformance(String id);
    public Student getAttendancePerformance(String id);
}
