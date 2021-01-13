package comp1140.ass2.gui;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

class Edges extends ImageView {
    private static final int EDGE_WIDTH = 30;
    private static final int EDGE_LENGTH = 75;

    /* Define a drop shadow effect that we will apply to tiles */
    private static DropShadow dropShadow;

    /* Static initializer to initialize dropShadow */
    static {
        dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.color(0, 0, 0, .4));
    }

    Edges(String edgeType, String edgePos){
        setFitHeight(EDGE_WIDTH);
        setFitWidth(EDGE_LENGTH);
        setLayout(edgePos);
        setImage(new Image(Viewer.class.getResource(Viewer.URI_BASE + "edges/" + edgeType + ".png").toString()));
        // one function to help detect tiles
        // System.out.println(Viewer.URI_BASE + tile + ".png");
        setEffect(dropShadow);
    }

    private void setLayout(String edgePos) {
        switch (edgePos) {
            case "0001":
                position(59, 189, -60);
                break;
            case "0004":
                position(85,209,0);
                break;

            case "0105":
                position(144,75,-30);
                break;

            case "0203":
                position(137,377,90);
                break;
            case "0204":
                position(126, 308, -60);
                break;
            case "0206":
                position(158,346,30);
                break;

            case "0306":
                position(143,452, -30);
                break;
            case "0307":
                position(159,484,30);
                break;

            case "0408":
                position(213,196,-30);
                break;
            case "0409":
                position(227,227,30);
                break;

            case "0508":
                position(272,55,60);
                break;
            case "0510":
                position(275,21,0);
                break;

            case "0611":
                position(274,398,0);
                break;

            case "0712":
                position(273,535,0);
                break;

            case "0809":
                position(328,188.5,90);
                break;
            case "0813":
                position(344,141,0);
                break;

            case "0911":
                position(341,314.02,60);
                break;

            case "1015":
                position(414,22,0);
                break;

            case "1112":
                position(396,443.5,90);
                break;
            case "1114":
                position(383.76,379,-60);
                break;

            case "1216":
                position(400.52,521,-30);
                break;
            case "1217":
                position(417,552.26,30);
                break;

            case "1314":
                position(466.5,188.5,90);
                break;
            case "1315":
                position(453.76,122,-60);
                break;

            case "1418":
                position(470.52,266,-30);
                break;
            case "1419":
                position(485,296.26,30);
                break;

            case "1520":
                position(554,38.26,30);
                break;

            case "1617":
                position(516.5,513.5, 90);
                break;
            case "1619":
                position(503.76,448,-60);
                break;

            case "1722":
                position(532, 604, 0);
                break;

            case "1820":
                position(573.76, 190, -60);
                break;
            case "1823":
                position(601,210,0);
                break;

            case "1921":
                position(599, 382.02, 60);
                break;
            case "1924":
                position(601,347, 0);
                break;

            case "2025":
                position(671, 90, 0);
                break;

            case "2122":
                position(653.5, 512.5, 90);
                break;
            case "2126":
                position(673,484.26, 30);
                break;

            case "2226":
                position(658.52, 590, -30);
                break;

            case "2324":
                position(723.5, 256.5, 90);
                break;
            case "2325":
                position(711.76,190,-60);
                break;

            case "2427":
                position(727.52, 333, -30);
                break;
            case "2428":
                position(742,365.26, 30);
                break;

            case "2529":
                position(811,107.26,30);
                break;

            case "2628":
                position(760.76, 516,-60);
                break;

            case "2728":
                position(842.5, 323.5, 90);
                break;
            case "2729":
                position(830.76, 260, -60);
                break;

            case "2831":
                position(858, 417, 0);
                break;

            case "2930":
                position(926, 194.02, 60);
                break;

            case "3031":
                position(981,324.5,90);
                break;
        }
    }

    private void position(double x, double y, double angle){
        setLayoutX(x);
        setLayoutY(y + Viewer.MARGIN);

        Rotate rotate = new Rotate(angle);
        getTransforms().add(rotate);

    }
}
