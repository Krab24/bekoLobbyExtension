package com.beko.lobby.handlers;

import com.beko.lobby.LobbyExtension;
import com.beko.lobby.simulation.Player;
import com.beko.lobby.simulation.World;
import com.beko.lobby.utils.RoomHelper;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

/**
 * Created by ankovalenko on 5/7/2015.
 */
public class SpawnMeHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject isfsObject) {
        World world = RoomHelper.getWorld(this);
        boolean newPlayer = world.addOrRespawnPlayer(user);
//        ISFSObject data = new SFSObject();
//        ISFSObject playerData = new SFSObject();
//        playerData.putInt("id", user.getId());
//        data.putSFSObject("player", playerData);
//        this.send("spawnPlayer", data, user);
        if (newPlayer) {
            // Send this player data about all the other players
            sendOtherPlayersInfo(user);
        }
    }

    // Send the data for all the other players to the newly joined client
    private void sendOtherPlayersInfo(User targetUser) {
        World world = RoomHelper.getWorld(this);
        for (Player player : world.getPlayers()) {
//            if (player.isDead()) {
//                continue;
//            }

            if (player.getSfsUser().getId() != targetUser.getId()) {
                LobbyExtension ext = (LobbyExtension) this.getParentExtension();
                ext.clientInstantiatePlayer(player, targetUser);
            }
        }
    }
}
