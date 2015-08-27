package com.beko.lobby.handlers;

import com.beko.lobby.simulation.World;
import com.beko.lobby.utils.RoomHelper;
import com.beko.lobby.utils.UserHelper;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import java.util.List;

/**
 * Created by ankovalenko on 5/22/2015.
 */
public class ExitGameHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject isfsObject) {
        World world = RoomHelper.getWorld(this);
        world.removePlayer(user);
        ISFSObject data = new SFSObject();
        data.putInt("id", user.getId());
        Room currentRoom = RoomHelper.getCurrentRoom(this);
        List<User> userList = UserHelper.getRecipientsList(currentRoom, user);
        this.send("userExitGame", data, userList);
    }
}
