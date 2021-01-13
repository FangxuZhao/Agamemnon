package comp1140.ass2.gui;

import comp1140.ass2.Agamemnon;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * A very simple viewer for board states in the Agamemnon game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various board states.
 *
 */
public class Viewer extends Board {

    /* board layout */
    private static final int SQUARE_SIZE = 60;
    private static final int VIEWER_WIDTH = 1024;
    private static final int VIEWER_HEIGHT = 768;
    private static final int baseBoardWidth = 1024;
    private static final int baseBoardHeight = 653;
    public static final int MARGIN = 0;

    /* URL related */
    static final String URI_BASE = "/comp1140/ass2/gui/assets/";
    private static final String BASEBOARD_URI = Viewer.class.getResource(URI_BASE + "baseboard.png").toString();

    /* Useful groups */
    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group board = new Group();
    private final Group tiles = new Group();
    private final Group dock = new Group();

    /*State representing*/
    private String tilesString = "";
    private String edgesString = "S0001S0004F0105L0204F0206L0203L0306S0307L0408S0409S0510F0508F0611S0712F0813S0809S0911S1015F1114L1112S1216F1217S1315F1314L1418L1419F1520L1619S1617F1722L1820L1823S1924F1921F2025L2126F2122L2226F2325F2324F2427S2428L2529L2628L2729L2728S2831S2930S3031";
    String[] state = {tilesString, edgesString};


    /* Nodes, predefined */
    private TextField tilesTextField;
    private TextField edgesTextField;

    /* Define a drop shadow effect that we will apply to tiles */
    private static DropShadow dropShadow;

