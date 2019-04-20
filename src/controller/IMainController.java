package controller;


import javafx.scene.canvas.Canvas;
import view.MainViewController;

public interface IMainController {
    void resize(int width, int height);
    void save();
    void right();
    void left();
    void down();
    void up();
    void dezoom();
    void zoom();
    Canvas getCanvas();
    int getHeight();
    int getWidth();
    MainViewController set(MainViewController view);
    void recolor();
    double getPosX();
    void setPosX(double posX);
    double getPosY();
    void setPosY(double posY);
    void setThread(int from, int to);
}
