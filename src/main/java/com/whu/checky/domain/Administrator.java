package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

import java.io.Serializable;
import java.util.List;

public class Administrator implements Serializable {

    @TableId
    private String userId;
    //用户名
    private String userName;
    //密码
    private String userPassword;

    private String sessionId;
    //权限列表
    private List<String> permissions;
    //管理员所处部门
    private String department;
    //邮箱
    private String email;

    private String userTel;

    private String userEmail;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

<<<<<<< HEAD
    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getDepartment(){
        return department;
    }
    public void setDepartment(String department){
        this.department=department;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
=======
    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
>>>>>>> 6a080a025d2047c62e11c6686b233db6fc15b7ac
    }
}
