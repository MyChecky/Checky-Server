package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Comment;
import com.whu.checky.domain.Essay;
import com.whu.checky.domain.Record;
import com.whu.checky.domain.User;
import com.whu.checky.service.*;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/essay")
@Component("AdminEssayController")
public class EssayController {
    @Autowired
    private EssayService essayService;
    @Autowired
    private UserService userService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private TopicService topicService;

    //假删除动态
    @RequestMapping("/delete")
    public JSONObject deleteEssayById(@RequestBody String jsonstr) {
        JSONObject res = new JSONObject();
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String essayId = (String) object.get("essayId");
        Essay essay = essayService.queryEssayById(essayId);
        essay.setIfDelete(1);
        int deleteResult = essayService.updateEssay(essay);
        if (deleteResult == 1)
            res.put("state", MyConstants.RESULT_OK);
        else
            res.put("state", MyConstants.RESULT_FAIL);
        return res;

    }

    //展示动态
    @RequestMapping("/all")
    public JSONObject all(@RequestBody String jsonstr) {
        JSONObject res = new JSONObject();
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        int currentPage = (Integer) object.get("page");
        Integer pageSize = (Integer) object.get("pageSize");
        if (pageSize == null) {
            pageSize = 5;
        }
        Page<Essay> page = new Page<Essay>(currentPage, pageSize);
        List<Essay> essays = essayService.displayEssay(page);
        List<AdminEssay> adminEssays = essays2AdminEssays(essays);
        res.put("state", MyConstants.RESULT_OK);
        res.put("essays", adminEssays);
        res.put("size", (int) Math.ceil(page.getTotal() / (double) pageSize));
        res.put("total", page.getTotal());
        return res;
    }

    private List<AdminEssay> essays2AdminEssays(List<Essay> essays) {
        List<AdminEssay> adminEssays = new ArrayList<>();
        for (Essay essay : essays) {
            adminEssays.add(essay2AdminEssay(essay));
        }
        return adminEssays;
    }

    private AdminEssay essay2AdminEssay(Essay essay) {
        List<Record> records = recordService.getRecordsByEssayId(essay.getEssayId());
        for (Record record : records) {
            if (!record.getRecordType().equals("text"))
                record.setRecordType(record.getRecordType().substring(0, 5));
        }

        AdminEssay adminEssay = new AdminEssay();
        User user = userService.queryUser(essay.getUserId());
        adminEssay.setCommentNum(essay.getCommentNum());
        adminEssay.setEssayContent(essay.getEssayContent());
        adminEssay.setEssayId(essay.getEssayId());
        adminEssay.setEssayTime(essay.getEssayTime());
        adminEssay.setLikeNum(essay.getLikeNum());
        adminEssay.setUserName(user.getUserName());
        adminEssay.setImg(records);

        adminEssay.setTopicName(topicService.getTopicNameById(essay.getTopicId()));

        return adminEssay;
    }

    @RequestMapping("/query")
    public JSONObject query(@RequestBody String jsonstr) {
        JSONObject res = new JSONObject();
        String essayId = JSON.parseObject(jsonstr).getString("essayId");

        Essay essay = essayService.queryEssayById(essayId);

        AdminEssay adminEssay = essay2AdminEssay(essay);

        List<Comment> comments = commentService.queryCommentByEssayId(essayId);

        res.put("essay", adminEssay);
        res.put("comments", comments);
        res.put("state", MyConstants.RESULT_OK);
        return res;
    }

    //根据username模糊搜索的动态
    @RequestMapping("/queryByKeyword")
    public JSONObject queryByKeyword(@RequestBody String jsonstr) {
        JSONObject res = new JSONObject();
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String startTime = object.getString("startTime");
        startTime = startTime != null && !startTime.equals("") ? startTime : MyConstants.START_TIME;
        String endTime = object.getString("endTime");
        endTime = endTime != null && !endTime.equals("") ? endTime : MyConstants.END_TIME;

        String keyword = object.getString("keyword");
        String searchType = object.getString("searchType");
        Integer page = object.getInteger("page");
        Integer pageSize = object.getInteger("pageSize");

        Page<Essay> p = new Page<>(page, pageSize);
        List<Essay> essays = new ArrayList<Essay>();

        if (keyword == null || keyword.equals("")) {
            essays = essayService.queryEssaysAll(p, startTime, endTime);
        } else if (searchType.equals("nickname")) {
            essays = essayService.queryEssaysLikeNickname(p, startTime, endTime, keyword);
        } else if (searchType.equals("content")) {
            essays = essayService.queryEssaysLikeContent(p, startTime, endTime, keyword);
        } else {
            res.put("state", MyConstants.RESULT_FAIL);
            return res;
        }

        List<AdminEssay> adminEssays = essays2AdminEssays(essays);
        res.put("total", p.getTotal());
        res.put("size", (int) Math.ceil(p.getTotal() / (double) pageSize));
        res.put("state", MyConstants.RESULT_OK);
        res.put("essays", adminEssays);

        return res;
    }


    class AdminEssay {
        private int commentNum;
        private String essayContent;
        private String essayId;
        private String essayTime;
        private int likeNum;
        private String userName;
        private List<Record> img;
        private String topicName;

        public String getTopicName() {
            return topicName;
        }

        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public String getEssayContent() {
            return essayContent;
        }

        public void setEssayContent(String essayContent) {
            this.essayContent = essayContent;
        }

        public String getEssayId() {
            return essayId;
        }

        public void setEssayId(String essayId) {
            this.essayId = essayId;
        }

        public String getEssayTime() {
            return essayTime;
        }

        public void setEssayTime(String essayTime) {
            this.essayTime = essayTime;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public List<Record> getImg() {
            return img;
        }

        public void setImg(List<Record> img) {
            this.img = img;
        }
    }


}
