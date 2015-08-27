package com.beko.lobby.simulation;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.Random;

/**
 * Created by ankovalenko on 5/7/2015.
 */
public class Transform {

    private double x;
    private double y;
    private double z;

    private double rotx;
    private double roty;
    private double rotz;

    private long timeStamp = 0;  // Timestamp is stored to perform interpolation and extrapolation on client

    private static Random rnd = new Random();

    public Transform(double x, double y, double z, double rotx, double roty, double rotz) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.rotx = rotx;
        this.roty = roty;
        this.rotz = rotz;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getRotx() {
        return rotx;
    }

    public double getRoty() {
        return roty;
    }

    public double getRotz() {
        return rotz;
    }

    public static Random getRnd() {
        return rnd;
    }

    public static Transform fromSFSObject(ISFSObject data) {
        ISFSObject transformData = data.getSFSObject("transform");

        double x = transformData.getDouble("x");
        double y = transformData.getDouble("y");
        double z = transformData.getDouble("z");

        double rx = transformData.getDouble("rx");
        double ry = transformData.getDouble("ry");
        double rz = transformData.getDouble("rz");

        long timeStamp = transformData.getLong("t");

        Transform transform = new Transform(x, y, z, rx, ry, rz);
        transform.setTimeStamp(timeStamp);
        return transform;
    }

    // Copy another transform to this one
    public void load(Transform another) {
        this.x = another.x;
        this.y = another.y;
        this.z = another.z;

        this.rotx = another.rotx;
        this.roty = another.roty;
        this.rotz = another.rotz;

        this.setTimeStamp(another.getTimeStamp());
    }

    public void toSFSObject(ISFSObject data) {
        ISFSObject transformData = new SFSObject();
        transformData.putDouble("x", x);
        transformData.putDouble("y", y);
        transformData.putDouble("z", z);

        transformData.putDouble("rx", rotx);
        transformData.putDouble("ry", roty);
        transformData.putDouble("rz", rotz);

        transformData.putLong("t", this.timeStamp);

        data.putSFSObject("transform", transformData);
    }

    // Create random transform choosing from the predefined spawnPoints list
    public static Transform random() {
        Transform[] spawnPoints = getSpawnPoints();

        int i = rnd.nextInt(spawnPoints.length);
        return spawnPoints[i];
    }

    // Hardcoded spawnPoints - where players will spawn
    private static Transform[] getSpawnPoints() {
        Transform[] spawnPoints = new Transform[2];
        spawnPoints[0] = new Transform(270, 6.3, 300, 0, 0, 0);
        spawnPoints[1] = new Transform(174, 3, 330, 0, 100, 0);
        return spawnPoints;
    }
}
