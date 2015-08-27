package com.beko.lobby.handlers;

import com.beko.lobby.utils.RoomHelper;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

//This request is sent when player shots
public class ShotHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User u, ISFSObject data) {
        RoomHelper.getWorld(this).processShot(u);
    }

}