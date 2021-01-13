package comp1140.ass2;

import java.util.*;

/**
 * At any time, the current state of the game is encoded as two strings:
 * 1. state[0] is a string representing the states of the playing tiles.
 * It consists of a number of 4-character tile placement strings,
 * each encoding a single tile placement as follows:
 * - 1st character is the color of the playing tile: {'O'=orange or 'B'=black },
 * - 2nd character is the type of the playing tile:
 * * 'a' = Leader, rank = A and strength = 1
 * * 'b' = Leader, rank = B and strength = 3
 * * 'c' = Leader, rank = C and strength = 4
 * * 'd' = Leader, rank = D and strength = 3
 * * 'e' = Leader, rank = E and strength = 2
 * * 'f' = Warrior and strength = 1
 * * 'g' = Warrior and strength = 2
 * * 'h' = Warrior and strength = 3
 * * 'i' = Weft weaver
 * * 'j' = Warp weaver
 * - 3rd character is the first digit of the destination node's id
 * - 4th character is the second digit of the destination node's id
 * <p>
 * Examples:
 * - a strength-one Warrior of the first player on node 6 is encoded as "Of06";
 * - a Weft tile of the second player on node 12 is encoded as "Bi12";
 * - the highest-ranked Leader of the first player on node 23 is encoded as "Oa23".
 * <p>
 * The number of these tile placement strings in state[0] is equal to the number of playing tiles already
 * played, e.g. if `n` tiles have been played so far, the total state[0] length will be `4*n`.
 * <p>
 * 2. state[1] is the current state of edges on the board.
 * <p>
 * The game board consists of a set of nodes indexed from 0 to n-1, and a set of edges indexed from 0 to e-1.
 * For the standard Agamemnon board, n=32 and e=49;
 * for the Loom board, n=33 and e=66.
 * <p>
 * Each edge has two nodes as its endpoints and is encoded as a 5-character string:
 * - 1st character is the edge type: {`L`=Leadership, `S`=Strength, `F`=Force},
 * * NOTE: the Loom variant adds {'E'=Empty} to the edge types.
 * * When playing a Warp tile, 'E' type edges maybe swapped with a neighbor edge of type 'L', 'S' or 'F'.
 * * Edges of type 'E' are not counted in the final game points.
 * - 2nd character is the first digit of endpoint_1's id
 * - 3rd character is the second digit of endpoint_1's id
 * - 4th character is the first digit of endpoint_2's id
 * - 5th character is the second digit of endpoint_2's id
 * <p>
 * Examples:
 * - a Strength edge connecting node 0 to node 1 is encoded as "S0001"
 * - a Leadership edge connecting node 8 to node 13 is encoded as "L0813".
 * - an empty edge (not connected) between nodes 25 and 29 is encoded as "E2529".
 * <p>
 * state[1] is a concatenation of the 5-character encodings of all of edges on the board.
 * During a game, some of these edges may change, e.g. by playing a Warp tile.
 */
public class Agamemnon {
    /**
     * Check whether the input state is well formed or not.
     * <p>
     * To be well-formed:
     * 1- input `state` must consist of two strings,
     * 2- correct string length: for state[0] a multiple of 4 and for state[1] a multiple of 5,
     * 3- each character in the two strings must be in its acceptable range, as explained in the
     * class documentation at the top of this class,
     * e.g. in state[0], 1st, 5th, ... characters must be either 'B' or 'O',
     * and 2nd, 6th, ... characters must be from 'a' to 'j', etc.
     *
     * @param state an array of strings representing the current game state
     * @return true if the input state is well-formed, otherwise false
     * @author Fangxu Zhao(u6822201)
     *
     * Task 2
     * Drafted by FX; Modified and tested by Leo.
     */

    public static boolean isStateWellFormed(String[] state) {
        boolean b;

        /* Rule 1 -- make sure the state has 2 strings, and each string must not be Null. */
        if (state.length != 2) return false;
        if (state[0] == null || state[1] == null) return false;

        String tiles = state[0];
        String edges = state[1];
        /* Rule 2 -- the length of tiles and edges must be correct. */
        b = (tiles.length() % 4 == 0) && (edges.length() % 5 == 0);

        /* Rule 3 -- Regulate the range of each char in each string. */
        // tiles
        for (int x = 0; x <= tiles.length() - 1; x += 4) {
            b = b && ((tiles.charAt(x) == 'B') || (tiles.charAt(x) == 'O'));
        }
        for (int x = 1; x <= tiles.length() - 1; x += 4) {
            int a = tiles.charAt(x);
            b = b && (a >= 97 && a <= 106);  // a - j
        }
        for (int i = 2; i < 4; i ++){
            for (int x = i; x <= tiles.length() - 1; x += 4) {
                int a = tiles.charAt(x);
                b = b && (a >= 48 && a <= 57);  // 0 - 9
            }
        }

        // edges
        for (int x = 0; x <= edges.length() - 1; x += 5) {
            b = b && ( (edges.charAt(x) == 'L')
                    || (edges.charAt(x) == 'S')
                    || (edges.charAt(x) == 'F')
                    || (edges.charAt(x) == 'E'));
        }
        for (int i = 1; i < 5; i ++){
            for (int x = i; x <= edges.length() - 1; x += 5) {
                int a = edges.charAt(x);
                b = b && (a >= 48 && a <= 57);
            }
        }
        return b;
    }

