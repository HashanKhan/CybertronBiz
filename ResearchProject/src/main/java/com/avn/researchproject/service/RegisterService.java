/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avn.researchproject.service;

import com.avn.researchproject.entity.Student;
import org.json.JSONObject;

/**
 *
 * @author chandima
 */
public interface RegisterService {
    public JSONObject registerFinger(Student student);
    public JSONObject getLastId();
}
