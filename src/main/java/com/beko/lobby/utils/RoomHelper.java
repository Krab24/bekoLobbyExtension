package com.beko.lobby.utils;

import com.beko.lobby.LobbyExtension;
import com.beko.lobby.simulation.World;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;

/**
 * Created by ankovalenko on 5/7/2015.
 */
public class RoomHelper {

    public static World getWorld(BaseClientRequestHandler handler) {
        LobbyExtension ext = (LobbyExtension) handler.getParentExtension();
        return ext.getWorld();
    }

    public static Room getCurrentRoom(BaseClientRequestHandler handler) {
        return handler.getParentExtension().getParentRoom();
    }

    public static Room getCurrentRoom(SFSExtension extension) {
        return extension.getParentRoom();
    }
}