    /**
     * Check whether the input state is valid or not.
     * <p>
     * To be valid:
     * 1- there must be at most one playing tile on each board node,
     * 2- destination nodes' ids must be in range [0 to n-1] inclusive, where `n` is the number of
     * nodes
     * 3- for each type of playing tiles, only the available number of it may be played
     * (e.g. at most one orange rank-A Leader can be on the board)
     * 4- the correct number of pieces must have been played by each player at each turn
     * (one on the first turn, then two on each subsequent turn until the final turn)
     * 5- each pair of nodes must be connected by at most one edge
     *
     * @param state an array of two strings, representing the current game state
     * @return true if the input state is valid and false otherwise.
     * @author Fangxu Zhao (u6822201)
     * Task 3
     * Drafted by FX; Modified and tested by Leo.
     */
    public static boolean isStateValid(String[] state) {
        boolean b = true;
        final int NODE_NUMBER = 32;
        final int NODE_NUMBER_LOOM = 33;

        String tiles = state[0];
        String edges = state[1];
        ArrayList<String> list = new ArrayList<>();


        /* Rule 1 -- at most one tile on each node. */
        // get the list of tiles
        for (int x = 2; x <= tiles.length() - 1; x += 4) {
            list.add(tiles.substring(x, x + 2));
        }

        Set<String> tilesSet = new LinkedHashSet<>(list);
        List<String> modifier = new ArrayList<>(tilesSet);
        if (modifier.size() < list.size()) b = false;

        /* Rule 2 -- destination nodes' ids must be in range [0, n-1] (inclusive). */
        for (int x = 0; x <= list.size() - 1; x ++) {
            b = b && Integer.parseInt(list.get(x)) <= NODE_NUMBER;
        }

        list.clear();

        /* Rule 3 -- only available number of each type of playing tile may be played. */
        // get a new list of tiles
        for (int x = 0; x < tiles.length() - 1; x += 4) {
            list.add(tiles.substring(x, x + 2));
        }
        // correct number of tiles
        b = isTilesCorrectAmount(list, b);

        /* Rule 4 -- correct number of pieces being played by each player at each turn. */
        if (list.get(0).charAt(0) != 'O') return false;
        for (int x = 1; x < list.size() - 3; x += 4) {
            b = b && list.get(x).charAt(0) == 'B'
                  && list.get(x+1).charAt(0) == 'B'
                  && list.get(x+2).charAt(0) == 'O'
                  && list.get(x+3).charAt(0) == 'O';
        }

        list.clear();

        /* Rule 5 -- each pair of nodes connected by at most one edge. */
        // get the edges' placement
        for (int x = 1; x <= edges.length() - 4; x += 5) {
            list.add(edges.substring(x, x + 4));
        }

        Set<String> edgesSet = new LinkedHashSet<>(list);
        List<String> modifier2 = new ArrayList<>(edgesSet);
        if (modifier2.size() < list.size()) b = false;

        // multiple graph
        for (int x = 0; x <= list.size() - 1; x++) {
            String a = list.get(x);
            String b1 = a.substring(2,4) + a.substring(0,2);
            for (int y = 0; y <= list.size() - 1; y++) {
                if (list.get(y).equals(b1)) {
                    return false;
                }
            }
        }
        return b;
    }

