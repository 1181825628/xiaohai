package com.lianxi01.xh;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseJwrTest {
    public static void main(String[] args) {
        Claims claims = Jwts.parser()
                .setSigningKey("yikaikeji")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLlsI_pqawiLCJpYXQiOjE1NzQ5MTI3NzQsImV4cCI6MTU3NDkxMjgzNCwicm9sZSI6ImFkbWluIn0.hWuD_GcK6-2g4KdRaz6mps7vCBqcVICRAwdIGK1ITqA")
                .getBody();

        System.out.println("ID"+claims.getId());
        System.out.println("用户名"+claims.getSubject());
        System.out.println("登陆时间"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));
        System.out.println("过期时间"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));
        System.out.println("账号"+claims.get("role"));
    }
}
