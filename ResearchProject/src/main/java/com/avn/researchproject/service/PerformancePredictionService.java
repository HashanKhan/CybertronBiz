/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avn.researchproject.service;

import org.json.JSONObject;

/**
 *
 * @author chandima
 */
public interface PerformancePredictionService {
    public JSONObject getPredictedPerformance(String itnumber);
    public void createDataFile();
    public JSONObject generatePredictionResultSet();
}
