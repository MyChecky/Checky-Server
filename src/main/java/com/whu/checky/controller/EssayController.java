package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.config.UploadConfig;
import com.whu.checky.domain.*;
import com.whu.checky.service.*;
import com.whu.checky.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/essay")
public class EssayController {
    @Autowired
    private EssayService essayService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UploadConfig uploadConfig;

    @Autowired
    private ParameterService parameterService;

//    @Autowired
//    private FileService fileService;



    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //发表动态
    @RequestMapping("/addEssay")
    public JSONObject addEssay(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String userId = (String) object.get("userId");
        String essayContent = (String) object.get("essayContent");
//        String checkId = (String) object.get("checkId");
        String longitude = (String) object.get("longitude");
        String latitude = (String) object.get("latitude");

        // 在Essay表添加记录
        Essay essay = new Essay();
        String essayId = UUID.randomUUID().toString();
        essay.setEssayId(essayId);
        essay.setEssayTime(dateFormat.format(new Date()));
        essay.setLatitude(latitude);
        essay.setLongtitude(longitude);
        essay.setUserId(userId);
        essay.setEssayContent(essayContent);
        int result = essayService.addEssay(essay);
        // 在record表相关记录添加essayId信息
        // 似乎因为上传时间过慢导致此时查询record时，找不到文件类型的记录，在文件上传里做了补充
//        List<Record> records = recordService.getRecordsByCheckId(checkId);
//        for (Record record : records) {
//            record.setEssayId(essayId);
//            recordService.updateRecord(record);
//        }
        JSONObject ans = new JSONObject();
        if (result == 1) {
            ans.put("state", "OK");
            ans.put("essayId", essay.getEssayId());  // 插入成功
        } else {
            ans.put("state", "FAIL"); // 插入失败
        }
        return ans;
    }

    //删除动态
    @RequestMapping("/deleteEssay")
    public List<EssayAndRecord> deleteEssay(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String essayId = (String) object.get("essayId");
        String userId = (String) object.get("userId");
        // 这里是假删除
        Essay essayToDelete = essayService.queryEssayById(essayId);
        essayToDelete.setIfDelete(1);
        int deleteResult = essayService.updateEssay(essayToDelete);

        if(deleteResult == 1){
            List<EssayAndRecord> res = new ArrayList<>();
            List<Essay> essays = essayService.queryUserEssays(userId);
            for (Essay essay : essays) {
                EssayAndRecord essayAndRecord = getEssayAndRecord(essay);
                res.add(essayAndRecord);
            }
            return res;
        }else{
            return null;
        }
    }

    //修改动态
    public void modifyEassy(@RequestBody String jsonstr) {
        Essay essay = JSON.parseObject(jsonstr, new TypeReference<Essay>() {
        });
        essay.setEssayId(UUID.randomUUID().toString());
        int result = essayService.modifyEssay(essay);
        if (result == 1) {
            //删除成功
        } else {
            //删除失败
        }
    }

    //查看自己的动态
    @RequestMapping("/queryUserEssays")
    public List<EssayAndRecord> queryUserEssays(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String userId = (String) object.get("userId");
        List<EssayAndRecord> res = new ArrayList<>();
        List<Essay> essays = essayService.queryUserEssays(userId);
        for (Essay essay : essays) {
            EssayAndRecord essayAndRecord = getEssayAndRecord(essay);
            res.add(essayAndRecord);
        }
        return res;
    }

    //查看单条动态-->只有显示在小程序页面，才会有调用此可能；故无需判别是否已删除
    @RequestMapping("/queryEssayById")
    public EssayAndRecord queryEssayById(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String essayId = (String) object.get("essayId");
//        String userId = (String) object.get("userId");
        Essay essay = essayService.queryEssayById(essayId);
        EssayAndRecord essayAndRecord = getEssayAndRecord(essay);
        return essayAndRecord;
    }


    //展示动态
    @RequestMapping("/displayEssay")
    public List<EssayAndRecord> displayEssay(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
//        String userId = (String) object.get("userId");
        int currentPage = (Integer) object.get("cPage");
        Page<Essay> page = new Page<>(currentPage, 5);
        List<EssayAndRecord> res = new ArrayList<EssayAndRecord>();
        List<Essay> essays = essayService.displayEssay(page);
        for (Essay essay : essays) {
            EssayAndRecord essayAndRecord = getEssayAndRecord(essay);
            res.add(essayAndRecord);
        }
        return res;
    }

    // 要返回给前端的动态列表，多处调用
    private EssayAndRecord getEssayAndRecord(Essay essay) {
        List<Record> records = recordService.getRecordsByEssayId(essay.getEssayId());
        for (int i = 0; i < records.size(); ) {
            if (records.get(i).getRecordType().equals("text")) {
                records.remove(i);
            } else {
                records.get(i).setRecordType(records.get(i).getRecordType().substring(0, 5));
                i++;
            }
        }
        User publisher = userService.queryUser(essay.getUserId());
        EssayAndRecord essayAndRecord = new EssayAndRecord();
        essayAndRecord.setUserId(publisher.getUserId());
        if(publisher.getUserAvatar().substring(0, 11).equals("/resources/")){
            String baseIp = parameterService.getValueByParam("baseIp").getParamValue();
            essayAndRecord.setUserAvatar(baseIp + publisher.getUserAvatar());
        }else{
            essayAndRecord.setUserAvatar(publisher.getUserAvatar());
        }
        essayAndRecord.setUserName(publisher.getUserName());
        essayAndRecord.setFileRecord(records);
        essayAndRecord.setEssay(essay);
//            List<EssayLike> essayLikes = likeService.queryAllLikeByEssayId(essay.getEssayId());
//            essayAndRecord.setLikeNum(essayLikes.size());
//            essayAndRecord.setCommentNum(commentService.queryCommentByEssayId(essay.getEssayId()).size());
        EssayLike essayLike = likeService.queryLike(essay.getUserId(), essay.getEssayId());
        boolean like = essayLike != null;
        essayAndRecord.setLike(like);
        return essayAndRecord;
    }

