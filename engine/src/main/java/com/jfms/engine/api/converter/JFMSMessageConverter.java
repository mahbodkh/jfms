package com.jfms.engine.api.converter;

import com.jfms.engine.api.Method;
import com.jfms.engine.api.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vahid on 4/9/18.
 */
@Component
public class JFMSMessageConverter {
    public JFMSServerEditMessage clientEditToServerEdit(JFMSClientEditMessage jfmsClientEditMessage){
        if (jfmsClientEditMessage == null)
            return null;
        return new JFMSServerEditMessage(
                jfmsClientEditMessage.getId(),
                jfmsClientEditMessage.getFrom(),
                jfmsClientEditMessage.getBody(),
                jfmsClientEditMessage.getSubject(),
                jfmsClientEditMessage.getEditTime(),
                jfmsClientEditMessage.getMethod()
        );
    }

    public JFMSServerDeleteMessage clientDeleteToServerDelete(JFMSClientDeleteMessage jfmsClientDeleteMessage) {
        if (jfmsClientDeleteMessage == null)
            return null;
        return new JFMSServerDeleteMessage(
                    jfmsClientDeleteMessage.getMethod(),
                    jfmsClientDeleteMessage.getIdList(),
                    jfmsClientDeleteMessage.getFrom()
                );
    }

    public JFMSServerIsTypingMessage clientIsTypingToServerIsTyping(JFMSClientIsTypingMessage jfmsClientIsTypingMessage) {
        if (jfmsClientIsTypingMessage == null)
            return null;
        return new JFMSServerIsTypingMessage(
                jfmsClientIsTypingMessage.getMethod(),
                jfmsClientIsTypingMessage.getFrom()
        );
    }

    public JFMSServerConversationMessage clientConversationLeaveToServerConversation(
            JFMSClientConversationLeaveMessage jfmsClientConversationLeaveMessage) {
        if (jfmsClientConversationLeaveMessage == null)
            return null;
        return new JFMSServerConversationMessage(
                Method.CONVERSATION_LEAVE.getValue(),
                jfmsClientConversationLeaveMessage.getFrom(),
                jfmsClientConversationLeaveMessage.getLeaveTime()
        );
    }

    public JFMSServerSeenMessage clientSeentoServerSeen(JFMSClientSeenMessage jfmsClientSeenMessage) {
        if (jfmsClientSeenMessage == null)
            return null;
        return new JFMSServerSeenMessage(
                jfmsClientSeenMessage.getMessageIdList(),
                jfmsClientSeenMessage.getSeenTime(),
                jfmsClientSeenMessage.getFrom()
        );
    }

    public JFMSServerSendMessage clientSendToServerSend(String messageId, JFMSClientSendMessage jfmsClientSendMessage) {
        return new JFMSServerSendMessage(
                jfmsClientSendMessage.getMethod(),
                messageId,
                jfmsClientSendMessage.getFrom(),
                jfmsClientSendMessage.getBody(),
                jfmsClientSendMessage.getSubject(),
                jfmsClientSendMessage.getSendTime()
        );

    }

    public JFMSServerGroupCreationMessage clientGroupCreationToServerGroupCreation(
            String groupId, JFMSClientGroupCreationMessage jfmsClientGroupCreationMessage) {
        return new JFMSServerGroupCreationMessage(
                groupId,
                Method.GROUP_CREATION.getValue(),
                jfmsClientGroupCreationMessage.getDisplayName(),
                jfmsClientGroupCreationMessage.getCreator(),
                jfmsClientGroupCreationMessage.getJfmsGroupMemberMap(),
                jfmsClientGroupCreationMessage.getType()
        );

    }

    public JFMSServerGroupCreationMessage clientGroupInfoEditToServerGroupCreation(
            JFMSClientGroupInfoEditMessage jfmsClientGroupInfoEditMessage) {
        return new JFMSServerGroupCreationMessage(
                jfmsClientGroupInfoEditMessage.getId(),
                Method.GROUP_INFO_EDIT.getValue(),
                jfmsClientGroupInfoEditMessage.getDisplayName(),
                jfmsClientGroupInfoEditMessage.getCreator(),
                jfmsClientGroupInfoEditMessage.getJfmsGroupMemberMap(),
                jfmsClientGroupInfoEditMessage.getType()
        );

    }


    public JFMSServerGroupSendMessage clientSendToServerGroupSend(String messageId, JFMSClientSendMessage jfmsClientGroupSendMessage) {
        return new JFMSServerGroupSendMessage(
                jfmsClientGroupSendMessage.getMethod(),
                jfmsClientGroupSendMessage.getTo(),
                messageId,
                jfmsClientGroupSendMessage.getFrom(),
                jfmsClientGroupSendMessage.getBody(),
                jfmsClientGroupSendMessage.getSubject(),
                jfmsClientGroupSendMessage.getSendTime()
        );
    }

    public JFMSServerGroupEditMessage clientEditToServerGroupEdit(JFMSClientEditMessage jfmsClientGroupEditMessage) {
        return new JFMSServerGroupEditMessage(
                jfmsClientGroupEditMessage.getId(),
                jfmsClientGroupEditMessage.getTo(),
                jfmsClientGroupEditMessage.getFrom(),
                jfmsClientGroupEditMessage.getBody(),
                jfmsClientGroupEditMessage.getSubject(),
                jfmsClientGroupEditMessage.getEditTime(),
                jfmsClientGroupEditMessage.getMethod()
        );
    }

    public JFMSServerGroupDeleteMessage clientDeleteToServerGroupDelete(JFMSClientDeleteMessage jfmsClientGroupDeleteMessage) {
        return new JFMSServerGroupDeleteMessage(
                jfmsClientGroupDeleteMessage.getMethod(),
                jfmsClientGroupDeleteMessage.getTo(),
                jfmsClientGroupDeleteMessage.getIdList(),
                jfmsClientGroupDeleteMessage.getFrom()
        );
    }

    public JFMSServerGroupIsTypingMessage clientIsTypingToServerGroupIsTyping(JFMSClientIsTypingMessage jfmsClientGroupIsTypingMessage) {
        return new JFMSServerGroupIsTypingMessage(
                jfmsClientGroupIsTypingMessage.getMethod(),
                jfmsClientGroupIsTypingMessage.getTo(),
                jfmsClientGroupIsTypingMessage.getFrom()
        );
    }

    public JFMSServerGroupConversationMessage clientConversationLeaveToServerGroupConversation(JFMSClientConversationLeaveMessage jfmsClientGroupConversationLeaveMessage) {
        return new JFMSServerGroupConversationMessage(
                jfmsClientGroupConversationLeaveMessage.getMethod(),
                jfmsClientGroupConversationLeaveMessage.getTo(),
                jfmsClientGroupConversationLeaveMessage.getFrom(),
                jfmsClientGroupConversationLeaveMessage.getLeaveTime()
        );
    }
}
