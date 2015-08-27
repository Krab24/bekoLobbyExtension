package com.beko.lobby.utils;

import com.beko.beans.dao.UserDAO;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;

import java.util.List;

/**
 * Created by ankovalenko on 5/7/2015.
 */
public class UserHelper {

    public static List<User> getRecipientsList(Room room, User exceptUser) {
        List<User> users = room.getUserList();
        if (exceptUser != null) {
            users.remove(exceptUser);
        }

        return users;

    }

    public static List<User> getRecipientsList(Room currentRoom) {
        return getRecipientsList(currentRoom, null);
    }

    public static com.beko.beans.entity.User toDBUser(User user, UserDAO userDAO){
        String userId = (String) user.getSession().getProperty("authKey");
        return userDAO.findById(userId);
    }
}
