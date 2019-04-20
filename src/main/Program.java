package main;

import controller.IMainController;
import controller.MainController;
import controller.PreciseMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import view.MainViewController;
import view.ConfigViewController;

public class Program extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader (getClass().getResource("/view/MainView.fxml"));
        IMainController mc = new MainController(1080, 1920);
        loader.setController(mc.set(new MainViewController(mc)));


        Parent root = loader.load();
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ADD) {
                mc.zoom();
            } else if (keyEvent.getCode() == KeyCode.SUBTRACT) {
                mc.dezoom();
            } else if (keyEvent.getCode() == KeyCode.UP) {
                mc.up();
            } else if (keyEvent.getCode() == KeyCode.DOWN) {
                mc.down();
            } else if (keyEvent.getCode() == KeyCode.LEFT) {
                mc.left();
            } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                mc.right();
            } else if (keyEvent.getCode() == KeyCode.S) {
                mc.save();
            } else if (keyEvent.getCode() == KeyCode.C) {
                mc.recolor();
            } else if (keyEvent.getCode() == KeyCode.R) {
                try {
                    resize(mc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (keyEvent.getCode() == KeyCode.L) {
                mc.resize(mc.getHeight(), mc.getWidth());
            }
        });

        stage.setScene(scene);
        stage.setTitle("Mandelbrot");
        //stage.getIcons().add(new Image(getClass().getResourceAsStream("../view/robot.png")));
        stage.setFullScreen(true);

        stage.show();
    }

    private void resize(IMainController mc) throws Exception {
        FXMLLoader loader = new FXMLLoader (getClass().getResource("/view/ConfigView.fxml"));
        loader.setController(new ConfigViewController(mc));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.setTitle("Configuration");
        //stage.getIcons().add(new Image(getClass().getResourceAsStream("../view/robot.png")));

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
