package comp1140.ass2.gui;

import comp1140.ass2.gui.Kits.CloseFunction;
import comp1140.ass2.gui.Kits.Menus;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class Board extends Application {

    /**
     * This Board is a stage. It has three scenes.
     * 1. Main welcome.
     * 2. Agamemnon.
     * 3. Loom variant.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Welcomer welcomer = new Welcomer();
        Scene welcome = welcomer.getWelcomer();

        ImageView imageView = welcomer.makeControls();
        imageView.setOnMouseClicked(e -> {
            Viewer viewer = new Viewer();
            Scene scene = viewer.getViewer();
            primaryStage.setScene(scene);
        });


        // stage
        primaryStage.setTitle("Agamemnon");
        primaryStage.setScene(welcome);

        primaryStage.setOnCloseRequest(e -> CloseFunction.mainClose(e, primaryStage));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
