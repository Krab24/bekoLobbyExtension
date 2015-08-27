package com.beko.lobby.utils;

import com.beko.lobby.simulation.Transform;

/**
 * Created by ankovalenko on 6/26/2015.
 */
public class ValidatorHelper {

    public static boolean isNewTransformValid(Transform playerTransform, Transform newPlayerTransform){
        if(newPlayerTransform.getY() < -2.0 || newPlayerTransform.getY()>115.0  || newPlayerTransform.getX() > 500.0 || newPlayerTransform.getX() < 0.0 ||
                newPlayerTransform.getZ() <0.0 || newPlayerTransform.getZ()>485.0){
            return false;
        }

        if(Math.abs(playerTransform.getX() - newPlayerTransform.getX()) > 2.0 ||
                Math.abs(playerTransform.getY() - newPlayerTransform.getY()) > 10.0 ||
                Math.abs(playerTransform.getZ() - newPlayerTransform.getZ()) > 2.0){
            return false;
        }
        return true;
    }
}