    private static boolean isTilesCorrectAmount(ArrayList list, boolean bool) {
        int Oa = Collections.frequency(list, "Oa");
        int Ob = Collections.frequency(list, "Ob");
        int Oc = Collections.frequency(list, "Oc");
        int Od = Collections.frequency(list, "Od");
        int Oe = Collections.frequency(list, "Oe");
        int Of = Collections.frequency(list, "Of");
        int Og = Collections.frequency(list, "Og");
        int Oh = Collections.frequency(list, "Oh");
        int Oi = Collections.frequency(list, "Oi");
        int Oj = Collections.frequency(list, "Oj");
        if (Oa>1 || Ob>1 || Oc>1 || Od>1 || Oe>1 || Oh>1) bool = false;
        if (Og>2 || Oi>2 || Oj>2) bool = false;
        if (Of>3) bool = false;

        int Ba = Collections.frequency(list, "Ba");
        int Bb = Collections.frequency(list, "Bb");
        int Bc = Collections.frequency(list, "Bc");
        int Bd = Collections.frequency(list, "Bd");
        int Be = Collections.frequency(list, "Be");
        int Bf = Collections.frequency(list, "Bf");
        int Bg = Collections.frequency(list, "Bg");
        int Bh = Collections.frequency(list, "Bh");
        int Bi = Collections.frequency(list, "Bi");
        int Bj = Collections.frequency(list, "Bj");
        if (Ba>1 || Bb>1 || Bc>1 || Bd>1 || Be>1 || Bh>1) bool = false;
        if (Bg>2 || Bi>2 || Bj>2) bool = false;
        if (Bf>3) bool = false;

        return bool;
    }

    /**
     * Randomly select one or two tiles for the current player.
     * On the first turn (before any pieces have been placed),
     * this method will return a two-character String representing a randomly selected tile.
     * A tile is encoded as 2 characters:
     * - 1st character is the color of the flipped tile, which is 'O' or 'B',
     * - 2nd character is the type of the flipped tile, which is between 'a' and 'j'.
     * On subsequent turns (except the final turn), this method will return a four-character string,
     * representing two tiles for the current player that have not already been placed.
     *
     * @param tilePlacements a String representing the previously placed tiles,
     *                       equivalent to the first string of the game state
     * @return a String of either two or four characters, representing randomly selected tile(s)
     * that are available to be placed for this turn
     * @author Fangxu Zhao (u6822201)
     * Task 5
     * Drafted by FX; modified and tested by Leo.
     */
    public static String selectTiles(String tilePlacements) {
        Character[] Tiles = {'a','b','c','d','e','f','f','f','g','g','h','i','i','j','j'};
        ArrayList<Character> a_list = new ArrayList<>(Arrays.asList(Tiles));
        int length = tilePlacements.length();

        String output = "";
        Random in = new Random();

        // the first turn
        if (length == 0) {
            Set<Character> set1 = new LinkedHashSet<>(a_list);
            List<Character> modifier = new ArrayList<>(set1);

            int random = in.nextInt(modifier.size());
            output = output + 'O' + modifier.get(random);
        } else {
            // not first turn
            if (tilePlacements.charAt(length-4) == 'O') {
                // remove the used tile
                int count = 5;
                while (count <= length - 5) {
                    findAndRemove(tilePlacements.charAt(count),a_list);
                    count = count + 4;
                    findAndRemove(tilePlacements.charAt(count),a_list);
                    count = count + 12;
                }

                // check if tiles run out
                if (a_list.size() == 0) {return "";}
                if (a_list.size() == 1) {return "B" + a_list.get(0).toString();}

                int random = in.nextInt(a_list.size());
                output = output + 'B' + a_list.get(random);
                a_list.remove(random);
                int new_random = in.nextInt(a_list.size());
                output = output + 'B' + a_list.get(new_random);
            } else {
                // remove all used tiles
                int count = 1;
                while (count <= length - 5) {
                    findAndRemove(tilePlacements.charAt(count),a_list);
                    count = count + 4;
                    findAndRemove(tilePlacements.charAt(count),a_list);
                    count = count + 12;
                }
                // check if tiles are ran out
                if (a_list.size() == 0) {return "";}
                if (a_list.size() == 1) {return "O" + a_list.get(0).toString();}

                int random = in.nextInt(a_list.size());
                output = output + 'O' + a_list.get(random);
                a_list.remove(random);
                int new_random = in.nextInt(a_list.size());
                output = output + 'O' + a_list.get(new_random);
            }
        }
        return output;
    }

    private static void findAndRemove(char a, ArrayList<Character> b) {
        for (int i = 0; i <= b.size() - 1; i++) {
            if (b.get(i) == a) {
                b.remove(i);
                break;
            }
        }
    }

