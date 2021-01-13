package comp1140.ass2.gui.Kits;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

 public class CloseFunction {
    private static boolean answer;

    public static void mainClose(Event e, Stage stage){
        e.consume();
        boolean answer = closeConfirm();
        if (answer) stage.close();
    }

    private static boolean closeConfirm(){
        Stage stage = new Stage();

        // Node
        Label label = new Label();
        label.setText("Are you sure to exit?");

        // create two buttons
        Button yes = new Button("yes");
        yes.setOnAction(e -> {
            answer = true;
            stage.close();
        });
        Button no  = new Button("No");
        no.setOnAction(e -> {
            answer = false;
            stage.close();
        });

        // Layout
        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(20,20,20,20));
        hBox.getChildren().addAll(yes, no);
        hBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20,20,20,20));
        layout.getChildren().addAll(label, hBox);
        layout.setAlignment(Pos.CENTER);

        // Scene
        Scene scene = new Scene(layout,300, 100);

        // Stage
        stage.setTitle("Exit?");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait(); // wait for response (strictly)
        // This makes it ignore event from other windows until we take care of this pop up window.


        return answer;
    }


}