    /* Static initializer to initialize dropShadow */ {
        dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.color(0, 0, 0, .4));
    }

    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param state an array of two strings, representing the current game state
     */
    private void displayState(String[] state) throws IllegalAccessException {
        // Task 4: implement the simple state viewer
        //if(!Agamemnon.isStateValid(state) || !Agamemnon.isStateWellFormed(state)) {
        //    throw new IllegalAccessException("Bad state: not well-formed or not valid");
        //}
        //else {
            tiles.getChildren().clear();

            String edgePlacement = state[1];
            for (int i = 0; i < edgePlacement.length() - 1; i += 5) {
                String edgeType = edgePlacement.substring(i, i + 1);
                //System.out.println(edgeType);
                String edgePos = edgePlacement.substring(i + 1, i + 5);
                //System.out.println(edgePos);
                tiles.getChildren().add(new Edges(edgeType, edgePos));
            }

            String tilePlacement = state[0];
            for (int i = 0; i < tilePlacement.length() - 1; i += 4) {
                String tileType = tilePlacement.substring(i, i + 2);
                //System.out.println(tileType);
                String tilePos = tilePlacement.substring(i + 2, i + 4);
                //System.out.println(tilePos);
                tiles.getChildren().add(new Tiles(tileType, tilePos));
            }

            makeStart();
        }
    //}

    /**
     * Prepare the board ready for game.
     */
    void makeBoard(){
        board.getChildren().clear();

        ImageView baseBoard = new ImageView();
        baseBoard.setImage(new Image(BASEBOARD_URI));
        baseBoard.setFitHeight(baseBoardHeight);
        baseBoard.setFitWidth(baseBoardWidth);
        //System.out.println(baseBoardHeight);
        baseBoard.setLayoutX(0);
        baseBoard.setLayoutY(MARGIN);
        baseBoard.setEffect(dropShadow);

        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(768);
        rectangle.setWidth(1024);
        rectangle.setFill(Color.web("#D8D8D8"));

        board.getChildren().addAll(rectangle,baseBoard);

        board.toBack(); // make the board at the bottom of other elements.

        // System.out.println(BASEBOARD_URI); // Useful for debugging.

    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    void makeControls() {
        // Node
        Label pieceLabel = new Label("Tiles:");
        tilesTextField = new TextField();
        tilesTextField.setPrefWidth(200);

        Label edgesLabel = new Label("Edges:");
        edgesTextField = new TextField();
        edgesTextField.setPrefWidth(300);

        Button button = new Button("Refresh");
        button.setOnAction(e -> {
            try {
                displayState(new String[]{tilesTextField.getText(), edgesTextField.getText()});
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        });

        // Layout
        HBox hBox = new HBox();
        hBox.getChildren().addAll(pieceLabel, tilesTextField, edgesLabel, edgesTextField, button);
        hBox.setAlignment(Pos.BASELINE_CENTER);
        hBox.setSpacing(10);
        //hBox.setLayoutX(160);
        //hBox.setLayoutY(VIEWER_HEIGHT - 60);


        controls.getChildren().add(hBox);

    }

    void makeStart() {
        dock.getChildren().clear();

        ImageView startButton = new ImageView(new Image((Viewer.class.getResource(URI_BASE + "start.png").toString())));
        startButton.setLayoutX(425);
        startButton.setLayoutY(671);
        dock.getChildren().add(startButton);

        startButton.setOnMouseEntered(e -> {
            startButton.setStyle("-fx-cursor: hand");
        });
        startButton.setOnMouseExited(e -> {
        });
        startButton.setOnMouseClicked(e -> {
            makeDock();
        });

    }

    private ImageView tileB1, tileB2, tileO1, tileO2;
    void makeDock(){
        dock.getChildren().clear();

        tileB1 = setInitialBack('B');
        transition(tileB1, 114, -40);

        tileB2 = setInitialBack('B');
        transition(tileB2,114, -10);

        tileO1 = setInitialBack('O');
        transition(tileO1,-114,-40);

        tileO2 = setInitialBack('O');
        transition(tileO2,-114,-10);

        ImageView bTiles = new ImageView();
        bTiles.setImage(new Image((Viewer.class.getResource(URI_BASE + "BTiles.png").toString())));
        bTiles.setLayoutX(171);
        bTiles.setLayoutY(663);

        ImageView oTiles = new ImageView();
        oTiles.setImage(new Image((Viewer.class.getResource(URI_BASE + "OTiles.png").toString())));
        oTiles.setLayoutX(769);
        oTiles.setLayoutY(663);

        dock.getChildren().addAll(bTiles, oTiles);

        String flippedTiles = Agamemnon.selectTiles(tilesString);
        if(flippedTiles.charAt(0) == 'O') {
            if(flippedTiles.length() == 2){
                flipTile(tileO1, flippedTiles);
                dock.getChildren().add(tileO1);
            }
            if(flippedTiles.length() == 4) {
                flipTile(tileO1, flippedTiles.substring(0,2));
                flipTile(tileO2, flippedTiles.substring(2,4));
                dock.getChildren().addAll(tileO1, tileO2);
            }
        }
        if(flippedTiles.charAt(0) == 'B') {
            if(flippedTiles.length() == 2){
                flipTile(tileB1, flippedTiles);
                dock.getChildren().add(tileB1);
            }
            if(flippedTiles.length() == 4) {
                flipTile(tileB1, flippedTiles.substring(0,2));
                flipTile(tileB2, flippedTiles.substring(2,4));
                dock.getChildren().addAll(tileB1, tileB2);
            }
        }
    }

    private ImageView setInitialBack(char player) throws IllegalArgumentException {
        if (player == 'B') {
            ImageView tileB = new ImageView();
            tileB.setImage(new Image(Viewer.class.getResource(URI_BASE + "tiles/B.png").toString()));
            tileB.setLayoutX(201);
            tileB.setLayoutY(713);
            tileB.setFitWidth(45);
            tileB.setFitHeight(45);

            return tileB;
        }
        if (player == 'O') {
            ImageView tileO1 = new ImageView();
            tileO1.setImage(new Image(Viewer.class.getResource(URI_BASE + "tiles/O.png").toString()));
            tileO1.setLayoutX(778);
            tileO1.setLayoutY(713);
            tileO1.setFitWidth(45);
            tileO1.setFitHeight(45);

            return tileO1;
        }
        else
            throw new IllegalArgumentException("not O or B");
    }

    private void transition(ImageView node, double relativeX, double relativeY) {
        TranslateTransition translate = new TranslateTransition(Duration.millis(500));
        translate.setToX(relativeX);
        translate.setToY(relativeY);
        translate.setNode(node);
        translate.play();

        ScaleTransition scale = new ScaleTransition(Duration.millis(500));
        scale.setToX(1.37);
        scale.setToY(1.37);
        scale.setNode(node);
        scale.play();
    }

    private void flipTile(ImageView tile, String flippedTile) {
        tile.setOnMouseClicked(e -> {
            makeDrag(tile, flippedTile);
        });
    }

    private void makeDrag(ImageView tile, String flippedTile) {
        double homeX = 0, homeY = 0;
        if (tile.equals(tileB1)) {
            homeX = 201 + 114;
            homeY = 713 - 40;
        }
        else if(tile.equals(tileB2)){
            homeX = 201 + 114;
            homeY = 713 - 10;
        }
        else if(tile.equals(tileO1)) {
            homeX = 778 - 114;
            homeY = 713 - 40;
        }
        else if(tile.equals(tileO2)) {
            homeX = 778 - 114;
            homeY = 713 - 10;
        }

        dock.getChildren().remove(tile);
        DraggableTile dt = new DraggableTile(flippedTile, homeX, homeY);
        System.out.println("??" + flippedTile);
        dock.getChildren().add(dt);
        System.out.println("hello:" + flippedTile);
        state = Agamemnon.applyAction(state, flippedTile + tilesString);
        try {
            tilesString = dt.findNearestTile(dt.getLayoutX(), dt.getLayoutY());
        }catch (NullPointerException ignored) {
            tilesString = "";
        }
    }

    Scene getViewer() {
        // Node
        makeBoard();
        makeControls();
        makeStart();
        root.getChildren().add(board);
        root.getChildren().add(controls);
        root.getChildren().add(tiles);
        root.getChildren().add(dock);

        // Scene
        return new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);
    }

    @Override
    public void start(Stage stage) throws Exception {
    }
}
