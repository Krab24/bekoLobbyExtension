package com.beko.lobby.simulation;

import com.beko.lobby.LobbyExtension;
import com.beko.lobby.utils.ValidatorHelper;
import com.smartfoxserver.v2.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ankovalenko on 5/7/2015.
 */
public class World {

    private LobbyExtension extension;

    // Players
    private List<Player> players = new ArrayList<Player>();

    public World(LobbyExtension extension) {
        this.extension = extension;
    }

    public List<Player> getPlayers() {
        return players;
    }

    // Gets the player corresponding to the specified SFS user
    private Player getPlayer(User u) {
        for (Player player : players) {
            if (player.getSfsUser().getId() == u.getId()) {
                return player;
            }
        }

        return null;
    }

    public Transform getTransform(User u) {
        Player player = getPlayer(u);
        return player.getTransform();
    }

    private boolean isValidNewTransform(Transform playerTransform ,
                                        Transform newTransform) {

        // Check if the given transform is valid in terms of collisions, speed hacks etc
        // In this example, the server will always accept a new transform from the client

        return ValidatorHelper.isNewTransformValid(playerTransform, newTransform);
    }

    // Trying to move player. If the new transform is not valid, returns null
    public Transform movePlayer(User u, Transform newTransform) {
        Player player = getPlayer(u);

//        if (player.isDead()) {
//            return player.getTransform();
//        }

        Transform playerTransform = player.getTransform();
        if (isValidNewTransform(playerTransform, newTransform)) {
            playerTransform.load(newTransform);

//            checkItem(player, newTransform);

            return newTransform;
        }

        return null;
    }


    // Add new player if he doesn't exist, or resurrect him if he already added
    public boolean addOrRespawnPlayer(User user) {
        Player player = getPlayer(user);

        if (player == null) {
            player = new Player(user);
            players.add(player);
            extension.clientInstantiatePlayer(player);
            return true;
        }
//        else {
//            player.resurrect();
//            extension.clientInstantiatePlayer(player);
//            return false;
//        }
        return false;
    }

    public void processShot(User fromUser) {
        Player player = getPlayer(fromUser);
        // Determine the intersection of the shot line with any of the other players to check if we hit or missed
        for (Player pl : players) {
            if (pl != player) {
                boolean res = checkHit(player, pl);
                if (res) {
                    playerHit(pl);
                    return;
                }

            }
        }

        // if we are here - we missed
    }

    // Checking if the player hits enemy using simple line intersection and
    // the known players position and rotation angles
    private boolean checkHit(Player player, Player enemy) {
        // First of all checking the line intersection with enemy in top projection

        double radius = enemy.getCollider().getRadius();
        double height = enemy.getCollider().getHeight();
        double myAngle = player.getTransform().getRoty();
        double vertAngle = player.getTransform().getRotx();

        // Calculating an angle relatively to X axis anti-clockwise
        double normalAngle = normAngle(360 + 90 - myAngle);

        //Calculating the angle of the line between player and enemy center point
        double difx = enemy.getX() - player.getX();
        double difz = enemy.getZ() - player.getZ();

        double ang = 0;
        if (difx == 0) {
            ang = 90;
        }
        else {
            ang = Math.toDegrees(Math.atan(Math.abs(difz / difx)));
        }

        // Modifying angle depending on the quarter
        if (difx <= 0) {
            if (difz <= 0) {
                ang += 180;
            }
            else {
                ang = 180 - ang;
            }
        }
        else {
            if (difz <= 0) {
                ang = 360 - ang;
            }
        }
        ang = normAngle(ang);

        // Calculating min angle to hit
        double angDif = Math.abs(ang - normalAngle);
        double d = Math.sqrt(difx * difx + difz * difz);
        double maxDif = Math.toDegrees(Math.atan(radius / d));

        if (angDif > maxDif) {
            return false;
        }

        // Now calculating the shot in the side projection

        // Correction value to fit the model visually (as the collider may not totally fit the model height on client)
        final double heightCorrection = 0.3;

        if (vertAngle > 90) {
            vertAngle = 360 - vertAngle;
        }
        else {
            vertAngle = -vertAngle;
        }

        double h = d * Math.tan(Math.toRadians(vertAngle));
        double dif = enemy.getTransform().getY() - player.getTransform().getY() - h + heightCorrection;
        if (dif < 0 || dif > height) {
            return false;
        }

        return true;
    }

    private double normAngle(double a) {
        if (a >= 360) {
            return a - 360;
        }
        return a;
    }

    // Applying the hit to the player.
    // Processing the health and death
    private void playerHit(Player pl) {
        extension.clientUpdateHealth(pl);
    }

    public void removePlayer(User user){
        Player player = getPlayer(user);
        if(player!=null){
            players.remove(player);
        }
    }
}
