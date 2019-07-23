package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
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
    private FileService fileService;

    private SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //发表动态
    @RequestMapping("/addEssay")
    public JSONObject addEssay(@RequestBody String jsonstr){
        Essay essay= JSON.parseObject(jsonstr,new TypeReference<Essay>(){});
        essay.setEssayId(UUID.randomUUID().toString());
        essay.setEssayTime(dateFormat.format(new Date()));
        int result=essayService.addEssay(essay);
        JSONObject object=new JSONObject();
        if(result==1){
            object.put("state","OK");
            object.put("essayId",essay.getEssayId());
            //插入成功
        }else {
            object.put("state","FAIL");
            //插入失败
        }
        return object;
    }

    //删除动态
    @RequestMapping("/deleteEssay")
    public JSONObject deleteEssay(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String essayId= (String)object.get("essayId");
//        List<Record> records=recordService.getRecordsByEssayId(essayId);
//        for (Record record:records){
//            recordService.deleteRecordById(record.getRecordId());
//        }
//        List<Comment> comments=commentService.queryCommentByEssayId(essayId);
//        for (Comment comment:comments){
//            commentService.deleteComment(comment.getCommentId());
//        }
////        List<EssayLike> likes=likeService.(essayId);
//        for (Comment comment:comments){
//            commentService.deleteComment(comment.getCommentId());
//        }
        int result=essayService.deleteEssay(essayId);
        JSONObject res=new JSONObject();
        if(result==1){
            object.put("state","OK");
            //插入成功
        }else {
            object.put("state","FAIL");
            //插入失败
        }
        return res;
    }

    //修改动态
    public void modifyEassy(@RequestBody String jsonstr) {
        Essay essay= JSON.parseObject(jsonstr,new TypeReference<Essay>(){});
        essay.setEssayId(UUID.randomUUID().toString());
        int result=essayService.modifyEssay(essay);
        if(result==1){
            //删除成功
        }else {
            //删除失败
        }

    }


    //查看自己的动态
    @RequestMapping("/queryUserEssays")
    public List<EssayAndRecord> queryUserEssays(@RequestBody String jsonstr) {
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String userId= (String)object.get("userId");
        User publisher=userService.queryUser(userId);
        List<EssayAndRecord> res=new ArrayList<EssayAndRecord>();
        List<Essay> essays=essayService.queryUserEssays(userId);
        for (Essay essay:essays){
            List<Record> records=recordService.getRecordsByEssayId(essay.getEssayId());
            EssayAndRecord essayAndRecord=new EssayAndRecord();
            essayAndRecord.setUserId(publisher.getUserId());
            essayAndRecord.setUserAvatar(publisher.getUserAvatar());
            essayAndRecord.setUserName(publisher.getUserName());
            essayAndRecord.setImg(records);
            essayAndRecord.setEssay(essay);
            res.add(essayAndRecord);
        }
        return res;
    }

    //查看单条动态
    @RequestMapping("/queryEssayById")
    public EssayAndRecord queryEssayById(@RequestBody String jsonstr) {
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String essayId= (String)object.get("essayId");
        String userId= (String)object.get("userId");
        Essay essay=essayService.queryEssayById(essayId);
        List<Record> records=recordService.getRecordsByEssayId(essay.getEssayId());
        User publisher=userService.queryUser(essay.getUserId());
        EssayAndRecord essayAndRecord=new EssayAndRecord();
        essayAndRecord.setUserId(publisher.getUserId());
        essayAndRecord.setUserAvatar(publisher.getUserAvatar());
        essayAndRecord.setUserName(publisher.getUserName());
        essayAndRecord.setImg(records);
        essayAndRecord.setEssay(essay);
        //此处有问题还没有查询
        EssayLike essayLike=likeService.queryLike(userId,essay.getEssayId());
        boolean like=essayLike==null?false:true;
        essayAndRecord.setLike(like);
        return essayAndRecord;
    }


    //展示动态
    @RequestMapping("/displayEssay")
    public List<EssayAndRecord> displayEssay(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String userId= (String)object.get("userId");
        List<EssayAndRecord> res=new ArrayList<EssayAndRecord>();
        List<Essay> essays=essayService.displayEssay();
        for (Essay essay:essays){
            List<Record> records=recordService.getRecordsByEssayId(essay.getEssayId());
            User publisher=userService.queryUser(essay.getUserId());
            EssayAndRecord essayAndRecord=new EssayAndRecord();
            essayAndRecord.setUserId(publisher.getUserId());
            essayAndRecord.setUserAvatar(publisher.getUserAvatar());
            essayAndRecord.setUserName(publisher.getUserName());
            essayAndRecord.setImg(records);
            essayAndRecord.setEssay(essay);
            //此处有问题还没有查询
            EssayLike essayLike=likeService.queryLike(userId,essay.getEssayId());
            boolean like=essayLike==null?false:true;
            essayAndRecord.setLike(like);
            res.add(essayAndRecord);
        }
        return res;
    }

    //点赞
    @RequestMapping("/like")
    public JSONObject likeEssay(@RequestBody String jsonstr){
        EssayLike essayLike= JSON.parseObject(jsonstr,new TypeReference<EssayLike>(){});
        essayLike.setAddTime(dateFormat.format(new Date()));
        int res=likeService.Like(essayLike);
        JSONObject object=new JSONObject();
        if(res==1){
            //点赞成功
             object.put("state","OK");
        }else {
            object.put("state","FAIL");
        }
        return object;

    }
    //取消点赞
    @RequestMapping("/unlike")
    public JSONObject unLikeEssay(@RequestBody String jsonstr){
        JSONObject data= (JSONObject) JSON.parse(jsonstr);
        String essayId= (String)data.get("essayId");
        String userId= (String)data.get("userId");
        int res=likeService.UnLike(userId,essayId);
        JSONObject object=new JSONObject();
        if(res==1){
            object.put("state","OK");
        }else {
            object.put("state","FAIL");
        }
        return object;

    }

    //评论
    @RequestMapping("/addComment")
    public JSONObject addEssayComment(@RequestBody String jsonstr){
        Comment comment= JSON.parseObject(jsonstr,new TypeReference<Comment>(){});
        comment.setCommentId(UUID.randomUUID().toString());
        comment.setCommentTime(dateFormat.format(new Date()));
        int res=commentService.addComment(comment);
        JSONObject object=new JSONObject();
        if(res==1){
            //添加评论成功
            List<Comment> comments=commentService.queryCommentByEssayId(comment.getEssayId());
            object.put("state","OK");
            object.put("comments",comments);
        }else {
            object.put("state","FAIL");
        }
        return object;
    }


    //删除评论
    @RequestMapping("/delComment")
    public JSONObject delEssayComment(@RequestBody String jsonstr){
        JSONObject data= (JSONObject) JSON.parse(jsonstr);
        String commentId= (String)data.get("commentId");
        String essayId= (String)data.get("essayId");
        int res=commentService.deleteComment(commentId);
        JSONObject object=new JSONObject();
        if(res==1){
            List<Comment> comments=commentService.queryCommentByEssayId(essayId);
            object.put("state","OK");
            object.put("comments",comments);
        }else {
            object.put("state","FAIL");
        }
        return object;
    }


    //查看评论
    @RequestMapping("/queryComments")
    public List<Comment> queryComment(@RequestBody String jsonstr){
        JSONObject data= (JSONObject) JSON.parse(jsonstr);
        String essayId= (String)data.get("essayId");
        List<Comment> comments=commentService.queryCommentByEssayId(essayId);
        return comments;
    }


    @PostMapping("/file/upload")
    public HashMap<String,String> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile[] files){

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
                    record.setEssayId(request.getParameter("essayId"));
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
class EssayAndRecord{
    private String userId;
    private String userAvatar;
    private String userName;
    private Essay essay;
    private List<Record> img;
    private boolean Like;

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

    public List<Record> getImg() {
        return img;
    }

    public void setImg(List<Record> img) {
        this.img = img;
    }

    public boolean getLike() {
        return Like;
    }

    public void setLike(boolean like) {
        Like = like;
    }
}

