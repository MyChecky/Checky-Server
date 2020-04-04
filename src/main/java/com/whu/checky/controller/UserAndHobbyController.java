package com.whu.checky.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whu.checky.config.UploadConfig;
import com.whu.checky.domain.Hobby;
import com.whu.checky.domain.ServiceTerms;
import com.whu.checky.domain.User;
import com.whu.checky.service.HobbyService;
import com.whu.checky.service.ServiceTermsService;
import com.whu.checky.service.UserService;
import com.whu.checky.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/userAndHobby")
public class UserAndHobbyController {

    @Autowired
    private UserService userService;

    @Autowired
    private HobbyService hobbyService;

    @Autowired
    private UploadConfig uploadConfig;

    @Autowired
    private ServiceTermsService serviceTermsService;

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    @RequestMapping("/initUserHobbies")
    public HashMap<String, Object> login(@RequestBody String body) {
        HashMap<String, Object> ret = new HashMap<>(); // 返回值
        JSONObject object = JSONObject.parseObject(body);
        String userId = (String) object.get("userId");
        int numSize = 9;
        String newHobbiesStrStart = getNewHobbiesStrStart(userId, numSize);
        ret.put("newHobbiesJson", newHobbiesStrStart);
        String hobbiesStrStart = getHobbiesStrStart(userId, numSize);
        ret.put("hobbiesJson", hobbiesStrStart);
        ret.put("state", "ok");
        return ret;
    }

    @RequestMapping("/updateUser")
    public HashMap<String, Object> updateUser(@RequestBody String body) {
        HashMap<String, Object> ret = new HashMap<>(); // 返回值
        JSONObject object = JSONObject.parseObject(body);
        String userId = (String) object.get("userId");
        int userGender = object.getInteger("userGender");
        String userAvatar = (String) object.get("userAvatar");
        String userName = (String) object.get("userName");
        User user = userService.queryUser(userId);
        user.setUserName(userName);
        user.setUserAvatar(userAvatar);
        user.setUserGender(userGender);
        userService.updateUser(user);
        ret.put("state", "ok");
        return ret;
    }

    @RequestMapping("/updateHobbies")
    public HashMap<String, Object> updateHobbies(@RequestBody String body) {
        HashMap<String, Object> ret = new HashMap<>(); // 返回值
        JSONObject object = JSONObject.parseObject(body);
        String userId = (String) object.get("userId");
        JSONArray hobbiesArray = object.getJSONArray("hobbies");
        List<String> hobbies = JSONObject.parseArray(hobbiesArray.toJSONString(), String.class);
        hobbyService.updateUserHobbies(userId, hobbies);
        ret.put("state", "ok");
        return ret;
    }

    @RequestMapping("/getHobbies")
    public HashMap<String, Object> getHobbies(@RequestBody String body) {
        HashMap<String, Object> ret = new HashMap<>(); // 返回值
        JSONObject object = JSONObject.parseObject(body);
        String userId = (String) object.get("userId");
        int numSize = 9;
        String newHobbiesStrStart = getNewHobbiesStrStart(userId, numSize);
        ret.put("newHobbiesJson", newHobbiesStrStart);
        ret.put("state", "ok");
        return ret;
    }

