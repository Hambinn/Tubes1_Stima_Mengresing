package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.PowerUps;
import za.co.entelect.challenge.enums.Terrain;
import java.util.*;



public class Lurus {
    private final static Command ACCELERATE = new AccelerateCommand();
    private final static Command DECELERATE = new DecelerateCommand();
    private final static Command DO_NOTHING = new DoNothingCommand();
    private final static Command LIZARD = new LizardCommand();
    private final static Command OIL = new OilCommand();
    private final static Command BOOST = new BoostCommand();
    private final static Command EMP = new EmpCommand();
    private final static Command FIX = new FixCommand();

    private final static Command TURN_RIGHT = new ChangeLaneCommand(1);
    private final static Command TURN_LEFT = new ChangeLaneCommand(-1);
    public static Command lurus(List<Object> blocks, Car mycar) {
        List<Object> currBlock = blocks;
        int maxSpeed = 9;
        if(mycar.damage == 0) {
            maxSpeed = 15;
        }else if(mycar.damage == 1){
            maxSpeed = 9;
        }else if(mycar.damage == 2){
            maxSpeed = 7;
        }else if(mycar.damage == 3){
            maxSpeed = 6;
        }else if(mycar.damage == 4){
            maxSpeed = 3;
        }else if(mycar.damage == 5){
            maxSpeed = 0;
        }

        if(!currBlock.contains(Terrain.MUD) && !currBlock.contains(Terrain.WALL) && !currBlock.contains(Terrain.OIL_SPILL)) {
            if (mycar.speed < maxSpeed && !hasPowerUp(PowerUps.BOOST, mycar.powerups)){
                return ACCELERATE;
            }
            else{
                if (hasPowerUp(PowerUps.BOOST, mycar.powerups)){
                    return BOOST;
                }
                else{
                    return ACCELERATE;
                }
            }
        }else{
            if (hasPowerUp(PowerUps.LIZARD, mycar.powerups)){
                // Entah kenapa, lizard dipake padahal gaada apa apa
                return LIZARD;
            }else{
                return DO_NOTHING;
            }
        }
    }

    private static Boolean hasPowerUp(PowerUps powerUpToCheck, PowerUps[] available) {
        for (PowerUps powerUp: available) {
            if (powerUp.equals(powerUpToCheck)) {
                return true;
            }
        }
        return false;
    }
}

// penggunaan lizard tidak tepat