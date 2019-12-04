package com.lianxi01.xh.entity;

import javax.persistence.*;
import java.io.Serializable;

    @Entity
    @Table(name = "historyiocn")
public class iocn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String icon1;
    private String icon2;
    private String icon3;
    private String icon4;
    private String icon5;
    public int getId() {
            return id;
    }
    public void setId(int id) {
            this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon1() {
        return icon1;
    }

    public void setIcon1(String icon1) {
        this.icon1 = icon1;
    }

    public String getIcon2() {
        return icon2;
    }

    public void setIcon2(String icon2) {
        this.icon2 = icon2;
    }

    public String getIcon3() {
        return icon3;
    }

    public void setIcon3(String icon3) {
        this.icon3 = icon3;
    }

    public String getIcon4() {
        return icon4;
    }

    public void setIcon4(String icon4) {
        this.icon4 = icon4;
    }

    public String getIcon5() {
        return icon5;
    }

    public void setIcon5(String icon5) {
        this.icon5 = icon5;
    }
}
