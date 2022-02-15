package za.co.entelect.challenge;
import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.Terrain;
import za.co.entelect.challenge.enums.PowerUps;

import java.util.*;

import static java.lang.Math.*;
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
        Command option1 = lurus(blocksLurus, myCar);
        
        Command option2;
        int jmlObstacleKiri = 999;
        int jmlObstacleKanan = 999;
        int jmlObstacleLurus = 999;

        int currentLane = myCar.position.lane;
        if (currentLane == 3){
            List<Object> blocksKanan = getBlocksInFront(myCar.position.lane + 1, myCar.position.block, gameState, myCar.speed);
            jmlObstacleKanan = cntObstacleInFront(blocksKanan);
            option2 = OnlyRight(blocksKanan);
        }
        else if (currentLane == 0){
            List<Object> blocksKiri = getBlocksInFront(myCar.position.lane - 1, myCar.position.block, gameState, myCar.speed);
            jmlObstacleKiri = cntObstacleInFront(blocksKiri);
            option2 = OnlyLeft(blocksKiri);
        }
        else{
            List<Object> blocksKanan = getBlocksInFront(myCar.position.lane + 1, myCar.position.block, gameState, myCar.speed);
            List<Object> blocksKiri = getBlocksInFront(myCar.position.lane - 1, myCar.position.block, gameState, myCar.speed);
            jmlObstacleKanan = cntObstacleInFront(blocksKanan);
            jmlObstacleKiri = cntObstacleInFront(blocksKiri);
            option2 = canRightAndLeft(blocksKiri, blocksKanan);
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

        Lane[] laneList = map.get(lane - 1);
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

    public static Command canRightAndLeft(List<Object> blocksKiri, List<Object> blocksKanan) {
        // Masukkin blockLane masing-masing arah
        List<Object> leftBlock = blocksKiri;
        List<Object> rightBlock = blocksKanan;

        // Memastikan bisa belok atau engga
        boolean isTurnLeft = !leftBlock.contains(Terrain.MUD) && !leftBlock.contains(Terrain.WALL) && !leftBlock.contains(Terrain.OIL_SPILL);
        boolean isTurnRight = !rightBlock.contains(Terrain.MUD) && !rightBlock.contains(Terrain.WALL) && !rightBlock.contains(Terrain.OIL_SPILL);
        
        if (isTurnLeft && isTurnRight){ 
            // Kiri dan kanan bisa. Niatnya pakai fungsi random tapi belum nyari caranya
            return TURN_LEFT;
        }
        else{
            if (isTurnLeft == true && isTurnRight == false){
                // Gabagus ke kanan
                return TURN_LEFT;
            }
            else if (isTurnLeft == false && isTurnRight == true){
                // Gabagus ke kiri
                return TURN_RIGHT;
            }
            else{ 
                // dua-duanya false
                // Return do nothing biar tar di luar aja dibandingin kualitas keputusannya sm yang lurus
                return DO_NOTHING;
            }
        }
    }

    public static Command OnlyRight(List<Object> blocksKanan){
        // Logic nya mirip sama yang di atas
        List<Object> rightBlock = blocksKanan;
        boolean isTurnRight = !rightBlock.contains(Terrain.MUD) && !rightBlock.contains(Terrain.WALL) && !rightBlock.contains(Terrain.OIL_SPILL);
        if (isTurnRight){
            return TURN_RIGHT;
        }
        else{
            return DO_NOTHING;
        }
    }

    public static Command OnlyLeft(List<Object> blocksKiri){
        // Logic nya mirip sama yang di atas
        List<Object> leftBlock = blocksKiri;
        boolean isTurnLeft = !leftBlock.contains(Terrain.MUD) && !leftBlock.contains(Terrain.WALL) && !leftBlock.contains(Terrain.OIL_SPILL);
        if (isTurnLeft){
            return TURN_LEFT;
        }
        else{
            return DO_NOTHING;
        }
    }

}