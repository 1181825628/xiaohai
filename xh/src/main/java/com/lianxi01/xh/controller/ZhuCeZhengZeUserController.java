package com.lianxi01.xh.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.lianxi01.xh.dao.FangWenDao;
import com.lianxi01.xh.dao.incn;
import com.lianxi01.xh.entity.iocn;
import com.lianxi01.xh.entity.user;
import com.lianxi01.xh.mapper.DaoMapper;
import com.lianxi01.xh.utils.JwtUtil;
import com.mysql.cj.x.protobuf.Mysqlx;
import io.jsonwebtoken.Claims;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import static java.util.Locale.*;


@RestController
@RequestMapping("user")
public class ZhuCeZhengZeUserController {
    @Autowired
    private incn incn;
    @Autowired
    private FangWenDao FangWenDao;
    @Autowired
    private BCryptPasswordEncoder encode;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private HttpServletRequest request;
    //    @Autowired
//    private CDUSService cdusService;
    @Autowired
    private DaoMapper daoMapper;
    private static int cnt = 0;


    //    登陆
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public HashMap<String, Object> login(@Valid user users) {
        Iterable<user> all = FangWenDao.findAll();
        for (user user : all) {
            if (user.getUsername().equals(users.getUsername()) && encode.matches(users.getPassword(), user.getPassword())) {
                String token = jwtUtil.createJWT(users.getUsername(), users.getPassword(), "admin");
                HashMap<String, Object> map = new HashMap<>();
                map.put("token", token);
                return map;
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("20000", "账号或密码错误");
        return map;
    }

    //个人中心
    @RequestMapping(value = "/centre", method = RequestMethod.GET)
    @ResponseBody
    public LinkedHashMap<String, Object> centre() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        String header = request.getHeader("Authorization");
        if (header == null || "".equals(header)) {
            map.put("90001", "权限不足！");
            return map;
        }
        if (!header.startsWith("Bearer ")) {
            map.put("90001", "权限不足！");
            return map;
        }
        String token = header.substring(7);
        try {
            Claims claims = jwtUtil.parseJWT(token);
            String roles = (String) claims.get("roles");
            if (roles == null) {
                map.put("90001", "权限不足！");
                return map;
            }
        } catch (Exception e) {
            map.put("90001", "权限不足！");
            return map;
        }
        List<user> all = FangWenDao.findAll();
        for (user user : all) {
            if (user.getUsername().equals(jwtUtil.parseJWT(token).get("jti"))) {
                map.put("ID", user.getId());
                map.put("username", user.getUsername());
                map.put("email", user.getEmail());
                map.put("xingming", user.getXingming());
                map.put("sex", user.getSex());
                map.put("shoujihao", user.getShoujihao());
                map.put("zhuceshijian", user.getZhuceshijian());
                map.put("shengri", user.getShengri());
                return map;
            }
        }
        map.put("20001", "权限不足！");
        return map;
    }


    //    更新用户数据
    @RequestMapping(value = "/userupdate", method = RequestMethod.GET)
    @ResponseBody
    public LinkedHashMap<String, Object> userUpdate(@Valid user users) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        String header = request.getHeader("Authorization");
        if (header == null || "".equals(header)) {
            map.put("90001", "权限不足！");
            return map;
        }
        if (!header.startsWith("Bearer ")) {
            map.put("90001", "权限不足！");
            return map;
        }
        String token = header.substring(7);
        try {
            Claims claims = jwtUtil.parseJWT(token);
            String roles = (String) claims.get("roles");
            if (roles == null) {
                map.put("90001", "权限不足！");
                return map;
            }
        } catch (Exception e) {
            map.put("90001", "权限不足！");
            return map;
        }
        List<user> all = FangWenDao.findAll();
        for (user user : all) {
            if (user.getUsername().equals(jwtUtil.parseJWT(token).get("jti"))) {
                user.setPassword(encode.encode(users.getPassword()));
                user.setEmail(users.getEmail());
                user.setXingming(users.getXingming());
                user.setSex(users.getSex());
                user.setShoujihao(users.getShoujihao());
                FangWenDao.save(user);
                map.put("OK", "更新成功");
                map.put("ID", user.getId());
                map.put("username", user.getUsername());
                map.put("email", user.getEmail());
                map.put("xingming", user.getXingming());
                map.put("sex", user.getSex());
                map.put("shoujihao", user.getShoujihao());
                map.put("zhuceshijian", user.getZhuceshijian());
                map.put("shengri", user.getShengri());
//                map.put("OK，更新后的用户资料:",user);
                return map;
            }

        }
        map.put("90002", "权限不足！");
        return map;
    }

    @RequestMapping(value = "demo", method = RequestMethod.POST)
//    @RequestBody
    public LinkedHashMap<String, Object> demo(@RequestBody user users) {
        System.out.println(users.getUsername());
        LinkedHashMap map = new LinkedHashMap();
        map.put("1", "OK");
        return map;
    }

