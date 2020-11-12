package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.*;
import com.whu.checky.service.*;
import com.whu.checky.util.MyConstants;
import com.whu.checky.util.MyStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@RestController()
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private UserFriendService userFriendService;

    @Autowired
    private FriendChatService friendChatService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @Autowired
    private EssayService essayService;

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private TopicService topicService;


    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping("/queryIfFriend")
    public HashMap<String, Object> queryIfFriend(@RequestBody String jsonstr){
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String userId = (String) object.get("userId");
        String targetUserId = (String) object.get("targetUserId");
        List<UserFriend> userFriends= userFriendService.queryUserFriends(userId);
        HashMap<String, Object> ans = new HashMap<>();
        for(UserFriend userFriend: userFriends){
            if(userFriend.getToUserId().equals(targetUserId) || userFriend.getFromUserId().equals(targetUserId)){
                ans.put("state", MyConstants.RESULT_OK);
                ans.put("ifFriend", 1); // 这里的1表示是好友
                return ans;
            }
        }
        ans.put("state", MyConstants.RESULT_OK);
        ans.put("ifFriend", 0); // 这里的0表示还不是好友
        return ans;
    }

    // 初始化聊天消息列表
    @PostMapping("/initMsgList")
    public HashMap<String, Object> initMsgList(@RequestBody String jsonstr) throws ParseException {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String userId = (String) object.get("userId");
        String targetUserId = (String) object.get("targetUserId");
        List<FriendChat> friendChats = friendChatService.queryMsgsFromFriends(userId, targetUserId);
        HashMap<String, Object> ans = new HashMap<>();
        List<MsgList> msgLists = new ArrayList<>();
        Date dateTmp = MyConstants.DATETIME_FORMAT.parse("1970-01-01 00:00:00");
        int timeDiff = 1000 * 60 * 3; // 3分钟
        for (int i = 0; i < friendChats.size(); ) {
            MsgList msgList = new MsgList();
            // date.getTime() 返回时间的毫秒数值
            if ((MyConstants.DATETIME_FORMAT.parse(friendChats.get(i).getChatTime())).getTime() - dateTmp.getTime() > timeDiff) {
                msgList.setSpeaker("time");
                msgList.setDate(friendChats.get(i).getChatTime());
                msgList.setContent(friendChats.get(i).getChatTime());
                dateTmp = MyConstants.DATETIME_FORMAT.parse(friendChats.get(i).getChatTime());
            } else {
                if (friendChats.get(i).getSendId().equals(userId))
                    msgList.setSpeaker("customer");
                else{
                    msgList.setSpeaker("server");
                    if(friendChats.get(i).getIfRead() == 0){
                        friendChats.get(i).setIfRead(1);
                        friendChatService.updateFriendChat(friendChats.get(i)); // 设为已读
                    }
                }
                dateTmp = MyConstants.DATETIME_FORMAT.parse(friendChats.get(i).getChatTime());
                msgList.setDate(friendChats.get(i).getChatTime());
                msgList.setContent(friendChats.get(i).getChatContent());
                i++;
            }
            msgLists.add(msgList);
        }
        ans.put("state", MyConstants.RESULT_OK);
        ans.put("msgLists", msgLists);
        return ans;
    }

    // 发送添加好友
    @PostMapping("/tryAddFriend")
    public String tryAddFriend(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String userId = (String) object.get("userId");
        String addContent = (String) object.get("addContent");
        String targetUserId = (String) object.get("targetUserId");
        UserFriend userFriend = new UserFriend();
        userFriend.setFromUserId(userId);
        userFriend.setToUserId(targetUserId);
        userFriend.setAddContent(addContent);
        userFriend.setAddTime(MyConstants.DATETIME_FORMAT.format(new Date()));

        int result = userFriendService.addUserFriend(userFriend);
        if (result == 0) {
            //添加失败
            return "addSuggestion Fail";
        } else {
            //添加成功
            return "addSuggestion Success";
        }
    }

    // 打开某个人的主页显示的动态列表
    @PostMapping("/displayEssaysForSomeone")
    public List<EssayAndRecord> displayEssaysForSomeone(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        int currentPage = (Integer) object.get("cPage");
        String userId = (String) object.get("userId");
        String targetUserId = (String) object.get("targetUserId");

        Page<Essay> page = new Page<>(currentPage, 5);
        List<EssayAndRecord> res = new ArrayList<EssayAndRecord>();
        List<Essay> essays = essayService.displayEssay(targetUserId, page);
        for (Essay essay : essays) {
            if (essay.getTopicId() != null && !essay.getTopicId().equals(""))
                essay.setTopicName(topicService.getTopicNameById(essay.getTopicId()));
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
        if(MyStringUtil.isEmpty(publisher.getUserAvatar()) && publisher.getUserAvatar().length() > 11){
            if(publisher.getUserAvatar().substring(0, 11).equals("/resources/")){
                String baseIp = parameterService.getValueByParam("baseIp").getParamValue();
                essayAndRecord.setUserAvatar(baseIp + publisher.getUserAvatar());
            }else{
                essayAndRecord.setUserAvatar(publisher.getUserAvatar());
            }
        }else{
            essayAndRecord.setUserAvatar("");
        }
        essayAndRecord.setUserName(publisher.getUserName());
        essayAndRecord.setFileRecord(records);
        essayAndRecord.setEssay(essay);
        EssayLike essayLike = likeService.queryLike(essay.getUserId(), essay.getEssayId());
        boolean like = essayLike != null;
        essayAndRecord.setLike(like);
        return essayAndRecord;
    }

    @PostMapping("/queryUserByNickname")
    public HashMap<String, Object> queryUserByNickname(@RequestBody String body) {
        HashMap<String, Object> ans = new HashMap<>();
        JSONObject data = JSON.parseObject(body);
        String userId = data.getString("userId");
        String nickName = data.getString("nickName");
        List<User> users = userService.queryUserByNickname(nickName);
        List<FriendListWithMessage> friendListWithMessages = new ArrayList<>();
        for(User user: users){
            FriendListWithMessage friendListWithMessage = new FriendListWithMessage();
            friendListWithMessage.setUserId(user.getUserId());
            friendListWithMessage.setContent(user.getUserName());
            friendListWithMessage.setUserTime(user.getUserTime());
            if(MyStringUtil.isEmpty(user.getUserAvatar()) && user.getUserAvatar().length() > 11){
                if(user.getUserAvatar().substring(0, 11).equals("/resources/")) {
                    String baseIp = parameterService.getValueByParam("baseIp").getParamValue();
                    friendListWithMessage.setAvatarUrl(baseIp + user.getUserAvatar());
                }else{
                    friendListWithMessage.setAvatarUrl(user.getUserAvatar());
                }
            }else{
                friendListWithMessage.setAvatarUrl("");
            }
            friendListWithMessages.add(friendListWithMessage);
        }
        ans.put("state", MyConstants.RESULT_OK);
        ans.put("friendList", friendListWithMessages);
        return ans;
    }

    // 更改好友申请状态
    @PostMapping("/opNewFriend")
    public HashMap<String, Object> opNewFriend(@RequestBody String body) {
        HashMap<String, Object> ans = new HashMap<>();
        JSONObject data = JSON.parseObject(body);
        String op = data.getString("op");
        String userId = data.getString("userId");
        String targetUserId = data.getString("targetUserId");
        userFriendService.updateUserFriend(targetUserId, userId, Integer.parseInt(op));
        ans.put("state", MyConstants.RESULT_OK);
        return ans;
    }

    // 查询某人好友申请列表
    @PostMapping("/queryNewFriend")
    public HashMap<String, Object> queryNewFriend(@RequestBody String body) {
        HashMap<String, Object> ans = new HashMap<>();
        JSONObject data = JSON.parseObject(body);
        String userId = data.getString("userId");
        List<UserFriend> userFriends = userFriendService.queryUserNewFriends(userId);
        List<FriendListWithMessage> friendListWithMessages = new ArrayList<>();
        for (UserFriend userFriend : userFriends) {
            FriendListWithMessage friendListWithMessage = new FriendListWithMessage();
            friendListWithMessage.setContent(userFriend.getFriendName());
            friendListWithMessage.setAvatarUrl(userFriend.getFriendAvatar());
            friendListWithMessage.setSubContent(userFriend.getAddContent());
            friendListWithMessage.setUserId(userFriend.getFromUserId());
            friendListWithMessage.setDate(userFriend.getAddTime());
            friendListWithMessages.add(friendListWithMessage);
        }
        ans.put("state", MyConstants.RESULT_OK);
        ans.put("friendList", friendListWithMessages);
        return ans;
    }

    // 查询一个人所有的朋友
    @PostMapping("/queryUserAllFriend")
    public HashMap<String, Object> getDayList(@RequestBody String body) {
        HashMap<String, Object> ans = new HashMap<>();
        JSONObject data = JSON.parseObject(body);
        String userId = data.getString("userId");
        List<UserFriend> userFriends = userFriendService.queryUserFriends(userId);
        List<FriendListWithMessage> friendListWithMessages = new ArrayList<>();
        for (UserFriend userFriend : userFriends) {
            FriendListWithMessage friendListWithMessage = new FriendListWithMessage();
            friendListWithMessage.setContent(userFriend.getFriendName());
            friendListWithMessage.setAvatarUrl(userFriend.getFriendAvatar());
            if(userFriend.getToUserId().equals(userId)){
                friendListWithMessage.setUserId(userFriend.getFromUserId());
            }else{
                friendListWithMessage.setUserId(userFriend.getToUserId());
            }
            FriendChat friendChat = friendChatService.queryLatestMessageByIds(userFriend.getToUserId(),
                    userFriend.getFromUserId());
            try {
                friendListWithMessage.setDate(friendChat.getChatTime());
                friendListWithMessage.setSubContent(friendChat.getChatContent());
            } catch (NullPointerException ex) {

            }
            friendListWithMessages.add(friendListWithMessage);
        }
        ans.put("state", MyConstants.RESULT_OK);
        ans.put("friendList", friendListWithMessages);
        return ans;
    }
}

class MsgList {
    private String speaker;
    private String contentType = "text";
    private String content;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

class FriendListWithMessage {
    private String content;
    private String subContent = "";
    private String date = "";
    private String operation = "";
    private String userId;
    private String avatarUrl;
    private String userTime;

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubContent() {
        return subContent;
    }

    public void setSubContent(String subContent) {
        this.subContent = subContent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