    /**
     * Check whether a playing action is valid or not.
     * <p>
     * A playing action is a variable length string, consisting of one or two sub-actions.
     * A sub-action represents playing a single tile of any type:
     * 1- if the tile is not a Warp weaver(j), the sub-action is a 4-character string
     * representing the placement of a playing tile on a board cell.
     * It follows exactly the same encoding as state[0] (available above this file).
     * 2- if the tile is a Warp weaver, the sub-action is a 8-character string,
     * in which the first 4 characters represent the placement of the Warp tile,
     * and the next 4 characters represent the swapping of two edges,
     * and is encoded as:
     * - 1st character is the left digit of endpoint_1's id + '0'
     * - 2nd character is the right digit of endpoint_1's id + '0'
     * - 3rd character is the left digit of endpoint_2's id + '0'
     * - 4th character is the right digit of endpoint_2's id + '0'
     * , where endpoint_1 and endpoint_2 are the other endpoints of the two edges
     * to be swapped (one end will be the node in action[0],
     * where the Warp is being placed).
     * NOTE_1: endpoint_1 and endpoint_2 can be equal. In that case, no edges will be swapped.
     * <p>
     * NOTE_2: All playing actions include two sub-actions, except the first and the last actions of
     * player_1 (the player who starts the game), which consist of only one sub-action.
     * <p>
     * To be valid, an action must:
     * 0- target tiles which have not already been played
     * 1- include a valid number of sub-actions (see NOTE_2),
     * 2- each sub-action must:
     * * 2.1- must have the correct string length (8 for Warp and 4 otherwise),
     * * 2.2- target an empty node,
     * * 2.3- (for Warp tiles) swap two valid edges. To be valid,
     * endpoint_1 and endpoint_2 must be neighbours of the target node.
     *
     * @param state  an array of strings, representing the current game state
     * @param action a string, representing a playing action
     * @return true  is 'action` is a valid playing action, and false otherwise
     * @author Fangxu Zhao (u6822201)
     */
    public static boolean isActionValid(String[] state, String action) {
        /* Task 6 -- checked and modified by Leo
        Still need better adjust, the code is a bit redundant, I will redesign it after JavaFX part.
         */
        boolean bool = true;
        String tiles = state[0];
        ArrayList<String> list = new ArrayList<>();

        // first action
        if (tiles.length() == 0) {
            for (int x = 2;x<=action.length()-3;x+=2) {
                list.add(action.substring(x,x+2));
            }
            Set<String> set1 = new LinkedHashSet<>(list);
            List<String> modifier = new ArrayList<>(set1);
            if (action.charAt(1) == 'j') {bool = (action.charAt(0) == 'O' && action.length() == 8 && modifier.size() == list.size());}
            if (action.charAt(1) != 'j') {bool = (action.charAt(0) == 'O' && action.length() == 4 && modifier.size() == list.size());}
            list.clear();
            return bool;
        }

        // test 1: test length (depends how many warp) and the correct turn at first
        if (action.length() <8 || action.length() >16) {return false;}
        if (tiles.charAt(tiles.length() - 4) == action.charAt(0)) {return false;}

        if (action.charAt(1) == 'j') { // if first is warp
            if (action.length() < 12 || action.charAt(0) != action.charAt(8)) {return false;}
            if (action.charAt(9) == 'j') {
                if (action.length() != 16) {return false;}
                }
        }
        if (action.charAt(1) != 'j' && action.charAt(5) == 'j') {if (action.length() != 12 || action.charAt(0) != action.charAt(4)) {return false;}}
        if (action.charAt(1) != 'j' && action.charAt(5) != 'j') {if (action.length() != 8 || action.charAt(0) != action.charAt(4)) {return false;}}

        // test 2: target tiles unused
        for (int x = 0; x<=tiles.length()-1;x+=2) {
            list.add(tiles.substring(x,x+2));
        }
        list.add(action.substring(0,1));
        list.add(action.substring(4,6));
        bool = isTilesCorrectAmount(list, true);
        list.clear();

        // test 3: target empty node
        for (int x = 2; x <= tiles.length() - 2; x+=4) {
            list.add(tiles.substring(x,x+2));
        }
        list.add(action.substring(2,4));

        // test 4: check the neighbor node
        if (action.charAt(1) == 'j') {
            bool = checkWarp(action,1,bool);
            list.add(action.substring(10,12));
            if (action.charAt(9) == 'j') {
               bool = checkWarp(action,9,bool);
               if (action.substring(12,14).equals(action.substring(2,4)) || action.substring(14,16).equals(action.substring(2,4))) { return false;}
            }
        }

        list.add(action.substring(6,8));
        if (action.charAt(5) == 'j') {
            bool = checkWarp(action,5,bool);
        }
        Set<String> set1 = new LinkedHashSet<>(list);
        List<String> modifier = new ArrayList<>(set1);
        if (modifier.size() < list.size()) {return false;}

        return bool;
    }