    //点赞
    @RequestMapping("/like")
    public JSONObject likeEssay(@RequestBody String jsonstr) {
        EssayLike essayLike = JSON.parseObject(jsonstr, new TypeReference<EssayLike>() {
        });
        essayLike.setAddTime(dateFormat.format(new Date()));
        int res = likeService.Like(essayLike);
        JSONObject object = new JSONObject();
        if (res == 1) {
            //点赞成功
            object.put("state", "OK");
        } else {
            object.put("state", "FAIL");
        }
        return object;

    }

    //取消点赞
    @RequestMapping("/unlike")
    public JSONObject unLikeEssay(@RequestBody String jsonstr) {
        JSONObject data = (JSONObject) JSON.parse(jsonstr);
        String essayId = (String) data.get("essayId");
        String userId = (String) data.get("userId");
        int res = likeService.UnLike(userId, essayId);
        JSONObject object = new JSONObject();
        if (res == 1) {
            object.put("state", "OK");
        } else {
            object.put("state", "FAIL");
        }
        return object;

    }

    //评论
    @RequestMapping("/addComment")
    public JSONObject addEssayComment(@RequestBody String jsonstr) {
        Comment comment = JSON.parseObject(jsonstr, new TypeReference<Comment>() {
        });
        comment.setCommentId(UUID.randomUUID().toString());
        comment.setCommentTime(dateFormat.format(new Date()));
        int res = commentService.addComment(comment);
        JSONObject object = new JSONObject();
        if (res == 1) {
            //添加评论成功
            List<Comment> comments = commentService.queryCommentByEssayId(comment.getEssayId());
            object.put("state", "OK");
            object.put("comments", comments);
        } else {
            object.put("state", "FAIL");
        }
        return object;
    }

    //删除评论
    @RequestMapping("/delComment")
    public JSONObject delEssayComment(@RequestBody String jsonstr) {
        JSONObject res = new JSONObject();
        JSONObject data = (JSONObject) JSON.parse(jsonstr);
        String commentId = (String) data.get("commentId");
        String essayId = (String) data.get("essayId");
        Comment comment = commentService.queryCommentById(commentId);
        comment.setIfDelete(1);
        int deleteResult = commentService.updateComment(comment);
        if (deleteResult == 1) {
            Essay essay = essayService.queryEssayById(essayId);
            essay.setCommentNum(essay.getCommentNum() - 1);
            essayService.updateEssay(essay);
            List<Comment> comments = commentService.queryCommentByEssayId(essayId);
            res.put("state", "OK");
            res.put("comments", comments);
        } else {
            res.put("state", "FAIL");
        }
        return res;
    }


    //查看评论
    @RequestMapping("/queryComments")
    public List<Comment> queryComment(@RequestBody String jsonstr) {
        JSONObject data = (JSONObject) JSON.parse(jsonstr);
        String essayId = (String) data.get("essayId");
        return commentService.queryCommentByEssayId(essayId);
    }


    @PostMapping("/file/upload")
    public HashMap<String, String> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile[] files) {
        HashMap<String, String> response = new HashMap<>();
        if (files != null && files.length >= 1) {
            try {
                for (MultipartFile file : files) {
                    String contentType = request.getParameter("type");
//                    String fileName = file.getOriginalFilename();
                    String type = FileUtil.getFileTypePostFix(file.getOriginalFilename());
                    String fileName = UUID.randomUUID().toString() + type;

                    String day = new SimpleDateFormat("yyyyMMdd").format(new Date());
//                    String filePath = request.getSession().getServletContext().getRealPath("/");
                    String filePath = uploadConfig.getUploadPath() + contentType + "/" + day + "/";
//                    System.out.println(filePath+fileName);

                    FileUtil.uploadFile(file.getBytes(), filePath, fileName);
                    Record record = new Record();
                    record.setFileAddr(uploadConfig.getStaticPath() + "/" + contentType + "/" + day + "/" + fileName);
                    record.setRecordType(file.getContentType());
                    record.setEssayId(request.getParameter("essayId"));
                    String recordId = UUID.randomUUID().toString().substring(0, 12);
                    record.setRecordId(recordId);
                    recordService.addRecord(record);
//                    fileService.saveFile2Database(record);
//                    response.put("recordId",recordId);
                    response.put("state", "ok");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.put("state", "fail");
            }
        }
        return response;
    }
}

class EssayAndRecord {
    private String userId;
    private String userAvatar;
    private String userName;
    private Essay essay;
    private List<Record> fileRecord;
    private boolean Like;
//    private int likeNum;
//    private int commentNum;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Essay getEssay() {
        return essay;
    }

    public void setEssay(Essay essay) {
        this.essay = essay;
    }

    public List<Record> getFileRecord() {
        return fileRecord;
    }

    public void setFileRecord(List<Record> fileRecord) {
        this.fileRecord = fileRecord;
    }

    public boolean getLike() {
        return Like;
    }

    public void setLike(boolean like) {
        Like = like;
    }

//    public int getLikeNum() {
//        return likeNum;
//    }
//
//    public void setLikeNum(int likeNum) {
//        this.likeNum = likeNum;
//    }
//
//    public int getCommentNum() {
//        return commentNum;
//    }
//
//    public void setCommentNum(int commentNum) {
//        this.commentNum = commentNum;
//    }
}

