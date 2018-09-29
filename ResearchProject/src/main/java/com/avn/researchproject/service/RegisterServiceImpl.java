/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avn.researchproject.service;

import com.avn.researchproject.dao.RegisterDAO;
import com.avn.researchproject.entity.Student;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author chandima
 */
@Service("registerService")
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    RegisterDAO registerDAO;

    @Override
    public JSONObject registerFinger(Student student) {

        int id = registerDAO.registerFinger(student);
        JSONObject response = new JSONObject();
        response.put("status", "SUCCESS");
        response.put("message", "Response recieved success");
        response.put("success", true);
        response.put("data", id);
        return response;
    }

    @Override
    public JSONObject getLastId() {
        int id = registerDAO.getLastId();
        JSONObject response = new JSONObject();
        response.put("status", "SUCCESS");
        response.put("message", "Response recieved success");
        response.put("success", true);
        response.put("data", id);
        return response;
    }

}
