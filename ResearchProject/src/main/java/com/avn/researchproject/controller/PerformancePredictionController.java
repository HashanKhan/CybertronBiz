/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avn.researchproject.controller;

import com.avn.researchproject.service.PerformancePredictionService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author chandima
 */
@Controller
@RequestMapping("/performance")
public class PerformancePredictionController {

    @Autowired
    PerformancePredictionService performancePredictionService;
    
    @RequestMapping(value = "/getperformancevalues", method = RequestMethod.POST)
    public @ResponseBody
    String insertStudent(HttpServletRequest request, @RequestParam("action") String selectFunction) throws IOException {
        String sid = request.getParameter("performance_info");
        JSONObject response = performancePredictionService.getPredictedPerformance(sid);
        return response.toString();
    }
    
    @RequestMapping(value = "/checkperformancedone", method = RequestMethod.GET)
    public @ResponseBody
    String checkPerformanceDone(HttpServletRequest request, @RequestParam("action") String selectFunction) throws IOException {
        JSONObject response = performancePredictionService.generatePredictionResultSet();
        return response.toString();
    }

}
