package comp1140.ass2.gui;

import comp1140.ass2.gui.Kits.Menus;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Welcomer extends Board{
    private static final int WELCOMER_WIDTH = 1024;
    private static final int WELCOMER_HEIGHT = 768;

    private static final String URI_BASE = "/comp1140/ass2/gui/assets/";
    private static final String WELCOME_URI = Board.class.getResource(URI_BASE + "welcome.png").toString();
    private static final String UNSELECTED = Board.class.getResource(URI_BASE + "unselected.png").toString();
    private static final String SELECTED = Board.class.getResource(URI_BASE + "selected.png").toString();

    private final Group root = new Group();
    private final Group backBoard = new Group();
    private final Group controls = new Group();


    private void makeWelcomeBoard() {
        ImageView welcomeBoard = new ImageView();
        welcomeBoard.setImage(new Image(WELCOME_URI));

        welcomeBoard.setLayoutX(0);
        welcomeBoard.setLayoutY(0);

        backBoard.getChildren().add(welcomeBoard);
        backBoard.toBack();
    }

    ImageView makeControls() {
        /* Make the main play button. */
        Image unselected = new Image(UNSELECTED);
        Image selected = new Image(SELECTED);

        ImageView imageView = new ImageView(unselected);
        imageView.setFitWidth(403);
        imageView.setFitHeight(57);
        imageView.setLayoutX(311);
        imageView.setLayoutY(440);


        controls.getChildren().add(imageView);
        imageView.setOnMouseEntered(e -> {
            imageView.setImage(selected);
            imageView.setFitWidth(413);
            imageView.setFitHeight(67);
            imageView.setLayoutX(306);
            imageView.setLayoutY(437);
            imageView.setStyle("-fx-cursor: hand");
        });
        imageView.setOnMouseExited(e -> {
            imageView.setImage(unselected);
            imageView.setFitWidth(403);
            imageView.setFitHeight(57);
            imageView.setLayoutX(311);
            imageView.setLayoutY(440);
        });

        /* make the menu */
        HBox menu = Menus.makeMenu();
        menu.setSpacing(10);
        controls.getChildren().add(menu);

        return imageView;
    }

    Scene getWelcomer() {
        makeControls();
        makeWelcomeBoard();

        root.getChildren().add(backBoard);
        root.getChildren().add(controls);

        // Scene
        return new Scene(root, WELCOMER_WIDTH, WELCOMER_HEIGHT);
    }

    @Override
    public void start(Stage stage) throws Exception {
    }
}
