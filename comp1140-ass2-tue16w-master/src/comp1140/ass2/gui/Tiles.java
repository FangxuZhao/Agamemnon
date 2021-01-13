package comp1140.ass2.gui;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;


class Tiles extends ImageView{
    private static ArrayList<String> list = new ArrayList<>(Arrays.asList(
             "00", "01", "02", "03", "04", "05", "06", "07", "08", "09"
            ,"10", "11", "12", "13", "14", "15", "16", "17", "18", "19"
            ,"20", "21", "22", "23", "24", "25", "26", "27", "28", "29"
            ,"30", "31"
    ));
    private static final int SQUARE_SIZE = 60;

    /* Define a drop shadow effect that we will apply to tiles */
    private static DropShadow dropShadow;

    /* Static initializer to initialize dropShadow */ {
        dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.color(0, 0, 0, .4));
    }

    String findNearestTile(double x, double y) {
        String output = list.get(0);

        for (int a = 0; a < list.size() - 1; a++) {
            if((new Tiles(output)).distance(x, y) > (new Tiles(list.get(a)).distance(x, y))) {
                output = list.get(a);
            }
        }
        return output;
    }

        Tiles(String tileType) {
            setImage(new Image(Viewer.class.getResource(Viewer.URI_BASE + "tiles/" + tileType + ".png").toString()));
            toFront();
            setFitHeight(SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);
            setEffect(dropShadow);
        }

        Tiles(String tileType, String tilePos) {
            setFitHeight(SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);
            setLayout(tilePos);
            setImage(new Image(Viewer.class.getResource(Viewer.URI_BASE + "tiles/" + tileType + ".png").toString()));
            // one function to help detect tiles
            // System.out.println(Viewer.URI_BASE + tile + ".png");
            setEffect(dropShadow);
        }

        void setLayout(String tilePos) {
            switch (tilePos) {
                case "00":
                    position(24, 194);
                    break;
                case "01":
                    position(93, 74);
                    break;
                case "02":
                    position(93, 313);
                    break;
                case "03":
                    position(93, 451);
                    break;
                case "04":
                    position(162, 194);
                    break;
                case "05":
                    position(213, 6);
                    break;
                case "06":
                    position(213, 382);
                    break;
                case "07":
                    position(213, 520);
                    break;
                case "08":
                    position(282, 127);
                    break;
                case "09":
                    position(282, 265);
                    break;
                case "10":
                    position(352, 6);
                    break;
                case "11":
                    position(352, 383);
                    break;
                case "12":
                    position(352, 520);
                    break;
                case "13":
                    position(421, 126);
                    break;
                case "14":
                    position(420, 264);
                    break;
                case "15":
                    position(491, 6);
                    break;
                case "16":
                    position(471, 451);
                    break;
                case "17":
                    position(471, 588);
                    break;
                case "18":
                    position(540, 195);
                    break;
                case "19":
                    position(540, 332);
                    break;
                case "20":
                    position(610, 75);
                    break;
                case "21":
                    position(609, 451);
                    break;
                case "22":
                    position(609, 588);
                    break;
                case "23":
                    position(678, 195);
                    break;
                case "24":
                    position(678, 333);
                    break;
                case "25":
                    position(748, 75);
                    break;
                case "26":
                    position(728, 520);
                    break;
                case "27":
                    position(798, 264);
                    break;
                case "28":
                    position(798, 402);
                    break;
                case "29":
                    position(867, 144);
                    break;
                case "30":
                    position(936, 263);
                    break;
                case "31":
                    position(936, 402);
                    break;
            }
        }

        private void position(double x, double y) {
            setLayoutX(x);
            setLayoutY(y + Viewer.MARGIN);
        }

        double distance(double x, double y) {
            return Math.sqrt(Math.pow((x - this.getLayoutX()), 2)) + Math.sqrt(Math.pow((y - this.getLayoutY()), 2));
        }
    }

    class DraggableTile extends Tiles {
        private double mouseX, mouseY;
        private double homeX, homeY;


        DraggableTile(String tileType, double homeX, double homeY) {
            super(tileType);
            this.homeX = homeX;
            this.homeY = homeY;


            setLayoutX(homeX);
            System.out.println(homeX);
            setLayoutY(homeY);
            System.out.println(homeY);

            /* event handlers */
            setOnMousePressed(e -> {
                mouseX = e.getSceneX();
                mouseY = e.getSceneY();
                toFront();
            });

            setOnMouseDragged(e -> {
                double deltaX = e.getSceneX() - mouseX;
                double deltaY = e.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + deltaX);
                setLayoutY(getLayoutY() + deltaY);
                mouseX = e.getSceneX();
                mouseY = e.getSceneY();
                e.consume();
            });

            setOnMouseReleased(e -> {
                snapToBoardNode(getLayoutX(), getLayoutY());
            });
        }

        void snapToBoardNode(double x, double y) {
            if (getBoardNode(x, y) == null) {
                setLayoutX(homeX);
                setLayoutY(homeY);
            } else {
                String node = getBoardNode(x, y);
                setLayout(getBoardNode(x, y));
                //System.out.println(node);
            }
        }

        String getBoardNode(double x, double y) {
            double AREA = 60;
            if ((24 - AREA) <= x && x <= (24 + AREA) && (194 - AREA) <= y && y <= (194 + AREA)) {
                return ("00");
            } else if ((93 - AREA) <= x && x <= (93 + AREA) && (74 - AREA) <= y && y <= (74 + AREA)) {
                return ("01");
            } else if ((93 - AREA) <= x && x <= (93 + AREA) && (313 - AREA) <= y && y <= (313 + AREA)) {
                return ("02");
            } else if ((93 - AREA) <= x && x <= (93 + AREA) && (451 - AREA) <= y && y <= (451 + AREA)) {
                return ("03");
            } else if ((162 - AREA) <= x && x <= (162 + AREA) && (194 - AREA) <= y && y <= (194 + AREA)) {
                return ("04");
            } else if ((213 - AREA) <= x && x <= (213 + AREA) && (6 - AREA) <= y && y <= (6 + AREA)) {
                return ("05");
            } else if ((213 - AREA) <= x && x <= (213 + AREA) && (382 - AREA) <= y && y <= (382 + AREA)) {
                return ("06");
            } else if ((213 - AREA) <= x && x <= (213 + AREA) && (520 - AREA) <= y && y <= (520 + AREA)) {
                return ("07");
            } else if ((282 - AREA) <= x && x <= (282 + AREA) && (127 - AREA) <= y && y <= (127 + AREA)) {
                return ("08");
            } else if ((282 - AREA) <= x && x <= (282 + AREA) && (265 - AREA) <= y && y <= (265 + AREA)) {
                return ("09");
            } else if ((352 - AREA) <= x && x <= (352 + AREA) && (6 - AREA) <= y && y <= (6 + AREA)) {
                return ("10");
            } else if ((352 - AREA) <= x && x <= (352 + AREA) && (383 - AREA) <= y && y <= (383 + AREA)) {
                return ("11");
            } else if ((352 - AREA) <= x && x <= (352 + AREA) && (520 - AREA) <= y && y <= (520 + AREA)) {
                return ("12");
            } else if ((421 - AREA) <= x && x <= (421 + AREA) && (126 - AREA) <= y && y <= (126 + AREA)) {
                return ("13");
            } else if ((420 - AREA) <= x && x <= (420 + AREA) && (264 - AREA) <= y && y <= (264 + AREA)) {
                return ("14");
            } else if ((491 - AREA) <= x && x <= (491 + AREA) && (6 - AREA) <= y && y <= (6 + AREA)) {
                return ("15");
            } else if ((471 - AREA) <= x && x <= (471 + AREA) && (451 - AREA) <= y && y <= (451 + AREA)) {
                return ("16");
            } else if ((471 - AREA) <= x && x <= (471 + AREA) && (588 - AREA) <= y && y <= (588 + AREA)) {
                return ("17");
            } else if ((540 - AREA) <= x && x <= (540 + AREA) && (195 - AREA) <= y && y <= (195 + AREA)) {
                return ("18");
            } else if ((540 - AREA) <= x && x <= (540 + AREA) && (332 - AREA) <= y && y <= (332 + AREA)) {
                return ("19");
            } else if ((610 - AREA) <= x && x <= (610 + AREA) && (75 - AREA) <= y && y <= (75 + AREA)) {
                return ("20");
            } else if ((609 - AREA) <= x && x < (609 + AREA) && (451 - AREA) <= y && y <= (451 + AREA)) {
                return ("21");
            } else if ((609 - AREA) <= x && x <= (609 + AREA) && (588 - AREA) <= y && y <= (588 + AREA)) {
                return ("22");
            } else if ((678 - AREA) <= x && x <= (678 + AREA) && (195 - AREA) <= y && y <= (195 + AREA)) {
                return ("23");
            } else if ((678 - AREA) <= x && x <= (678 + AREA) && (333 - AREA) <= y && y <= (333 + AREA)) {
                return ("24");
            } else if ((748 - AREA) <= x && x <= (748 + AREA) && (75 - AREA) <= y && y <= (75 + AREA)) {
                return ("25");
            } else if ((728 - AREA) <= x && x <= (728 + AREA) && (520 - AREA) <= y && y <= (520 + AREA)) {
                return ("26");
            } else if ((798 - AREA) <= x && x <= (798 + AREA) && (264 - AREA) <= y && y <= (264 + AREA)) {
                return ("27");
            } else if ((798 - AREA) <= x && x <= (798 + AREA) && (402 - AREA) <= y && y <= (402 + AREA)) {
                return ("28");
            } else if ((867 - AREA) <= x && x <= (867 + AREA) && (144 - AREA) <= y && y <= (144 + AREA)) {
                return ("29");
            } else if ((936 - AREA) <= x && x <= (936 + AREA) && (263 - AREA) <= y && y <= (263 + AREA)) {
                return ("30");
            } else if ((936 - AREA) <= x && x <= (936 + AREA) && (402 - AREA) <= y && y <= (402 + AREA)) {
                return ("31");
            }
            return null;
        }
    }