package com.beko.lobby.simulation;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

/**
 * Created by ankovalenko on 5/7/2015.
 */
public class Player {
    private User sfsUser; // SFS user that corresponds to this player
    private Transform transform; // Transform of the player that is synchronized with clients
    private PlayerCollider collider; // Collider - determines the size of the player for shooting calculations

    public Player(User sfsUser) {
        this.sfsUser = sfsUser;
        this.transform = Transform.random();
        this.collider = new PlayerCollider(0, 1, 0, 0.5, 2);
    }

    public double getX() {
        return this.collider.getCenterx() + this.transform.getX();
    }

    public double getY() {
        return this.collider.getCentery() + this.transform.getY();
    }

    public double getZ() {
        return this.collider.getCenterz() + this.transform.getZ();
    }

    public User getSfsUser() {
        return sfsUser;
    }

    public Transform getTransform() {
        return transform;
    }

    public PlayerCollider getCollider() {
        return collider;
    }

    public void toSFSObject(ISFSObject data) {
        ISFSObject playerData = new SFSObject();

        playerData.putInt("id", sfsUser.getId());

        transform.toSFSObject(playerData);
        data.putSFSObject("player", playerData);
    }

}