    // function for checking the neighbour nodes
    public static boolean checkWarp(String action, int a, boolean bool) {
        if (a == 0) {
            return bool;
        }
        int targetNode = Integer.parseInt(action.substring(a+1, a+3));
        int abs1 = Math.abs(targetNode - Integer.parseInt(action.substring(a+3,a+5)));
        int abs2 = Math.abs(targetNode - Integer.parseInt(action.substring(a+5,a+7)));
        bool = bool && (abs1 >= 1 && abs1 <= 5)
                    && (abs2 >= 1 && abs2 <= 5);
        return bool;
    }

    /**
     * Given the current game state and a playing action, calculate the updated game state.
     *
     * @param state  an array of two strings, representing the current game state
     * @param action a string, representing a playing action
     * @return an array of two strings, representing the game state after applying `action`
     * @author Fangxu Zhao (u6822201)
     */
    public static String[] applyAction(String[] state, String action) {
        // Task 7
        int x = 0;
        String begin, end, begin2, end2;
        ArrayList<String> edges_list = new ArrayList<>();

        String[] output = new String[state.length];
        output[0] = "";
        output[1] = "";

        // get the edge
        while (x <= state[1].length() - 5) {
            edges_list.add(state[1].substring(x, x + 5));
            x = x + 5;
        }

        // if the first one is "swap"
        if (action.charAt(1) == 'j') {
            output[0] = state[0] + action.substring(0,4) + action.substring(8,12);

            begin = miniFirst(action,2,4,4,6);
            end = miniFirst(action,2,4,6,8);
            output[1] = swap(state[1],findPosition(edges_list,begin,end)[0],findPosition(edges_list,begin,end)[1]);

            // if both are "swap"
            if (action.charAt(9) == 'j') {
                begin2 = miniFirst(action,10,12,12,14);
                end2 = miniFirst(action,10,12,14,16);
                output[1] = swap(output[1],findPosition(edges_list,begin2,end2)[0],findPosition(edges_list,begin2,end2)[1]);
            }
        }

        // if the first is not a "swap" and the second is a "swap"
        if (action.charAt(1) !='j' && action.charAt(5) =='j') {
            output[0] = state[0] + action.substring(4,8) + action.substring(0,4);
            begin = miniFirst(action,6,8,8,10);
            end = miniFirst(action,6,8,10,12);
            output[1] = swap(state[1],findPosition(edges_list,begin,end)[0],findPosition(edges_list,begin,end)[1]);
        }
        // neither are "swap"
        if (action.charAt(1) != 'j' && action.charAt(5) !='j') {
            output[0] = state[0] + action;
            output[1] = state[1];
        }
        return output;
    }
    // find the position of begin and end in list
    public static int[] findPosition(ArrayList<String> list, String begin, String end) {
        int[] output = new int[2];

        for (int count = 0; count <= list.size() - 1; count++) {
            if (list.get(count).substring(1,5).equals(begin)) {
                output[0] = count;
            }
            if (list.get(count).substring(1,5).equals(end)) {
                output[1] = count;
            }
        }
        return output;
    }
    // swap two char by index
    private static String swap(String state1, int p1, int p2) {
        char[] stat = state1.toCharArray();
        char temp = stat[p1 * 5];
        StringBuilder output1 = new StringBuilder();

        stat[p1*5] = stat[p2*5];
        stat[p2*5] = temp;
        for (int x = 0; x<=stat.length-1; x++) {
            output1.append(stat[x]);
        }
        return output1.toString();
    }
    // compare the int value of two string
    private static String miniFirst(String action, int a, int b, int c, int d) {
        String begin;
        if (Integer.parseInt(action.substring(c,d)) < Integer.parseInt(action.substring(a,b))) {
            begin = action.substring(c,d) + action.substring(a,b);
        } else {
            begin = action.substring(a,b) + action.substring(c,d);
        }
        return begin;
    }