    //头像修改
    /*
    老样子，验证token 查找这个用户 查到以后 开始在本地创建文件夹 接受用户传来的头像
    加密后存放到文件夹中（后期需要添加验证 验证文件是否有效）将存放地址，放到用户的数据库中。需要时 查找出来即可
     */
    @RequestMapping(value = "/useriocn",method = RequestMethod.POST)
    public LinkedHashMap<String,Object> UserIocn(@RequestParam("file")MultipartFile file) throws IOException {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        String header = request.getHeader("Authorization");
        if (header == null || "".equals(header)) {
            map.put("90001", "权限不足！");
            return map;
        }
        if (!header.startsWith("Bearer ")) {
            map.put("90001", "权限不足！");
            return map;
        }
        String token = header.substring(7);
        try {
            Claims claims = jwtUtil.parseJWT(token);
            String roles = (String) claims.get("roles");
            if (roles == null) {
                map.put("90001", "权限不足！");
                return map;
            }
        } catch (Exception e) {
            map.put("90001", "权限不足！");
            return map;
        }
        String filepath = "/Volumes/dongzaihai/image/1";
        if (file.isEmpty()) {
//            System.out.println("文件为空空");
            map.put("90000", "文件为空");
            return map;
        }
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        if (!".jpg".equals(suffixName) && !".jpeg".equals(suffixName) && !".png".equals(suffixName) && !".bmp".equals(suffixName)
                && !".gif".equals(suffixName)) {
            map.put("90006", "上传失败:无效图片文件类型");
            return map;
        }
        long fileSize = file.getSize();
        if (fileSize > 1024 * 500) {
            map.put("90002", "图片过大");
            return map;
        }
//        System.out.println(filepath+token+suffixName);
        filepath = filepath + token + suffixName;
        File dest = new File(filepath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<user> all = FangWenDao.findAll();
        for (user user : all) {
            if (user.getUsername().equals(jwtUtil.parseJWT(token).get("jti"))) {
                String iconUrl = "/Volumes/dongzaihai/image/1" + token + suffixName;
                user.setIcon(iconUrl);
                List<iocn> incnAll = incn.findAll();
                for (iocn iocn : incnAll) {
                    if (iocn.getUsername().equals(jwtUtil.parseJWT(token).get("jti")) == true) {
                        if (iocn.getIcon1() == null) {
                            iocn.setIcon1(iconUrl);
                            incn.save(iocn);
                            map.put("10001", "本地存储成功");
                            map.put("存储地址", filepath);
                            return map;
                        } else if (iocn.getIcon2() == null) {
                            iocn.setIcon2(iconUrl);
                            incn.save(iocn);
                            map.put("10002", "本地存储成功");
                            map.put("存储地址", filepath);
                            return map;
                        } else if (iocn.getIcon3() == null) {
                            iocn.setIcon3(iconUrl);
                            incn.save(iocn);
                            map.put("10003", "本地存储成功");
                            map.put("存储地址", filepath);
                            return map;
                        } else if (iocn.getIcon4() == null) {
                            iocn.setIcon4(iconUrl);
                            incn.save(iocn);
                            map.put("10004", "本地存储成功");
                            map.put("存储地址", filepath);
                            return map;
                        } else if (iocn.getIcon5() == null) {
                            iocn.setIcon5(iconUrl);
                            incn.save(iocn);
                            map.put("10005", "本地存储成功");
                            map.put("存储地址", filepath);
                            return map;
                        }else {
                            iocn.setIcon1(iconUrl);
                            incn.save(iocn);
                            map.put("10000", "本地存储成功");
                            map.put("存储地址", filepath);
                            return map;
                        }
                    }
                }

                map.put("90011", "非法访问");
                return map;
            }
        }
            map.put("90010", "非法入侵");
            return map;
    }
    @RequestMapping(value = "/getImage",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage() throws IOException {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        List<user> all = FangWenDao.findAll();
        for (user user : all) {
            if (user.getUsername().equals(jwtUtil.parseJWT(token).get("jti"))) {
                if (user.getIcon().length()==0){
                    return null;
                }
                String icon = user.getIcon();
                File file = new File(icon);
                FileInputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes, 0, inputStream.available());
                return bytes;
            }
        }
        byte[] bytes = new byte[10];
        return bytes;
    }
    @RequestMapping(value = "/getByUserName", method = RequestMethod.POST)
    @ResponseBody
    public String getByUserName(@Valid user users, BindingResult bindingResult) {
        Iterable<user> all = FangWenDao.findAll();
        for (user user : all) {
            boolean equals = user.getUsername().equals(users.getUsername());
            if (equals == true) {
                return "本账号已被注册，请重新注册";
            }
        }
        if (bindingResult.hasErrors()) {
            return bindingResult.getFieldError().getDefaultMessage();
        }
        if (users.getPassword().length() < 5 || users.getPassword().length() > 10) {
            return "密码长度要求5-10位";
        }
        users.setPassword(encode.encode(users.getPassword()));
        FangWenDao.save(users);
        iocn n = new iocn();
        n.setUsername(users.getUsername());
        incn.save(n);
        return "向数据库输入user数据成功";
    }
}