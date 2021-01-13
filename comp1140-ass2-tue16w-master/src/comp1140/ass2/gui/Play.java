package comp1140.ass2.gui;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class Play extends Viewer {
    private static String[] BTiles =
            {"Ba", "Bb", "Bc", "Bd", "Be"
            ,"Bf", "Bf", "Bf", "Bg", "Bg", "Bh"
            ,"Bi", "Bi", "Bj", "Bj"
            };
    static ArrayList<String> listOfBTiles = new ArrayList<>(Arrays.asList(BTiles));

    private static String[] OTiles =
            {
                     "Oa", "OO", "Oc", "Od", "Oe"
                    ,"Of", "Of", "Of", "Og", "Og", "Oh"
                    ,"Oi", "Oi", "Oj", "Oj"
            };
    static ArrayList<String> listOfOTiles = new ArrayList<>(Arrays.asList(OTiles));

    static String getOneTile(ArrayList<String> listOfTiles) {
        Collections.shuffle(listOfTiles);
        String get = listOfTiles.get(0);
        listOfTiles.remove(0);
        return get;
    }
}