    /**
     * Given a game state, calculate the total number of edges won by each player.
     *
     * @param state an array of two strings, representing the current game state
     * @return an array of two integers, where:
     * * result[0] includes the points earned by the Orange player (player_1)
     * * result[1] includes the points earned by the Black player (player_2)
     * @author Fangxu Zhao (u6822201)
     */
    public static int[] getTotalScore(String[] state) {
        // FIXME - Task +8
        String tiles = state[0];
        String edges = state[1];
        int final_score_O = 0;
        int final_score_B = 0;
        int[] output = {0,0};

        ArrayList<String> Strength = new ArrayList<>();
        ArrayList<String> Leadership = new ArrayList<>();
        ArrayList<String> Force = new ArrayList<>();

        // classify the edges
        // because the score depends on the type of string.
        for (int x = 0; x <= edges.length()-1; x+=5) {
            String edge = edges.substring(x,x+5);
            switch (edge.charAt(0)) {
                case ('S'):
                    Strength.add(edge);
                    break;
                case ('L'):
                    Leadership.add(edge);
                    break;
                case ('F'):
                    Force.add(edge);
            }
        }

        // get the tiles
        ArrayList<String> list_tiles = new ArrayList<>();
        for (int x = 0; x < tiles.length()-1; x += 4) {
            String tile = tiles.substring(x, x + 4);
            list_tiles.add(tile);
        }
        if (list_tiles.size() == 0) return output;// if there is no tile

        // find the edges connected by tiles(actually there are tiles in the "paths")
        List<ArrayList<String>> paths_Strength = findPaths(list_tiles,Strength);
        List<ArrayList<String>> paths_Leadership = findPaths(list_tiles,Leadership);
        List<ArrayList<String>> paths_Force = findPaths(list_tiles,Force);

        // find the score in the path
        //score of Strength
        for(int x = 0; x<=paths_Strength.size()-1; x++){// get an ArrayList of the "path"(with its tile)
            int score_O = 0;
            int score_B = 0;
            for (int index = 0; index<=paths_Strength.get(x).size()-1; index++){// get the index
                if (paths_Strength.get(x).get(index).charAt(0) == 'O' || paths_Strength.get(x).get(index).charAt(0) == 'B') {
                    score_O = countingScores(paths_Strength.get(x).get(index),score_O,score_B)[0];
                    score_B = countingScores(paths_Strength.get(x).get(index),score_O,score_B)[1];
                    paths_Strength.get(x).remove(index);// after counting the strength we need to remove the tile for counting final scores
                    index = index - 1;
                }
            }
            if (score_O > score_B) final_score_O = final_score_O + paths_Strength.get(x).size();// after looping all the edges, compare the scores of edges
            if (score_B > score_O) final_score_B = final_score_B + paths_Strength.get(x).size();
        }

        // score of Leadership
        for (int x = 0; x<=paths_Leadership.size()-1;x++){
            ArrayList<Integer> rank_O = new ArrayList<>();
            ArrayList<Integer> rank_B = new ArrayList<>();
            for (int index = 0; index<=paths_Leadership.get(x).size()-1; index++) {
                if (paths_Leadership.get(x).get(index).charAt(0) == 'O' || paths_Leadership.get(x).get(index).charAt(0) == 'B') {
                if (paths_Leadership.get(x).get(index).charAt(0) == 'O' && countingRanks(paths_Leadership.get(x).get(index)) != null) {rank_O.add((int)countingRanks(paths_Leadership.get(x).get(index)));}
                if (paths_Leadership.get(x).get(index).charAt(0) == 'B' && countingRanks(paths_Leadership.get(x).get(index)) != null) {rank_B.add((int)countingRanks(paths_Leadership.get(x).get(index)));}
                paths_Leadership.get(x).remove(index);
                index = index - 1;
                }
            }
            if (rank_O.size() != 0 && rank_B.size() == 0) {final_score_O = final_score_O + paths_Leadership.get(x).size();}
            if (rank_O.size() == 0 && rank_B.size() != 0) {final_score_B = final_score_B + paths_Leadership.get(x).size();}
            if (rank_O.size() != 0 && rank_B.size() != 0) {
            if (Collections.min(rank_O) < Collections.min(rank_B)) final_score_O = final_score_O + paths_Leadership.get(x).size();
            if (Collections.min(rank_O) > Collections.min(rank_B)) final_score_B = final_score_B + paths_Leadership.get(x).size();
            }
        }

        //score of Force
        for (int x = 0;x<=paths_Force.size()-1;x++){
            ArrayList<String> tileNumber_O = new ArrayList<>();
            ArrayList<String> tileNumber_B = new ArrayList<>();
            for (int index = 0; index<=paths_Force.get(x).size()-1; index++) {
                if (paths_Force.get(x).get(index).charAt(0) == 'O' || paths_Force.get(x).get(index).charAt(0) == 'B') {
                if (paths_Force.get(x).get(index).charAt(0) == 'O') tileNumber_O.add(paths_Force.get(x).get(index));
                if (paths_Force.get(x).get(index).charAt(0) == 'B') tileNumber_B.add(paths_Force.get(x).get(index));
                paths_Force.get(x).remove(index);
                index = index - 1;
                }
            }
            if (tileNumber_O.size() > tileNumber_B.size()) final_score_O = final_score_O + paths_Force.get(x).size();
            if (tileNumber_O.size() < tileNumber_B.size()) final_score_B = final_score_B + paths_Force.get(x).size();
        }
        output[0] = final_score_O;
        output[1] = final_score_B;
        return output;
    }

