package com.jfms.engine.service.biz;

import com.google.gson.Gson;
import com.jfms.engine.api.converter.JFMSMessageConverter;
import com.jfms.engine.api.model.*;
import com.jfms.engine.dal.UserSessionRepository;
import com.jfms.engine.service.biz.remote.OnlineMessageConverter;
import com.jfms.engine.service.biz.remote.OnlineMessageListener;
import com.jfms.engine.service.biz.remote.api.*;
import com.jfms.engine.service.biz.remote.model.OnlineMessageEntity;
import com.jfms.offline_message.model.OfflineMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by vahid on 4/3/18.
 */
@Component
public class ChatManager implements InitializingBean {

    @Autowired
    OnlineMessageRepository onlineMessageRepository;
    @Autowired
    OnlineMessageConverter onlineMessageConverter;
    @Autowired
    PresenceRepository presenceRepository;
    @Autowired
    LastSeenRepository lastSeenRepository;
    @Autowired
    JFMSMessageConverter jfmsMessageConverter;
    @Autowired
    UserSessionRepository userSessionRepository;
    @Autowired
    OnlineMessageListener onlineMessageListener;
    @Autowired
    OfflineMessageApiClient offlineMessageApiClient;

    Gson gson = new Gson();

    @Override
    public void afterPropertiesSet() throws Exception {
        onlineMessageListener.init(
                onlineMessageConverter,
                userSessionRepository);

        onlineMessageRepository.setMessageListener(onlineMessageListener);
    }


    public void init(JFMSClientLoginMessage jfmsClientLoginMessage, WebSocketSession session) {
        userSessionRepository.addSession(jfmsClientLoginMessage.getUserName(), session);
        presenceRepository.setPresenceStatus(jfmsClientLoginMessage.getUserName(), UserStatus.ONLINE.getValue());
        List<String> offlineMessageList =
                offlineMessageApiClient.consumeMessage(jfmsClientLoginMessage.getUserName());
        String offlineMessageListInJson = gson.toJson(offlineMessageList, List.class);
        try {
            session.sendMessage(new TextMessage(offlineMessageListInJson));
        } catch (IOException e) {
            e.printStackTrace();
            //todo log
        }
    }

    public void sendMessage(JFMSClientSendMessage jfmsClientSendMessage, WebSocketSession session) {
        String messageId = UUID.randomUUID().toString();
        String receiverStatus= presenceRepository.getPresenceStatus(jfmsClientSendMessage.getTo());
        if (receiverStatus == null || receiverStatus.equals(UserStatus.OFFLINE.getValue())){
            OfflineMessage offlineMessage = OfflineMessageProducer.getOfflineMessage(
                    jfmsClientSendMessage.getTo(),
                    gson.toJson(jfmsMessageConverter.clientSendToServerSend(messageId, jfmsClientSendMessage))
            );
            offlineMessageApiClient.produceMessage(offlineMessage);
        }else{
            OnlineMessageEntity onlineMessageEntity = onlineMessageConverter.getOnlineMessageEntity(messageId, jfmsClientSendMessage);
            String redisChannelEntityJson = gson.toJson(onlineMessageEntity);
            onlineMessageRepository.sendMessage(
                    getChannelName(jfmsClientSendMessage.getFrom() , jfmsClientSendMessage.getTo()),
                    redisChannelEntityJson
            );
        }
        String messageIdJson = "{\"id\":\"" + messageId + "\"}";
        try {
            session.sendMessage(new TextMessage(messageIdJson));
        } catch (IOException e) {
            e.printStackTrace();
            //todo log
        }
        //todo save in message history
    }

    public void editMessage(JFMSClientEditMessage jfmsClientEditMessage) {
        JFMSServerEditMessage jfmsServerEditMessage =
                jfmsMessageConverter.clientEditToServerEdit(jfmsClientEditMessage);
        String receiverStatus= presenceRepository.getPresenceStatus(jfmsClientEditMessage.getTo());
        if (receiverStatus == null || receiverStatus.equals(UserStatus.OFFLINE.getValue())){
            OfflineMessage offlineMessage = OfflineMessageProducer.getOfflineMessage(
                    jfmsClientEditMessage.getTo(),
                    gson.toJson(jfmsServerEditMessage)
            );
            offlineMessageApiClient.produceMessage(offlineMessage);
        }else{
            WebSocketSession receiverSession = userSessionRepository.getSession(jfmsClientEditMessage.getTo());
            try {
                receiverSession.sendMessage(new TextMessage(gson.toJson(jfmsServerEditMessage)));
            } catch (IOException e) {
                e.printStackTrace();
                //todo log
            }
        }
        //todo edit in message history
    }

