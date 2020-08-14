package com.whu.checky.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.config.UploadConfig;
import com.whu.checky.domain.*;
import com.whu.checky.service.*;
//import com.whu.checky.service.FileService;
import com.whu.checky.util.FileUtil;
import com.whu.checky.util.MyConstants;
import com.whu.checky.util.Util;
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
    private EssayService essayService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskTypeService taskTypeService;

    /**
     * 打卡统计
     */
    @PostMapping("/checkChart")
    public HashMap<String, Object> checkChart(@RequestBody String body) {
        HashMap<String, Object> ret = new HashMap<>(); // return
        String userId = JSONObject.parseObject(body).getString("userId");

        int[] numTimely = checkService.queryUserCheckNumTimely(userId);
        List<MySeries> seriesTime = new ArrayList<>();
        seriesTime.add(new MySeries("第一季度", numTimely[0]));
        seriesTime.add(new MySeries("第二季度", numTimely[1]));
        seriesTime.add(new MySeries("第三季度", numTimely[2]));
        seriesTime.add(new MySeries("第四季度", numTimely[3]));
        ret.put("seriesTime", seriesTime);

        User user = userService.queryUser(userId);
        ret.put("taskTotalNum", user.getTaskNum());
        ret.put("taskPassNum", user.getTaskNumSuc());

        List<Task> tasks = taskService.queryUserTasks(userId, null);
        List<MySeries> seriesType = new ArrayList<>();
        int checkShouldNum = 0; // 应打卡数目
        int checkTotalNum = 0; // 实际打卡数目
        int checkPassNum = 0; // 实际通过数目
        for (Task task : tasks) {
            if (task.getTaskState().equals(MyConstants.TASK_STATE_NOMATCH) ||
                    task.getTaskState().equals(MyConstants.TASK_STATE_SAVE)) {
                continue;
            }
            checkShouldNum += task.getCheckTimes();
            checkTotalNum += task.getCheckNum();
            checkPassNum += task.getCheckPass();
        }
        ret.put("checkShouldNum", checkShouldNum);
        ret.put("checkTotalNum", checkTotalNum);
        ret.put("checkPassNum", checkPassNum);

        ret.put("state", MyConstants.RESULT_OK);
        return ret;
    }

    @PostMapping("/checkDate")
    public HashMap<String, String> checkDate(@RequestBody String body) {
        String ymd = JSONObject.parseObject(body).getString("ymd");
        HashMap<String, String> ret = new HashMap<>();
        String state = Util.judgeDate(ymd) ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL;
        ret.put("state", state);
        return ret;
    }

    @PostMapping("/addCheck")
    public HashMap<String, String> addCheck(@RequestBody String body) {
        JSONObject object = JSONObject.parseObject(body);
        Check check = paserJson2NewCheck(object);
        HashMap<String, String> ans = new HashMap<>();
        String content = object.getString("content");
        String essayId = object.getString("essayId");
        try {
            // check表
            checkService.addCheck(check);
            // task表
            Task task = taskService.queryTask(check.getTaskId());
            task.setCheckNum(task.getCheckNum() + 1);
            taskService.updateTask(task);
            // record表
            Record record = new Record();
            String recordId = UUID.randomUUID().toString().substring(0, 12);
            record.setRecordId(recordId);
            record.setCheckId(check.getCheckId());
            record.setRecordContent(content);
            record.setRecordType("text");
            if (!essayId.equals("noEssay")) {
                record.setEssayId(essayId);
            }
            recordService.addRecord(record);
            ans.put("checkId", check.getCheckId());
        } catch (Exception ex) {
            ex.printStackTrace();
            ans.put("state", MyConstants.RESULT_FAIL);
//            return MyConstants.RESULT_FAIL;
        }
        ans.put("state", MyConstants.RESULT_OK);
        return ans;
    }

    @PostMapping("/queryCheck")
    public Check queryCheck(@RequestBody String body) {
        String checkId = (String) JSON.parse(body);
        List<Check> list = checkService.queryCheck("check_id", checkId);
        if (list.size() == 1)
            return list.get(0);
        else
            return null;
    }


    @PostMapping("/listCheck")
    public List<CheckHistory> listCheck(@RequestBody String body) {
        JSONObject object = JSONObject.parseObject(body);
        String userId = (String) object.get("userId");
        int currentPage = (Integer) object.get("cPage");
        Page<Check> page = new Page<>(currentPage, MyConstants.PAGE_LENGTH);
        List<Check> checks = checkService.queryCheckByUserId(userId, page);
        List<CheckHistory> res = new ArrayList<CheckHistory>();
        for (Check check : checks) {
            List<Record> records = recordService.getRecordsByCheckId(check.getCheckId());
            Record textRecord = null;
            for (Record record : records) {
                if (record.getRecordType().equals("text")) {
                    textRecord = record;
                } else {
                    record.setRecordType(record.getRecordType().substring(0, 5));
                }
            }
            records.remove(textRecord);
            CheckHistory checkHistory = new CheckHistory();
            Task task = taskService.queryTask(check.getTaskId());
            check.setTaskTitle(task.getTaskTitle());
            checkHistory.setCheck(check);
            checkHistory.setFileRecord(records);
            checkHistory.setText(textRecord);
            checkHistory.setTaskType(taskTypeService.queryTaskType(task.getTypeId()).getTypeContent());
            res.add(checkHistory);
        }
        return res;
    }

    @PostMapping("/listDayCheck")
    public HashMap<String, Object> getDayList(@RequestBody String body) {
        HashMap<String, Object> ans = new HashMap<>();
        JSONObject data = JSON.parseObject(body);
        String date = data.getString("date");
        List<Task> taksList = taskService.queryUserTasks(data.getString("userId"), date);

        List<DayCheckAndTask> toChecks = new ArrayList<>();
        List<DayCheckAndTask> AlreadyChecks = new ArrayList<>();
        List<DayCheckAndTask> unKnownChecks = new ArrayList<>();

        for (Task t : taksList) {
            if (t.getTaskState().equals(MyConstants.TASK_STATE_NOMATCH) || t.getTaskState().equals(MyConstants.TASK_STATE_SAVE)) // 余额不足的save状态
                continue;
            Check check = checkService.getCheckByTask(t.getTaskId(), date);
//            List<Object> temp = new ArrayList<>();
//            temp.add(t);
//            if(check!=null) { //该任务当天没有打卡
//                temp.add(check);
//                if(check.getCheckState().equals("success")){
//                    passChecks.add(temp);
//                }else if(check.getCheckState().equals(MyConstants.RESULT_FAIL)){
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

            if (check == null) {
                toChecks.add(ret);//未打卡，应提供现在打卡按钮
            } else {
                ret.setCheckId(check.getCheckId());
                ret.setCheckState(check.getCheckState());
                if (check.getCheckState().equals("unknown"))
                    unKnownChecks.add(ret);//已打卡，但未判决该次打卡结果
                else
                    AlreadyChecks.add(ret);//已打卡且已判决该次打卡结果；应提供申诉按钮
            }
        }
        int supOutDay = Integer.parseInt(parameterService.getValueByParam("time_out_day").getParamValue());
        ans.put("supOutDay", supOutDay);
        ans.put("state", MyConstants.RESULT_OK);
        ans.put("toCheck", toChecks);
        ans.put("checked", AlreadyChecks);
        ans.put("unknown", unKnownChecks);
        return ans;
    }

    @PostMapping("/updateCheck")
    public String updateCheck(@RequestBody String body) {
        JSONObject object = JSONObject.parseObject(body);
        Check check = paserJasonUpdateUser(object);
        try {
            checkService.updateCheck(check);
        } catch (Exception ex) {
            ex.printStackTrace();
            return MyConstants.RESULT_FAIL;
        }
        return MyConstants.RESULT_OK;
    }

    @PostMapping("/deleteCheck")
    public String deleteCheck(@RequestBody String body) {
        String checkId = (String) JSON.parse(body);
        try {
            // 清除record
            List<Record> records = recordService.getRecordsByCheckId(checkId);
            for (Record record : records) {
                recordService.deleteRecordById(record.getRecordId());
            }
            // 可能清除essay
            if (records.size() > 0 && records.get(0).getEssayId() != null) {
                essayService.deleteEssay(records.get(0).getEssayId());
            }
            checkService.deleteCheck(checkId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return MyConstants.RESULT_FAIL;
        }
        return MyConstants.RESULT_OK;
    }

    private Check paserJson2NewCheck(JSONObject object) {
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

    private Check paserJasonUpdateUser(JSONObject object) {
        Check check = paserJson2NewCheck(object);
        check.setCheckState(object.getString("checkState"));
        check.setCheckTime(object.getString("checkTime"));
        check.setPassNum(object.getInteger("passNum"));
        check.setSuperviseNum(object.getInteger("superviseNum"));

        return check;
    }

    @PostMapping("/file/upload")
    public HashMap<String, String> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile[] files) {
        // 文件地址: yml-x-cloud地址/userId/day/fileType/filename
        // fileType: image,video,audio(注意对应数据库是：image,video,audio)
        HashMap<String, String> response = new HashMap<>();
        if (files != null && files.length >= 1) {
            try {
                for (MultipartFile file : files) {
                    // 文件相关
                    String contentType = request.getParameter("type");
                    String userId = request.getParameter("userId");
                    String essayId = request.getParameter("essayId");
                    String type = FileUtil.getFileTypePostFix(file.getOriginalFilename()); // 文件后缀
                    String fileName = UUID.randomUUID().toString() + type;
                    String day = MyConstants.DATE_FORMAT.format(new Date());
                    String filePath = uploadConfig.getUploadPath() + userId + "/" + day + "/" + contentType + "/";
                    FileUtil.uploadFile(file.getBytes(), filePath, fileName);
                    // 数据库相关
                    Record record = new Record();
                    record.setFileAddr(uploadConfig.getStaticPath() + "/" + userId + "/" + day + "/" + contentType + "/" + fileName);
                    record.setRecordType(file.getContentType());
                    String checkId = request.getParameter("checkId");
                    record.setCheckId(checkId);
                    if (!essayId.equals("noEssay")) {
                        record.setEssayId(essayId);
                    }
                    /* 这里必须改变原有逻辑：文本上传record，文件上传到record(仅有checkId),判断是否要分享->文本上传到essay，获得essayId,
       文件重复上传到record(仅有essayId)*/
                    /* 文本上传到essayId, 返回essayId->文本与文件上传到record(含checkId, 可能含essayId),故不需要下面防止文件上传慢的问题*/
//                    // 这里防止因文件上传过慢，导致文本记录的的essayId已设，而文件记录的essayId未能同步
//                    List<Record> records = recordService.getRecordsByCheckId(checkId);
//                    try{
//                        for(Record record1:records){
//                            if(!record1.getEssayId().equals("")){
//                                record.setEssayId(record1.getEssayId());
//                                break;
//                            }
//                        }
//                    }catch (NullPointerException ex){
//                        // 有异常，文件上传成功了但没有essayId
//                        fileService.saveFile2Database(record);
//                    }
//                    fileService.saveFile2Database(record);
                    String recordId = UUID.randomUUID().toString().substring(0, 12);
                    record.setRecordId(recordId);
                    recordService.addRecord(record);
                    response.put("state", MyConstants.RESULT_OK);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.put("state", MyConstants.RESULT_FAIL);
            }
        }
        return response;


    }
}

class MySeries {
    private String name;
    private Integer data;

    public MySeries(String name, Integer data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }
}

class DayCheckAndTask {
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


class CheckHistory {
    private Check check;
    private List<Record> fileRecord;
    private Record text;
    private String taskType;

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public List<Record> getFileRecord() {
        return fileRecord;
    }

    public void setFileRecord(List<Record> fileRecord) {
        this.fileRecord = fileRecord;
    }

    public Check getCheck() {
        return check;
    }

    public void setCheck(Check check) {
        this.check = check;
    }

    public Record getText() {
        return text;
    }

    public void setText(Record text) {
        this.text = text;
    }
}