    // inputs a tile and then find the paths(represented by connected edges) it forms with the edges.
    //* The core of this task *//
    public static List<ArrayList<String>> findPaths(ArrayList<String> list_tiles, ArrayList<String> list_edges) {
        // create a list to store all the "paths"
        List<ArrayList<String>> general = new ArrayList<>();

        // get the edges **every** tile connected to(except the tile "i")
        for (int x = 0; x<=list_tiles.size()-1;x++) {
            if (list_tiles.get(x).charAt(1) == 'i') {// if it is tile "i", just put it with every edge it connects to into a list, put them into the general list
                for (int y = 0; y<=list_edges.size()-1;y++) {
                    if (tileIsConnected(list_tiles.get(x),list_edges.get(y))) { // if the tile "i" connected to the edge
                        ArrayList<String> i = new ArrayList<>();
                        i.add(list_tiles.get(x));
                        i.add(list_edges.get(y));
                        general.add(i); // just put this edge with the "i" itself into the list, put the list into the general list.
                    }
                }
            }
            if (list_tiles.get(x).charAt(1) != 'i') {
                ArrayList<String> a = tile_connect_edges(list_tiles.get(x),list_edges);
                if (a.size() != 0) {
                    general.add(a);
                    a.add(list_tiles.get(x));// put the tile into the list for counting scores
                }
            }
        }

        for (int x = 0; x<=general.size()-1;x++) {
            for (int y = x+1; y <=general.size()-1;y++) {//get two paths and find if they are connected
                for (int index_1 = 0; index_1 <= general.get(x).size() - 1; index_1++) {
                    for (int index_2 = 0; index_2 <= general.get(y).size() - 1; index_2++){
                        if (general.get(x).get(index_1).length() > 4 && general.get(y).get(index_2).length() > 4){// avoid compare tiles
                            if (general.get(x).get(index_1).equals(general.get(y).get(index_2)) && (x != y)) {// if any two edges are repeated, means the two "paths" are connected
                                general.get(x).addAll(general.get(y));// combine the two connected "paths"
                                general.remove(y);
                                Set<String> edgesSet = new LinkedHashSet<>(general.get(x));
                                general.set(x, new ArrayList<>(edgesSet));// remove the repeating edges
                                y = x+1;// begin at the new list's next list
                                index_1 = 0;// begin at the new list's first element
                                index_2 = -1;// after combination, the ge.get(y) is new list , begin at this list's first element.
                            }
                            if (general.size() == 1 || y == general.size()) return general;// if there is only one "path" or the last two lists combine, just return
                        }
                    }
                }
            }
        }
        return general;
    }

    // find the edges that a tile connects to
    public static ArrayList<String> tile_connect_edges(String tile,ArrayList<String> list_edges) {
        ArrayList<String> connect = new ArrayList<>();
        for (int y = 0; y<= list_edges.size()-1;y++) {
            if (tileIsConnected(tile,list_edges.get(y))) {
                connect.add(list_edges.get(y));
            }
        }
        return connect;
    }
    // find if a tile and the edge are connected
    public static boolean tileIsConnected(String tile,String edge) {
        String tileNode = tile.substring(2,4);
        String node1 = edge.substring(1,3);
        String node2 = edge.substring(3,5);
        return (tileNode.equals(node1) || tileNode.equals(node2));
    }
    // counting the Strength of the given tile
    public static int[] countingScores(String tile,int score_O, int score_B){
        int[] output = {0,0};
        switch (tile.substring(0,2)) {
            case "Oa":
            case "Of":
                score_O = score_O + 1;break;
            case "Ob":
            case "Od":
            case "Oh":
                score_O = score_O + 3;break;
            case "Oc":
                score_O = score_O + 4;break;
            case "Oe":
            case "Og":
                score_O = score_O + 2;break;

            case "Ba":
            case "Bf":
                score_B = score_B + 1;break;
            case "Bb":
            case "Bd":
            case "Bh":
                score_B = score_B + 3;break;
            case "Bc":
                score_B = score_B + 4;break;
            case "Be":
            case "Bg":
                score_B = score_B + 2;break;
            default:
        }
        output[0] = score_O;
        output[1] = score_B;
        return output;
    }
    // finding the rank of the given tile
    public static Character countingRanks(String tile) {
        switch (tile.substring(0,2)) {
            case "Oa":
            case "Ob":
            case "Oc":
            case "Od":
            case "Oe":
            case "Ba":
            case "Bb":
            case "Bc":
            case "Bd":
            case "Be":
                return tile.toUpperCase().charAt(1);
            default: return null;
        }
    }

