package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.PowerUps;
import za.co.entelect.challenge.enums.Terrain;
import java.util.*;

public class RLcheck {
    private final static Command DO_NOTHING = new DoNothingCommand();

    private final static Command TURN_RIGHT = new ChangeLaneCommand(1);
    private final static Command TURN_LEFT = new ChangeLaneCommand(-1);
    public static Command cekKananAtauKiri(Car myCar, List<Object> blocksKiri, List<Object> blocksKanan) {
        // Cek dulu ada di paling kiri banget atau kanan banget atau di tengah
        int currentLane = myCar.position.lane;
        if (currentLane == 3){
            return OnlyRight(blocksKanan);
        }
        else if (currentLane == 0){
            return OnlyLeft(blocksKiri);
        }
        else{
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

