package com.gimbal.android.sample;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by sravyadara on 5/5/15.
 */
@ParseClassName("Registration")
public class RegistrationDAO extends ParseObject {

    public RegistrationDAO(){

    }

    public String getUserName(){
        return getString("UserName");
    }

    public void setUserName(String userName){
        put("UserName", userName);
    }

    public String getUserId(){
        return getString("UserId");
    }

    public void setUserId(String userId){
        put("UserId", userId);
    }

    public String getEmail(){
        return getString("Email");
    }

    public void setEmail(String email){
        put("Email", email);
    }

    public String getImei(){
        return getString("IMEI");
    }

    public void setImei(String imei){
        put("IMEI", imei);
    }

    public String getRole(){
        return getString("Role");
    }

    public void setRole(String role){
        put("Role", role);
    }

    public static ParseQuery<RegistrationDAO> getQuery() {
        return ParseQuery.getQuery(RegistrationDAO.class);
    }
}
