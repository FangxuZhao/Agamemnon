package comp1140.ass2;

import comp1140.ass2.Agamemnon;

import java.util.ArrayList;
import java.util.Random;

public class AI {
    public static String[] random(String[] state,String flippedTiles) {
        String[] actions = new String[2];
        String tiles = state[0];
        String edges = state[1];
        ArrayList<String> tilesNodes_list = new ArrayList<>();
        ArrayList<String> edges_list = new ArrayList<>();

        String[] list_first = {"0","1","2","3"};
        String[] list_second = {"0","1","2","3","4","5","6","7","8","9"};
        ArrayList<String> available = new ArrayList<>();

        // get all the nodes
        int count_nodes = 0;
        for (int first = 0; first <= list_first.length - 1; first++) {
            for (int second = 0; second <= list_second.length - 1; second ++) {
                available.add(list_first[first] + list_second[second]);
                count_nodes = count_nodes + 1;
                if (count_nodes > 31) {break;} // nodes: 00 ~ 31
            }
        }

        // get all the edges
        for (int x = 0; x<= edges.length()-1; x+=5) {
            edges_list.add(edges.substring(x,x+5));
        }

        // get the tiles nodes
        for (int x = 0; x<= tiles.length()-1; x+=4) {
            tilesNodes_list.add(tiles.substring(x+2,x+4));
        }
        available.removeAll(tilesNodes_list); // get available nodes

        // random pick one node to put the flipped tile
        if (flippedTiles.length() == 2) {
            String[] action = new String[1];
            if (flippedTiles.charAt(1) != 'j') {
                action[0] = Agamemnon.non_warpPick(available,flippedTiles,state);
                return action;
            } else if (flippedTiles.charAt(1) == 'j') {
                action[0] = Agamemnon.non_warpPick(available,flippedTiles,state);
                return action;
            }
        } else if (flippedTiles.length() == 4) {
            if (flippedTiles.charAt(1) == 'j') {
                actions[0] = Agamemnon.warp_Pick(available,edges_list,flippedTiles.substring(0,2));
                if (flippedTiles.charAt(3) == 'j') {
                    actions[1] = Agamemnon.warp_Pick(available,edges_list,flippedTiles.substring(2,4));
                } else {
                    actions[1] = Agamemnon.non_warpPick(available,flippedTiles.substring(2,4),state);
                }
            } else {
                if (flippedTiles.charAt(3) == 'j') {
                    actions[0] = Agamemnon.non_warpPick(available,flippedTiles.substring(0,2),state);
                    actions[1] = Agamemnon.warp_Pick(available,edges_list,flippedTiles.substring(2,4));
                } else {
                    actions[0] = Agamemnon.non_warpPick(available,flippedTiles.substring(0,2),state);
                    actions[1] = Agamemnon.non_warpPick(available,flippedTiles.substring(2,4),state);
                }
            }
        }
        return actions;
    }
}
