package application;
import Calculations.AlmanacModule;
import Calculations.SatelliteCalculations;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class Main extends Application {


    public static void main(String[] args) throws IOException {
        List<List<Double>> Nav = AlmanacModule.readAlmanac("src/main/resources/Almanac2024053.alm");
        List<Double> rowNav = Nav.getFirst();
        double[]XYZ = (SatelliteCalculations.getSatPos(SatelliteCalculations.weekSecond[1],SatelliteCalculations.weekSecond[0], rowNav));
//        for(double coords : XYZ){
//            System.out.println(coords);
//        }

        launch(args);
    }


    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, Color.BEIGE);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Bleh w00t w00t");

    }

}