    public void deleteMessage(JFMSClientDeleteMessage jfmsClientDeleteMessage) {
        JFMSServerDeleteMessage jfmsServerDeleteMessage =
                jfmsMessageConverter.clientDeleteToServerDelete(jfmsClientDeleteMessage);
        String receiverStatus= presenceRepository.getPresenceStatus(jfmsClientDeleteMessage.getTo());
        if (receiverStatus == null || receiverStatus.equals(UserStatus.OFFLINE.getValue())){
            OfflineMessage offlineMessage = OfflineMessageProducer.getOfflineMessage(
                    jfmsClientDeleteMessage.getTo(),
                    gson.toJson(jfmsServerDeleteMessage)
            );
            offlineMessageApiClient.produceMessage(offlineMessage);
        }else {
            WebSocketSession session = userSessionRepository.getSession(jfmsClientDeleteMessage.getTo());
            try {
                session.sendMessage(new TextMessage(gson.toJson(jfmsServerDeleteMessage)));
            } catch (IOException e) {
                e.printStackTrace();
                //todo log
            }
        }
        //todo delete from history
    }

    public void sendIsTypingMessage(JFMSClientIsTypingMessage jfmsClientIsTypingMessage) {
        WebSocketSession session = userSessionRepository.getSession(jfmsClientIsTypingMessage.getTo());
        JFMSServerIsTypingMessage jfmsServerIsTypingMessage =
                jfmsMessageConverter.clientIsTypingToServerIsTyping(jfmsClientIsTypingMessage);
        try {
            session.sendMessage(new TextMessage(gson.toJson(jfmsServerIsTypingMessage)));
        } catch (IOException e) {
            e.printStackTrace();
            //todo log
        }
    }

    public void updatePresenceTime(JFMSClientPingMessage jfmsClientPingMessage, WebSocketSession session) {
        //todo think about this
        presenceRepository.changePresenceTime(jfmsClientPingMessage.getFrom(), System.currentTimeMillis());
        try {
            session.sendMessage(new TextMessage("{\"status\":\"ok\"}"));
        } catch (IOException e) {
            e.printStackTrace();
            //todo log
        }
    }

    public void setLeaveTime(JFMSClientConversationLeaveMessage jfmsClientConversationLeaveMessage) {
        lastSeenRepository.setLastSeen(
                jfmsClientConversationLeaveMessage.getFrom(),
                jfmsClientConversationLeaveMessage.getTo(),
                jfmsClientConversationLeaveMessage.getLeaveTime()
        );
        JFMSServerConversationMessage jfmsServerConversationMessage =
                jfmsMessageConverter.clientConversationLeaveToServerConversation(jfmsClientConversationLeaveMessage);
        String receiverStatus = presenceRepository.getPresenceStatus(jfmsClientConversationLeaveMessage.getTo());
        if (receiverStatus == null || receiverStatus.equals(UserStatus.OFFLINE.getValue())){
            OfflineMessage offlineMessage = OfflineMessageProducer.getOfflineMessage(
                    jfmsClientConversationLeaveMessage.getTo(),
                    gson.toJson(jfmsServerConversationMessage)
            );
            offlineMessageApiClient.produceMessage(offlineMessage);
        }else {
            WebSocketSession session = userSessionRepository.getSession(jfmsClientConversationLeaveMessage.getTo());
            try {
                session.sendMessage(new TextMessage(gson.toJson(jfmsServerConversationMessage)));
            } catch (IOException e) {
                e.printStackTrace();
                //todo log
            }
        }

    }

    public void getLeaveTime(JFMSClientConversationInMessage jfmsClientConversationInMessage, WebSocketSession session) {
        Long leaveTime = lastSeenRepository.getLastSeen(
                jfmsClientConversationInMessage.getFrom(),
                jfmsClientConversationInMessage.getTo()
        );
        try {
            session.sendMessage(
                    new TextMessage(
                        gson.toJson(new JFMSServerConversationMessage(jfmsClientConversationInMessage.getTo(), leaveTime))
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
            //todo log
        }
    }

    public void setSeen(JFMSClientSeenMessage jfmsClientSeenMessage) {
        WebSocketSession session = userSessionRepository.getSession(jfmsClientSeenMessage.getTo());
        JFMSServerSeenMessage jfmsServerSeenMessage = jfmsMessageConverter.clientSeentoServerSeen(jfmsClientSeenMessage);
        try {
            session.sendMessage(new TextMessage(gson.toJson(jfmsServerSeenMessage)));
        } catch (IOException e) {
            e.printStackTrace();
            //todo log
        }
    }

    public void removeUserSession(String sessionId) {
        userSessionRepository.removeBySession(sessionId, presenceRepository);
//        presenceRepository.setPresenceStatus(jfmsClientLoginMessage.getUserName(), UserStatus.ONLINE.getValue());

    }


    //---------------------------------

    public String getChannelName(String from, String to){
        if (from.compareTo(to) >= 0)
            return from + to;
        else
            return to + from;

    }
}