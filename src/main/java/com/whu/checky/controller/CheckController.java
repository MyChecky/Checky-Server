package com.whu.checky.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.config.UploadConfig;
import com.whu.checky.domain.Check;
import com.whu.checky.domain.Essay;
import com.whu.checky.domain.Record;
import com.whu.checky.domain.Task;
import com.whu.checky.service.CheckService;
import com.whu.checky.service.FileService;
import com.whu.checky.service.RecordService;
import com.whu.checky.service.TaskService;
import com.whu.checky.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/check")
public class CheckController {

    @Autowired
    private CheckService checkService;

    @Autowired
    private UploadConfig uploadConfig;

    @Autowired
    private FileService fileService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RecordService recordService;

    @PostMapping("/addCheck")
    public HashMap<String,String> addCheck(@RequestBody String body){
        JSONObject object = JSONObject.parseObject(body);
        Check check = paserJson2NewCheck(object);
        HashMap<String,String> ans =  new HashMap<>();
        String content = object.getString("content");
        try{
            checkService.addCheck(check);
            Record record = new Record();
            record.setCheckId(check.getCheckId());
            record.setRecordContent(content);
            record.setRecordType("text");
            fileService.saveFile2Database(record);
            ans.put("checkId",check.getCheckId());
        }
        catch (Exception ex){
            ex.printStackTrace();
            ans.put("state","fail");
//            return "fail";
        }
        ans.put("state","success");
        return ans;
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


    @PostMapping("/listCheck")
    public List<CheckHistory> listCheck(@RequestBody String body){
        JSONObject object = JSONObject.parseObject(body);
        String userId = (String) object.get("userId");
        int currentPage=(Integer) object.get("cPage");
        Page<Check> page=new Page<>(currentPage,10);
        List<Check> checks=checkService.queryCheckByUserId(userId,page);
        List<CheckHistory> res=new ArrayList<CheckHistory>();
        for(Check check:checks){
            List<Record> records=recordService.getRecordsByCheckId(check.getCheckId());
            Record textRecord=null;
            for (Record record: records){
                if (record.getRecordType().equals("text")){
                    textRecord=record;
                }

            }
            records.remove(textRecord);
            CheckHistory checkHistory=new CheckHistory();
            check.setTaskTitle(taskService.getTitleById(check.getTaskId()));
            checkHistory.setCheck(check);
            checkHistory.setImages(records);
            checkHistory.setText(textRecord);
            res.add(checkHistory);
        }
        return res;
    }

    @PostMapping("/listDayCheck")
    public HashMap<String,Object> getDayList(@RequestBody String body){
        HashMap<String,Object> ans = new HashMap<>();
        JSONObject data = JSON.parseObject(body);
        String date = data.getString("date");
        List<Task> taksList = taskService.queryUserTasks(data.getString("userId"),date);

        List<DayCheckAndTask> toChecks = new ArrayList<>();
        List<DayCheckAndTask> AlreadyChecks = new ArrayList<>();
        List<DayCheckAndTask> unKnownChecks = new ArrayList<>();

        for(Task t:taksList){
            if(t.getTaskState().equals("nomatch")) continue;
            Check check = checkService.getCheckByTask(t.getTaskId(),date);
//            List<Object> temp = new ArrayList<>();
//            temp.add(t);
//            if(check!=null) { //该任务当天没有打卡
//                temp.add(check);
//                if(check.getCheckState().equals("success")){
//                    passChecks.add(temp);
//                }else if(check.getCheckState().equals("fail")){
//                    failChecks.add(temp);
//                }else if(check.getCheckState().equals("unknown")){
//                    unKnownChecks.add(temp);
//                }
//            }
            //taskid,
            DayCheckAndTask ret = new DayCheckAndTask();
            ret.setTaskId(t.getTaskId());
            ret.setTaskTitle(t.getTaskTitle());
            ret.setTaskContent(t.getTaskContent());

            if(check==null){
                toChecks.add(ret);
            }else {
                ret.setCheckId(check.getCheckId());
                ret.setCheckState(check.getCheckState());
                if(check.getCheckState().equals("unknown")) unKnownChecks.add(ret);
                else AlreadyChecks.add(ret);
            }
        }

        ans.put("state","ok");
        ans.put("toCheck",toChecks);
        ans.put("checked",AlreadyChecks);
        ans.put("unknown",unKnownChecks);



        return ans;
    }

    @PostMapping("/updateCheck")
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

    @PostMapping("/deleteCheck")
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
        object = object.getJSONObject("checkInfo");
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
                    String contentType = request.getParameter("type");
//                    String fileName = file.getOriginalFilename();
                    String type = FileUtil.getFileTypePostFix(file.getOriginalFilename());
                    String fileName = UUID.randomUUID().toString() + type;

                    String day = new SimpleDateFormat("yyyyMMdd").format(new Date());
//                    String filePath = request.getSession().getServletContext().getRealPath("/");
                    String filePath = uploadConfig.getUploadPath()  + contentType + "/" + day+ "/";
//                    System.out.println(filePath+fileName);

                    FileUtil.uploadFile(file.getBytes(), filePath, fileName);
                    Record record = new Record();
                    record.setFileAddr("resources/" + contentType + "/" + day + "/"+fileName);
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


class DayCheckAndTask{
    private String taskId;

    private String taskTitle;

    private String taskContent;

    private String checkId;

    private String checkState;

    public String getTaskId() {
        return taskId;
    }

    public String getCheckState() {
        return checkState;
    }

    void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;

    }

    public String getTaskTitle() {
        return taskTitle;
    }

    void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskContent() {
        return taskContent;
    }

    void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }
}



class CheckHistory{
    private Check check;
    private List<Record> images;
    private Record text;

    public Check getCheck() {
        return check;
    }

    public void setCheck(Check check) {
        this.check = check;
    }


    public List<Record> getImages() {
        return images;
    }

    public void setImages(List<Record> images) {
        this.images = images;
    }

    public Record getText() {
        return text;
    }

    public void setText(Record text) {
        this.text = text;
    }
}
