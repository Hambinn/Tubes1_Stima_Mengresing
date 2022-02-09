package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.PowerUps;
import za.co.entelect.challenge.enums.Terrain;
import java.util.*;



public class Lurus {
    public Command lurus(List<Object> blocks, Car mycar) {
        List<Object> currBlock = blocks;
        Car car = mycar;
        if(!currBlock.contains(Terrain.MUD) && !currBlock.contains(Terrain.WALL) && !currBlock.contains(Terrain.OIL_SPILL)) {
            if (car.speed != 9 && hasPowerUp(PowerUps.BOOST, car.powerups)){
                return new AccelerateCommand();
            }
            else{
                return new BoostCommand();
            }
        }else{
            if (hasPowerUp(PowerUps.LIZARD, car.powerups)){
                return new LizardCommand();
            }else{
                // cek kiri kanannya gatau ini gmn
            }
        }
        return new AccelerateCommand();
    }

    private Boolean hasPowerUp(PowerUps powerUpToCheck, PowerUps[] available) {
        for (PowerUps powerUp: available) {
            if (powerUp.equals(powerUpToCheck)) {
                return true;
            }
        }
        return false;
    }
}
