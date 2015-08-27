package com.beko.lobby.handlers;

import com.beko.beans.dao.ChatDAO;
import com.beko.beans.dao.UserDAO;
import com.beko.beans.entity.ChatMessage;
import com.beko.lobby.utils.RoomHelper;
import com.beko.lobby.utils.UserHelper;
import com.beko.security.LoginExtension;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Created by ankovalenko on 5/20/2015.
 */
public class ChatMessageHandler extends BaseClientRequestHandler {

    private UserDAO userDAO;
    private ChatDAO chatDAO;


    @Override
    public void handleClientRequest(User user, ISFSObject isfsObject) {
        if(userDAO==null || chatDAO == null){
            ApplicationContext context = ((LoginExtension)getParentExtension()).getContext();
            userDAO = context.getBean(UserDAO.class);
            chatDAO = context.getBean(ChatDAO.class);
        }
        String chatMessage = isfsObject.getUtfString("m");
        com.beko.beans.entity.User userDB = UserHelper.toDBUser(user, userDAO);
        ChatMessage message = new ChatMessage();
        message.setUser(userDB);
        message.setChatMessage(chatMessage);
        chatDAO.create(message);

        ISFSObject data = new SFSObject();
        data.putUtfString("m", chatMessage);
        data.putUtfString("sender", user.getName());
        Room currentRoom = RoomHelper.getCurrentRoom(this);
        List<User> userList = UserHelper.getRecipientsList(currentRoom);
        this.send("chatMessageToAll", data, userList);
    }
}
