package cn.ffcs.zhsq.mybatis.domain.executeControl;

import java.io.Serializable;

public class SelfAttr implements Serializable {

    private String name; //姓名
    private String gender; //性别
    private String mobile; //手机号
    private String birthday; //出生日期
    private String nationalityCN; //民族
    private String identityCardNumber; //身份证号
    private String description; //描述

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNationalityCN() {
        return nationalityCN;
    }

    public void setNationalityCN(String nationalityCN) {
        this.nationalityCN = nationalityCN;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
