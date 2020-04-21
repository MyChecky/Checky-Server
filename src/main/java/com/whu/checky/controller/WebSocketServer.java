package com.whu.checky.controller;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whu.checky.domain.FriendChat;
import com.whu.checky.domain.User;
import com.whu.checky.service.FriendChatService;
import com.whu.checky.service.UserService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint("/socket/{userId}/{userSessionId}/{targetUserId}")
@Component
public class WebSocketServer {
    //@ServerEndPoint中是无法使用@Autowired注入Bean的
    private static UserService userService;
    private static FriendChatService friendChatService;

    @Autowired
    public void setFriendChatService(FriendChatService friendChatService){
        WebSocketServer.friendChatService = friendChatService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        WebSocketServer.userService = userService;
    }

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userId
     */
    private String userId = "";
    /**
     * 当前连接对话对象targetUserId
     */
    private String targetUserId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId,
                       @PathParam("userSessionId") String userSessionId,
                       @PathParam("targetUserId") String targetUserId) {
        log.info("新用户尝试连接!");

        User user = userService.queryUser(userId);
        if (user != null && user.getSessionId().equals(userSessionId) && targetUserId != null) {
            log.info("\n\t\tsessionId:{}\n\t\tuserId:{}\n\t\tuserSessionId:{}", session.getId(), userId, userSessionId);
            this.session = session;
            this.userId = userId;
            this.targetUserId = targetUserId;
            if (webSocketMap.containsKey(userId)) {
                webSocketMap.remove(userId);
                webSocketMap.put(userId, this);
                //加入set中
            } else {
                webSocketMap.put(userId, this);
                //加入set中
                addOnlineCount();
                //在线数加1
            }
            log.info("用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());
            try {
                sendMessage("{\"state\": \"init\"}");
            } catch (IOException e) {
                log.error("用户:" + userId + ",网络异常!!!!!!");
            }
        } else {
            log.error("非法访问!\n\t\tsessionId:{}\t\tuserId:{}\n\t\tuserSessionId:{}\n\t\ttargetUserId:{}",
                    session.getId(), userId, userSessionId, targetUserId);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:" + userId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:" + userId + ",报文:" + message);
        //可以群发消息
        //消息保存到数据库、redis
        if (message != null) {
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人(防止串改)
                jsonObject.put("fromUserId", this.userId);
                String fromUserId = jsonObject.getString("fromUserId");
                String toUserId = jsonObject.getString("toUserId");
                String content = jsonObject.getString("content");
                String date = jsonObject.getString("date");
                if(!fromUserId.equals(this.userId) || !toUserId.equals(this.targetUserId)){
                    log.error("有误！\t本身fromUserId:{}\t\t本身toUserId:{}\n\t实际fromUserId:{}\t\t实际toUserId:{}",
                            this.userId, this.targetUserId, fromUserId, toUserId);
                }
                FriendChat friendChat = new FriendChat();
                friendChat.setSendId(fromUserId);
                friendChat.setReceiverId(toUserId);
                friendChat.setChatTime(date);
                friendChat.setChatContent(content);
                //传送给对应toUserId用户的websocket
                if (toUserId != null && webSocketMap.containsKey(toUserId)) {
                    JSONObject ans = new JSONObject();
                    ans.put("content", content);
                    ans.put("date", date);
                    ans.put("state", MyConstants.RESULT_OK);
                    webSocketMap.get(toUserId).sendMessage(ans.toJSONString());
                    friendChat.setIfRead(1); // 对方在聊天框，消息状态已读
                } else {
                    log.error("请求的userId:" + toUserId + "不在该服务器上");
                }
                friendChatService.addFriendChat(friendChat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, @PathParam("userId") String userId) throws IOException {
        log.info("发送消息到:" + userId + "，报文:" + message);
        if (userId != null && webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).sendMessage(message);
        } else {
            log.error("用户" + userId + ",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
