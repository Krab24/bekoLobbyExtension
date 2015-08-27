package com.beko.lobby;

import com.beko.beans.dao.ChatDAO;
import com.beko.beans.dao.UserDAO;
import com.beko.lobby.handlers.ChatMessageHandler;
import com.beko.lobby.handlers.ExitGameHandler;
import com.beko.lobby.handlers.GetTimeHandler;
import com.beko.lobby.handlers.InitChatHandler;
import com.beko.lobby.handlers.SendTransformHandler;
import com.beko.lobby.handlers.ShotHandler;
import com.beko.lobby.handlers.SpawnMeHandler;
import com.beko.lobby.simulation.Player;
import com.beko.lobby.simulation.World;
import com.beko.lobby.utils.RoomHelper;
import com.beko.lobby.utils.UserHelper;
import com.beko.security.LoginEventHandler;
import com.beko.security.LoginExtension;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.ExtensionLogLevel;

import java.util.List;

/**
 * Created by ankovalenko on 5/7/2015.
 */
public class LobbyExtension extends LoginExtension {

    private World world; // Reference to World simulation model

    public World getWorld() {
        return world;
    }

    @Override
    public void init() {
        world = new World(this);
        addRequestHandler("sendTransform", SendTransformHandler.class);
        addRequestHandler("spawnMe", SpawnMeHandler.class);
        addRequestHandler("getTime", GetTimeHandler.class);
        addRequestHandler("shot", ShotHandler.class);
        addRequestHandler("chatMessage", ChatMessageHandler.class);
        addRequestHandler("initChat", InitChatHandler.class);
        addRequestHandler("exitGame", ExitGameHandler.class);

        trace(ExtensionLogLevel.INFO, "INIT LOBBY EXTENSION!");
    }

    // Send instantiate new player message to all the clients
    public void clientInstantiatePlayer(Player player) {
        clientInstantiatePlayer(player, null);
    }

    //Send the player instantiation message to all the clients or to a specified user only
    public void clientInstantiatePlayer(Player player, User targetUser) {
        ISFSObject data = new SFSObject();

        player.toSFSObject(data);
        Room currentRoom = RoomHelper.getCurrentRoom(this);
        if (targetUser == null) {
            // Sending to all the users
            List<User> userList = UserHelper.getRecipientsList(currentRoom);
            this.send("spawnPlayer", data, userList);
        }
        else {
            // Sending to the specified user
            this.send("spawnPlayer", data, targetUser);
        }
    }

    // Send message to clients when the health value of a player is updated
    public void clientUpdateHealth(Player pl) {
        ISFSObject data = new SFSObject();
        data.putInt("id", pl.getSfsUser().getId());

        Room currentRoom = RoomHelper.getCurrentRoom(this);
        List<User> userList = UserHelper.getRecipientsList(currentRoom);
        this.send("health", data, userList);
    }
}
