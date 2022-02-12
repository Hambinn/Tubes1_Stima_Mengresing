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
        Car car = mycar;
        if(!currBlock.contains(Terrain.MUD) && !currBlock.contains(Terrain.WALL) && !currBlock.contains(Terrain.OIL_SPILL)) {
            if (car.speed != 9 && !hasPowerUp(PowerUps.BOOST, car.powerups)){
                return ACCELERATE;
            }
            else{
                return BOOST;
            }
        }else{
            if (hasPowerUp(PowerUps.LIZARD, car.powerups)){
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
