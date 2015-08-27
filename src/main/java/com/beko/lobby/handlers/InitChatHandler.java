package com.beko.lobby.handlers;

import com.beko.beans.dao.ChatDAO;
import com.beko.beans.dao.UserDAO;
import com.beko.beans.entity.ChatMessage;
import com.beko.security.LoginExtension;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ankovalenko on 5/20/2015.
 */
public class InitChatHandler extends BaseClientRequestHandler {

    private UserDAO userDAO;
    private ChatDAO chatDAO;

    @Override
    public void handleClientRequest(User user, ISFSObject isfsObject) {
        if(userDAO==null || chatDAO == null){
            ApplicationContext context = ((LoginExtension)getParentExtension()).getContext();
            userDAO = context.getBean(UserDAO.class);
            chatDAO = context.getBean(ChatDAO.class);
        }
        List<ChatMessage> allChatMessages = chatDAO.getAll();
        List<String> sendMessages = convertChatMessages(allChatMessages);
        ISFSObject data = new SFSObject();
        data.putUtfStringArray("messages", sendMessages);
        send("initMessages", data, user);

    }

    private List<String> convertChatMessages(List<ChatMessage> chatMessageList){
        List<String> result = new ArrayList<String>();
        for(ChatMessage message: chatMessageList){
            result.add(message.getUser().getLogin()+" said "+message.getChatMessage());
        }
        return result;
    }
}
