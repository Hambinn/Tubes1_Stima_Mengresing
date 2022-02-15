package za.co.entelect.challenge;
import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.Terrain;

import java.util.*;

import static java.lang.Math.*;


import java.security.SecureRandom;

public class Bot {
    private List<Command> directionList = new ArrayList<>();

    private Random random;
    private GameState gameState;
    private Car opponent;
    
    private final static Command ACCELERATE = new AccelerateCommand();
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
        
        List<Object> blocksLurus = getBlocksInFront(myCar.position.lane, myCar.position.block, gameState, myCar.speed);
        Command option1 = Lurus.lurus(blocksLurus, myCar);
        
        Command option2;
        int jmlObstacleKiri = 999;
        int jmlObstacleKanan = 999;
        int jmlObstacleLurus = 999;

        int currentLane = myCar.position.lane;
        if (currentLane == 3){
            List<Object> blocksKanan = getBlocksInFront(myCar.position.lane - 1, myCar.position.block, gameState, myCar.speed);
            jmlObstacleKanan = cntObstacleInFront(blocksKanan);
            option2 = RLcheck.OnlyRight(blocksKanan);
        }
        else if (currentLane == 0){
            List<Object> blocksKiri = getBlocksInFront(myCar.position.lane + 1, myCar.position.block, gameState, myCar.speed);
            jmlObstacleKiri = cntObstacleInFront(blocksKiri);
            option2 = RLcheck.OnlyLeft(blocksKiri);
        }
        else{
            List<Object> blocksKanan = getBlocksInFront(myCar.position.lane - 1, myCar.position.block, gameState, myCar.speed);
            List<Object> blocksKiri = getBlocksInFront(myCar.position.lane + 1, myCar.position.block, gameState, myCar.speed);
            jmlObstacleKanan = cntObstacleInFront(blocksKanan);
            jmlObstacleKiri = cntObstacleInFront(blocksKiri);
            option2 = RLcheck.canRightAndLeft(blocksKiri, blocksKanan);
        }
        if (myCar.damage >= 4){
            return FIX;
        }
        
        if(option1 != DO_NOTHING){ 
            // Tidak ada Obstacle di lane lurus
            return option1;
        }
        else if (option2 != DO_NOTHING){ 
            // Ada obstacle di lane lurus tetapi kanan / kiri kosong
            return option2;
        }
        else{
            // Ada obstacle di lurus, kanan, dan kiri
            //Bandingin jumlah obstacle kanan, kiri, dan tengah
            int prioritasLane = min(jmlObstacleKanan,min(jmlObstacleKiri,jmlObstacleLurus));
            if (prioritasLane == jmlObstacleLurus){
                return ACCELERATE;
            }
            else{
                if (jmlObstacleKanan == jmlObstacleKiri){
                    if (currentLane == 1){
                        return TURN_RIGHT;   
                    }
                    else{
                        return TURN_LEFT;
                    }
                }
                else if (prioritasLane == jmlObstacleKanan){
                    return TURN_RIGHT;
                }
                else{
                    return TURN_LEFT;
                }
            }
        }
    }

    /**
     * Returns map of blocks and the objects in the for the current lanes, returns the amount of blocks that can be
     * traversed at max speed.
     **/
    private List<Object> getBlocksInFront(int lane, int block, GameState gameState, int speed) {
        List<Lane[]> map = gameState.lanes;
        List<Object> blocks = new ArrayList<>();
        int startBlock = map.get(0)[0].position.block;

        Lane[] laneList = map.get(lane);
        for (int i = max(block - startBlock, 0); i <= block - startBlock + speed; i++) {
            if (laneList[i] == null || laneList[i].terrain == Terrain.FINISH) {
                break;
            }

            blocks.add(laneList[i].terrain);

        }
        return blocks;
    }

    private int cntObstacleInFront(List<Object> currBlock){
        int count = 0;
        for (int i = 0; i<currBlock.size(); i++){
            if ((currBlock.get(i) == Terrain.MUD) || (currBlock.get(i) == Terrain.WALL) || (currBlock.get(i) == Terrain.OIL_SPILL)){
                count += 1;
            }
        }
        return count;
    }
}
