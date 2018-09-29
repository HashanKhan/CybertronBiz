/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avn.researchproject.service;

import com.avn.researchproject.dao.PerformancePredictionDAO;
import com.avn.researchproject.entity.Student;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author chandima
 */
@Service("performancepredictionservice")
public class PerformancePredictionServiceImpl implements PerformancePredictionService {

    @Autowired
    PerformancePredictionDAO performancePredictionDAO;

    @Override
    public JSONObject getPredictedPerformance(String itnumber) {
        try {
            //createDataFile();
            Student student = performancePredictionDAO.getPredictedPerformance(itnumber);
            Student student1 = performancePredictionDAO.getAttendancePerformance(itnumber);
            double attendancePerform = (100*student1.getSumAttendance())/70;
            JSONObject response = new JSONObject();
            response.put("status", "SUCCESS");
            response.put("message", "ok");
            response.put("attendance", attendancePerform);
            //response.put("data", student.getMonth1()+":"+student.getMonth2());
            response.put("data", student.getPerformanceLevel());
            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    @Override
    public void createDataFile() {
        try {
            File UIFile = new File("C:/Users/chandima/Documents/python/output.txt");
            if (UIFile.exists()) {
                Scanner scanner = new Scanner(new File("C:/Users/chandima/Documents/python/output.txt"));
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().toString();
                    String[] lineArray = line.split("\\.");
                    String out = lineArray[0];
                    System.out.println(out);
                }
                scanner.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(PerformancePredictionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            File input = new File("input");
            File output = new File("output");
            Scanner sc = new Scanner(input);
            PrintWriter printer = new PrintWriter(output);
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                printer.write(s);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found. Please scan in new file.");
        }
    }

    public JSONObject generatePredictionResultSet() {
        int status = 5;
        Runtime rt = Runtime.getRuntime();
        Process p1 = null;
        try {
            p1 = rt.exec("cmd /c start C:/Users/chandima/Documents/python/grade.bat");
            p1.waitFor();
            while (status != 1) {
                int count = 0;
                if (p1.exitValue() == 0) {
                    status = 1;
                }
                count++;
            }
        } catch (IOException ex) {
            Logger.getLogger(PerformancePredictionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(PerformancePredictionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 

        JSONObject response = new JSONObject();
        response.put("status", "SUCCESS");
        response.put("message", "Response recieved success");
        response.put("success", true);
        response.put("data", status);
        return response;
    }

}
