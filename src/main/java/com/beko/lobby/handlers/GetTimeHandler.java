package com.beko.lobby.handlers;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import java.util.Date;

/**
 * Created by ankovalenko on 5/8/2015.
 */
public class GetTimeHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User u, ISFSObject data) {
        ISFSObject res = new SFSObject();
        Date date = new Date();
        res.putLong("t", date.getTime());
        this.send("time", res, u);
    }

}