    private String getHobbiesStrStart(String userId, int numSize) {
        StringBuilder hobbiesStrStart = new StringBuilder("{\"hobbies\":[");
        List<String> hobbies = hobbyService.getUserHobbies(userId, numSize);
        for (int i = 0; i < (numSize / 3); i++) {
            String a = hobbies.get(i * 3).equals("") ? "0" : "1";
            String b = hobbies.get(i * 3 + 1).equals("") ? "0" : "1";
            String c = hobbies.get(i * 3 + 2).equals("") ? "0" : "1";
            hobbiesStrStart.append("{\"subHobbies\":[" + "{\"hobbyValue\": \"")
                    .append(hobbies.get(i * 3))
                    .append("\",\"hobbyId\": \"1\",\"ifChoose\": \"" + a + "\"},")
                    .append("{\"hobbyValue\": \"")
                    .append(hobbies.get(i * 3 + 1))
                    .append("\",\"hobbyId\": \"1\",\"ifChoose\": \"" + b + "\"},")
                    .append("{\"hobbyValue\": \"")
                    .append(hobbies.get(i * 3 + 2))
                    .append("\",\"hobbyId\": \"1\",\"ifChoose\": \"" + c + "\"}]},");
        }

//        for (int i = 0; i <= (hobbies.size() / 3); i++) {
//            if (i==hobbies.size()/3 && hobbies.size() % 3 == 0) {
//                break;
//            } else {
//                hobbiesStrStart.append("{\"subHobbies\":[").append("{\"hobbyValue\": \"")
//                        .append(hobbies.get(i * 3))
//                        .append("\",\"hobbyId\": \"1\",\"ifChoose\": \"1\"},");
//            }
//            if(i==hobbies.size()/3 && hobbies.size() %3 == 1){
//                hobbiesStrStart.replace(hobbiesStrStart.length() - 1, hobbiesStrStart.length(),"]},");
//                break;
//            }else{
//                hobbiesStrStart.append("{\"hobbyValue\": \"")
//                        .append(hobbies.get(i * 3 + 1))
//                        .append("\",\"hobbyId\": \"1\",\"ifChoose\": \"1\"},");
//            }
//            if(i==hobbies.size()/3 && hobbies.size()%3 == 2){
//                hobbiesStrStart.replace(hobbiesStrStart.length() - 1, hobbiesStrStart.length(),"]},");
//                break;
//            }else{
//                hobbiesStrStart.append("{\"hobbyValue\": \"")
//                        .append(hobbies.get(i * 3 + 2))
//                        .append("\",\"hobbyId\": \"1\",\"ifChoose\": \"1\"}")
//                        .append("]},");
//            }
//        }
        hobbiesStrStart.replace(hobbiesStrStart.length() - 1, hobbiesStrStart.length(), "]}");
        return hobbiesStrStart.toString();
    }

    private String getNewHobbiesStrStart(String userId, int numSize) {
        StringBuilder newHobbiesStrStart = new StringBuilder("{\"newHobbies\":[");
        List<String> hobbies = hobbyService.getHobbies(userId, numSize);
        for (int i = 0; i < (numSize / 3); i++) {
            newHobbiesStrStart.append("{\"subHobbies\":[" + "{\"hobbyValue\": \"")
                    .append(hobbies.get(i * 3))
                    .append("\",\"hobbyId\": \"1\",\"ifChoose\": \"1\"},")
                    .append("{\"hobbyValue\": \"")
                    .append(hobbies.get(i * 3 + 1))
                    .append("\",\"hobbyId\": \"1\",\"ifChoose\": \"1\"},")
                    .append("{\"hobbyValue\": \"")
                    .append(hobbies.get(i * 3 + 2))
                    .append("\",\"hobbyId\": \"1\",\"ifChoose\": \"1\"}]},");
        }
        newHobbiesStrStart.replace(newHobbiesStrStart.length() - 1, newHobbiesStrStart.length(), "]}");
        return newHobbiesStrStart.toString();
    }


    @RequestMapping("/uploadAvatar")
    public HashMap<String, String> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile[] files) {
        // 文件地址: yml-x-cloud地址/userId/day/image/filename
        HashMap<String, String> response = new HashMap<>();
        if (files != null && files.length >= 1) {
            try {
                for (MultipartFile file : files) {
                    String contentType = "image";
                    String userId = request.getParameter("userId");
//                    String baseIp = request.getParameter("baseIp");
                    String type = FileUtil.getFileTypePostFix(Objects.requireNonNull(file.getOriginalFilename())); // 后缀
                    String fileName = UUID.randomUUID().toString() + type;
                    String day = new SimpleDateFormat("yyyyMMdd").format(new Date());
                    String filePath = uploadConfig.getUploadPath() + userId + "/" + day + "/" + contentType + "/";
                    FileUtil.uploadFile(file.getBytes(), filePath, fileName);
                    String avatarURL = "/resources/" + userId + "/" + day + "/" + contentType + "/" + fileName;
                    response.put("avatarUrl", avatarURL);
                    response.put("state", "ok");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.put("state", "fail");
            }
        }
        return response;
    }

    @RequestMapping("/getServiceTerms")
    public HashMap<String, Object> getServiceTerms(@RequestBody String body){
        HashMap<String, Object> ret = new HashMap<>(); // 返回值
//        JSONObject object = JSONObject.parseObject(body);
//        String userId = (String) object.get("userId");
        ServiceTerms serviceTerms = serviceTermsService.getLatestServiceTerms();
        ret.put("serviceTermsContent", serviceTerms.getServiceContent());
        ret.put("serviceTermsTime", serviceTerms.getServiceTime());
        ret.put("state", "ok");
        return ret;
    }
}

class SubHobbies {
    private String hobbyValue;
    private int hobbyId;
    private int ifChoose;

}