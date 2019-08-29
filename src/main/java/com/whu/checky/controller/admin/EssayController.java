package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Essay;
import com.whu.checky.domain.Record;
import com.whu.checky.domain.User;
import com.whu.checky.service.EssayService;
import com.whu.checky.service.RecordService;
import com.whu.checky.service.UserService;
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

    //展示动态
    @RequestMapping("/all")
    public JSONObject all(@RequestBody String jsonstr){
        JSONObject res=new JSONObject();
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        int currentPage = (Integer) object.get("page");
        Page<Essay> page=new Page<>(currentPage,5);
        List<AdminEssay> adminEssays=new ArrayList<AdminEssay>();
        List<Essay> essays=essayService.displayEssay(page);
        for (Essay essay:essays){
            List<Record> records=recordService.getRecordsByEssayId(essay.getEssayId());
            AdminEssay adminEssay=new AdminEssay();
            User user=userService.queryUser(essay.getUserId());
            adminEssay.setCommentNum(essay.getCommentNum());
            adminEssay.setEssayContent(essay.getEssayContent());
            adminEssay.setEssayId(essay.getEssayId());
            adminEssay.setEssayTime(essay.getEssayTime());
            adminEssay.setLikeNum(essay.getLikeNum());
            adminEssay.setUserName(user.getUserName());
            adminEssay.setImg(records);
            adminEssays.add(adminEssay);
        }
        res.put("state","ok");
        res.put("essays",adminEssays);
        return res;
    }


    class AdminEssay{
        private int commentNum;
        private String essayContent;
        private String essayId;
        private String essayTime;
        private int likeNum;
        private String userName;
        private List<Record> img;

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