    /**
     * Given the current game state, and one or two flipped playing tiles,
     * generate a valid playing action.
     * <p>
     * A playing action is variable length string, consisting of one or two sub-actions.
     * NOTE: The choice between one or two is explained in {@link #isActionValid(String[], String)}
     * <p>
     * To be valid, the playing action must:
     * 1- include all target playing tiles,
     * 2- have all of the conditions explained in {@link #isActionValid(String[], String)}
     *
     * @param state an array of two strings, representing the current game state
     * @param tiles a string representing one or two flipped playing tiles, as described in
     *              {@link #selectTiles(String)}
     * @return a string representing a playing action on the target tile(s)
     * @author Fangxu Zhao (u6822201)
     *
     */
    public static String generateAction(String[] state, String tiles) {
        // FIXME - Task +10

        String[] list_first = {"0","1","2","3"};
        String[] list_second = {"0","1","2","3","4","5","6","7","8","9"};
        String output = tiles;
        int count_nodes = 0;
        ArrayList<String> edges = new ArrayList<>();
        ArrayList<String> usedNodes = new ArrayList<>();
        ArrayList<String> available = new ArrayList<>();

        // get the list of nodes
            for (int first = 0; first <= list_first.length - 1; first++) {
                for (int second = 0; second <= list_second.length - 1; second ++) {
                    available.add(list_first[first] + list_second[second]);
                    count_nodes = count_nodes + 1;
                    if (count_nodes > 31) {break;} // nodes: 00 ~ 31
                }
            }

        // get all the edges
        for (int x = 0; x<=state[1].length()-1;x+=5){
            edges.add(state[1].substring(x,x+5));
        }

        // remove the nodes that have been used
        for (int x = 0; x<=state[0].length() - 1;x+=4) {
            usedNodes.add(state[0].substring(x+2,x+4));
        }
        available.removeAll(usedNodes);

        // randomly pick available nodes from the available nodes
        if (tiles.length() == 2) { // length is 2
            if (tiles.charAt(1) != 'j') { // if it is not "j"
                output = non_warpPick(available,output,state);
                return output;
            } else { // if it is "j"
                output = warp_Pick(available,edges,output);
                return output;
            }
        } else { // length is 4 (two tiles)
            if (tiles.charAt(1) == 'j') { // first is 'j'
                output = warp_Pick(available, edges, output.substring(0,2));
                output = output + tiles.substring(2, 4);

                if (tiles.charAt(9) == 'j') { // both is 'j'
                    output = warp_Pick(available, edges, output);
                    return output;

                } else { // only first is "j"
                    output = non_warpPick(available,output,state);
                    return output;
                }
            } else {
                if (tiles.charAt(5) == 'j') { // second is 'j'
                    output = warp_Pick(available, edges, output);
                    return output;
                } else { // both not 'j'
                    output = non_warpPick(available,output.substring(0,2),state);
                    output = output + tiles.substring(2,4);
                    output = non_warpPick(available,output,state);
                    return output;
                }
            }
        }
    }

    public static String warp_Pick(ArrayList<String> available, ArrayList<String> edges, String output) {
        Random in = new Random();
        int a = in.nextInt(available.size());
        String node = available.get(a);
        return (output + node + connectedNodes(edges,node)); // the connected function make sure that it is valid
    }

    public static String non_warpPick(ArrayList<String> allNodes, String output, String[] state) {
        for (int x = 0; x<= allNodes.size()-1 ; x++) {
            output = output + allNodes.get(x);
            if (isActionValid(state,output)) {return output;}
        }
        return output; // if there is no valid node for this tile.
    }

    public static String connectedNodes(ArrayList<String> edges,String node) {
        String output = "";
        for (int x = 0; x<=edges.size()-1;x++) {
            String node_1 = edges.get(x).substring(1,3);
            String node_2 = edges.get(x).substring(3,5);
            if (node_1.equals(node)) {output = output + node_2;}
            else if (node_2.equals(node)) {output = output + node_1;}
            if (output.length() == 4) {miniFirst(output,0,2,2,4);return output;}
        }

        if (output.length() == 2) {
            return output+output;// if there is only one connecting edges
        } else {
            return null; // there is no edges connected to this node (Actually impossible)
        }
    }
}