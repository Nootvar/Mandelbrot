package view;

import controller.IMainController;
import controller.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    private IMainController mc;

    public MainViewController(IMainController mc) {
        this.mc = mc;
    }

    @FXML
    private HBox root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setMinWidth(mc.getWidth());
        root.setMinHeight(mc.getHeight());
        root.getChildren().add(mc.getCanvas());
    }

    public void put(Canvas canvas) {
        root.getChildren().clear();
        root.setMinWidth(mc.getWidth());
        root.setMinHeight(mc.getHeight());
        root.getChildren().add(canvas);
    }
}
