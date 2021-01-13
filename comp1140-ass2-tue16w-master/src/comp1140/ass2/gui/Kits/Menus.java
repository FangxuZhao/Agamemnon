package comp1140.ass2.gui.Kits;

import comp1140.ass2.gui.Viewer;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class Menus {
    public static HBox makeMenu(){
        // Node
        // Menu
        Menu fileMenu = new Menu("File");

        // This is the proper way to add a new menu item.
        MenuItem newProject = new MenuItem("New...");
        newProject.setOnAction(e -> System.out.println("Creating a new ..."));

        fileMenu.getItems().addAll(newProject, new MenuItem("Open"), new MenuItem("Close"));
        fileMenu.getItems().add(new SeparatorMenuItem());
        fileMenu.getItems().addAll(new MenuItem("Import..."), new MenuItem("Project Structure"), new MenuItem("Exit"));

        // Edit menu
        Menu editMenu = new Menu("_Edit"); // notice the underscore here, it can quickly create a keyboard shortcut (can't use in Mac OS X)
        MenuItem undo = new MenuItem("Undo");
        undo.setDisable(true);  // make the option disable !

        editMenu.getItems().addAll(undo, new MenuItem("Copy"), new MenuItem("Cut"), new MenuItem("Paste"));

        // Help Item
        Menu helpItem = new Menu("Help");
        CheckMenuItem showLines = new CheckMenuItem("Show line Numbers");
        showLines.setOnAction(e -> {
            if (showLines.isSelected())
                System.out.println("showed");
            else
                System.out.println("not showed");
        });
        CheckMenuItem autoSave = new CheckMenuItem("AutoSave");
        autoSave.setSelected(true);
        helpItem.getItems().addAll(showLines, autoSave);

        // Difficulty menu
        Menu diffMenu = new Menu("Difficulty");
        ToggleGroup diffToggle = new ToggleGroup();

        RadioMenuItem easy = new RadioMenuItem("Easy");
        RadioMenuItem medium = new RadioMenuItem("Medium");
        RadioMenuItem hard = new RadioMenuItem("hard");
        // easy.setToggleGroup(diffToggle);
        // medium.setToggleGroup(diffToggle);
        // hard.setToggleGroup(diffToggle);
        diffToggle.getToggles().addAll(easy, medium, hard);

        diffMenu.getItems().addAll(easy, medium, hard);


        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, editMenu, helpItem, diffMenu);
        menuBar.setMinHeight(Viewer.MARGIN);
        menuBar.setStyle("-fx-background-color: transparent");

        // Layout
        HBox hBox = new HBox();
        hBox.getChildren().add(menuBar);

        return hBox;
    }
}
