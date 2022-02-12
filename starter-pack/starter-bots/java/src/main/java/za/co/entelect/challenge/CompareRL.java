package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.PowerUps;
import za.co.entelect.challenge.enums.Terrain;
import java.util.*;

public class CompareRL{
    private final static Command DO_NOTHING = new DoNothingCommand();
    private final static Command TURN_RIGHT = new ChangeLaneCommand(1);
    private final static Command TURN_LEFT = new ChangeLaneCommand(-1);

    public static Command Compare(Car myCar, List<Object> currBlock, List<Object> blockKanan, List<Object> blockKiri){
        int curr_lane = myCar.position.lane;
        boolean isSafeRight = !(blockKanan.contains(Terrain.MUD) || blockKanan.contains(Terrain.WALL) || blockKanan.contains(Terrain.OIL_SPILL));
        boolean isSafeLeft = !(blockKiri.contains(Terrain.MUD) || blockKiri.contains(Terrain.WALL) || blockKiri.contains(Terrain.OIL_SPILL));
        if (curr_lane == 0){
            //Berada di paling kiri, cek kemungkinan kanan
            if (!isSafeRight){
                //Jika kanan tidak bisa juga, bandingkan yang menghasilkan kemungkinan terbaik
                //Jika bisa Deaccelerate, ambil deaccelerate, jika tidak, pasang trap jika ada, jika tidak Do_Nothing
                //Masalahnya bingung gimana cari kemungkinan aman deaccelerate
            }
            else{
                return TURN_RIGHT;
            }
        }
        else if (curr_lane == 3){
            //Berada di paling kanan, cek kemungkinan kiri
            if (!isSafeLeft){
                //Jika kiri tidak bisa juga, bandingkan yang menghasilkan kemungkinan terbaik
                //Jika bisa Deaccelerate, ambil deaccelerate, jika tidak, pasang trap jika ada, jika tidak Do_Nothing
                //Masalahnya bingung gimana cari kemungkinan aman deaccelerate
            }
            else{
                return TURN_LEFT;
            }
        }
        else{
            //Berada di tengah lane
            if ((!isSafeLeft) && (!isSafeRight)) {
                //Kanan dan kiri lane ada halangan, maka cari yang terbaik diantara 3 lane
                //Jika bisa Deaccelerate, ambil deaccelerate, jika tidak, pasang trap jika ada, jika tidak Do_Nothing
                //Masalahnya bingung gimana cari kemungkinan aman deaccelerate
            }
            else if (isSafeRight) {
                //Kanan tidak memiliki halangan
                return TURN_RIGHT;
            }
            else if (isSafeLeft) {
                //Kiri tidak memiliki halangan
                return TURN_LEFT;
            }
            else{
                //Kanan dan kiri tidak memiliki halangan
                if (curr_lane == 1){
                    //Jika di lane 1, maka pilih belok kanan (dengan kemungkinan agar pilihan gerakan lebih leluasa dibanding ke kiri)
                    return TURN_RIGHT;
                }
                else{
                    //Jika di lane 2, maka pilih belok kiri agar lebih leluasa
                    return TURN_LEFT;
                }
            }
        }

    }
}