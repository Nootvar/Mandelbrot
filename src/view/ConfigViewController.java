package view;

import controller.IMainController;
import controller.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfigViewController implements Initializable {

    private IMainController mc;

    public ConfigViewController(IMainController mc) {
        this.mc = mc;
    }

    @FXML
    private TextField width;

    @FXML
    private TextField height;

    @FXML
    private TextField posX;

    @FXML
    private TextField posY;

    @FXML
    private VBox root;

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void apply(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        mc.setPosX(Double.parseDouble(posX.getText()));
        mc.setPosY(Double.parseDouble(posY.getText()));
        mc.resize(Integer.parseInt(height.getText()), Integer.parseInt(width.getText()));
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        width.setText("" + mc.getWidth());
        height.setText("" + mc.getHeight());
        posX.setText("" + mc.getPosX());
        posY.setText("" + mc.getPosY());
    }
}
