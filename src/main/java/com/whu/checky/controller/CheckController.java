package com.whu.checky.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whu.checky.config.UploadConfig;
import com.whu.checky.domain.Check;
import com.whu.checky.domain.Record;
import com.whu.checky.service.CheckService;
import com.whu.checky.service.FileService;
import com.whu.checky.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/check")
public class CheckController {

    @Autowired
    private CheckService checkService;

    @Autowired
    private UploadConfig uploadConfig;

    @Autowired
    private FileService fileService;

    @PostMapping("/addCheck")
    public String addCheck(@RequestBody String body){
        JSONObject object = JSONObject.parseObject(body);
        Check check = paserJson2NewCheck(object);

        try{
            checkService.addCheck(check);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return "fail";
        }
        return "success";
    }

    @PostMapping("/queryCheck")
    public Check queryCheck(@RequestBody String body){
        String checkId = (String) JSON.parse(body);
        List<Check> list = checkService.queryCheck("check_id", checkId);
        if(list.size()==1)
            return list.get(0);
        else
            return null;
    }

    @PostMapping("/Checky/check/listCheck")
    public List<Check> listCheck(@RequestBody String body){
        String userId = (String) JSON.parse(body);
        return checkService.queryCheck("user_id", userId);
    }

    @PostMapping("/Checky/check/updateCheck")
    public String updateCheck(@RequestBody String body){
        JSONObject object = JSONObject.parseObject(body);
        Check check = paserJasonUpdateUser(object);

        try{
            checkService.updateCheck(check);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return "fail";
        }

        return "success";
    }

    @PostMapping("/Checky/check/deleteCheck")
    public String deleteCheck(@RequestBody String body){
        String checkId = (String) JSON.parse(body);

        try{
            checkService.deleteCheck(checkId);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return "fail";
        }

        return "success";
    }

    private Check paserJson2NewCheck(JSONObject object){
        //解析json获取登录信息
        String userId = object.getString("userId");
        String taskId = object.getString("taskId");
        String checkTime = object.getString("checkTime");

        //构造登录用户
        Check check = new Check();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        check.setCheckId(uuid);
        check.setUserId(userId);
        check.setTaskId(taskId);
        check.setCheckTime(checkTime);

        return check;
    }

    private Check paserJasonUpdateUser(JSONObject object){
        Check check = paserJson2NewCheck(object);
        check.setCheckState(object.getString("checkState"));
        check.setCheckTime(object.getString("checkTime"));
        check.setPassNum(object.getInteger("passNum"));
        check.setSuperviseNum(object.getInteger("superviseNum"));

        return check;
    }





//    //    @Value("${web.upload.path}")
//    private String uploadPath="D:/img";

//    @Value("${media.save.urlprefix}")
//    private String urlprefix;

    @PostMapping("/file/upload")
    public HashMap<String,String> uploadFile(HttpServletRequest request, @RequestParam("file")MultipartFile[] files){

        HashMap<String,String> response = new HashMap<>();

        if(files!=null && files.length>=1) {

            try {
                for(MultipartFile file:files){
//                    String contentType = file.getContentType();
//                    String fileName = file.getOriginalFilename();
                    String type = FileUtil.getFileTypePostFix(file.getOriginalFilename());
                    String fileName = UUID.randomUUID().toString() + type;

//                    String filePath = request.getSession().getServletContext().getRealPath("/");
                    String filePath = uploadConfig.getUploadPath() + type.substring(1) + "/";
//                    System.out.println(filePath+fileName);

                    FileUtil.uploadFile(file.getBytes(), filePath, fileName);
                    Record record = new Record();
                    record.setFileAddr("resources/"+ type.substring(1) + "/"+fileName);
                    record.setRecordType(file.getContentType());
                    record.setCheckId(request.getParameter("checkId"));
                    fileService.saveFile2Database(record);
//                    response.put("recordId",recordId);
                    response.put("state","ok");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.put("state","fail");
            }

        }
        return response;


    }
}
