package za.co.entelect.challenge;
import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.Terrain;

import java.util.*;

import static java.lang.Math.max;

import java.security.SecureRandom;

public class Bot {
    private List<Command> directionList = new ArrayList<>();

    private Random random;
    private GameState gameState;
    private Car opponent;
    
    private final static Command ACCELEREATE = new AccelerateCommand();
    private final static Command DO_NOTHING = new DoNothingCommand();
    private final static Command DECELERATE = new DecelerateCommand();
    private final static Command LIZARD = new LizardCommand();
    private final static Command OIL = new OilCommand();
    private final static Command BOOST = new BoostCommand();
    private final static Command EMP = new EmpCommand();
    private final static Command FIX = new FixCommand();

    private final static Command TURN_RIGHT = new ChangeLaneCommand(1);
    private final static Command TURN_LEFT = new ChangeLaneCommand(-1);

    public Command run(GameState gameState) {
        Car myCar = gameState.player;
        opponent = gameState.opponent;

        // ALGORITMA PEMILIHAN ARAH BERDASARKAN KEPUTUSAN DI SETIAP ARAH
        // Ini ide versi saya, jadi saya komen dulu karna belum pasti
        // Saya juga sedikit update beberapa di file lurus
        // Kalo ada yg ngacauin, bilang ya hehehe
        // -- Raka
        
        List<Object> blocksLurus = getBlocksInFront(myCar.position.lane, myCar.position.block, gameState);
        Command option1 = Lurus.lurus(blocksLurus, myCar);
        Command option2;

        int currentLane = myCar.position.lane;
        if (currentLane == 3){
            List<Object> blocksKanan = getBlocksInFront(myCar.position.lane - 1, myCar.position.block, gameState);
            option2 = RLcheck.OnlyRight(blocksKanan);
        }
        else if (currentLane == 0){
            List<Object> blocksKiri = getBlocksInFront(myCar.position.lane + 1, myCar.position.block, gameState);
            option2 = RLcheck.OnlyLeft(blocksKiri);
        }
        else{
            List<Object> blocksKanan = getBlocksInFront(myCar.position.lane - 1, myCar.position.block, gameState);
            List<Object> blocksKiri = getBlocksInFront(myCar.position.lane + 1, myCar.position.block, gameState);
            option2 = RLcheck.canRightAndLeft(blocksKiri, blocksKanan);
        }
        if (myCar.damage >= 4){
            return FIX;
        }
        if (option1 == DO_NOTHING && option2 == DO_NOTHING){
            return ACCELEREATE;
        }
        else{
            if (option2 == DO_NOTHING && option1 != DO_NOTHING){
                return option1;
            }
            else if (option2 != DO_NOTHING && option1 == DO_NOTHING){
                return option2;
            }
            else{
                return option1;
            }
        }
    }

    /**
     * Returns map of blocks and the objects in the for the current lanes, returns the amount of blocks that can be
     * traversed at max speed.
     **/
    private List<Object> getBlocksInFront(int lane, int block, GameState gameState) {
        List<Lane[]> map = gameState.lanes;
        List<Object> blocks = new ArrayList<>();
        int startBlock = map.get(0)[0].position.block;

        Lane[] laneList = map.get(lane);
        for (int i = max(block - startBlock, 0); i <= block - startBlock + 9; i++) {
            if (laneList[i] == null || laneList[i].terrain == Terrain.FINISH) {
                break;
            }

            blocks.add(laneList[i].terrain);

        }
        return blocks;
    }

}
