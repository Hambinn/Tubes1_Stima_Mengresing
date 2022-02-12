package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.PowerUps;
import za.co.entelect.challenge.enums.Terrain;
import java.util.*;

public class Kanan {
    private final static Command ACCELERATE = new AccelerateCommand();
    private final static Command Decelerate = new DecelerateCommand();
    private final static Command LIZARD = new LizardCommand();
    private final static Command OIL = new OilCommand();
    private final static Command BOOST = new BoostCommand();
    private final static Command EMP = new EmpCommand();
    private final static Command FIX = new FixCommand();

    private final static Command TURN_RIGHT = new ChangeLaneCommand(1);
    public static Command kanan(List<Object> blocks, Car mycar) {
        List<Object> currBlock = blocks;
        Car car = mycar;
        if(!currBlock.contains(Terrain.MUD) && !currBlock.contains(Terrain.WALL) && !currBlock.contains(Terrain.OIL_SPILL)) {
            // Belum beres
            return TURN_RIGHT;
        }
        else {
            return TURN_RIGHT;
        }
    }
    // Kayanya gaperlu power up sih ya, soalnya udh fokus di Lurus si powerup
}
