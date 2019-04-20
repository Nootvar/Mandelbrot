package controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import model.Mandelbrot;
import view.MainViewController;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class MainController implements IMainController {

    private int height, width;
    private double halfHeight, halfWidth;
    private int widthPart;
    private int thread;

    private int missing;
    private int threadFinished;
    private Color[][] colors;
    private Canvas canvas;
    private PixelWriter pixels;
    private double zoom;
    private double posX, posY;
    private MainViewController view;

    public MainController(int height, int width) {
        this.zoom = 0.005;
        this.posX = -1;
        this.posY = 0;
        this.threadFinished = 0;
        setSize(height, width);
        setCanvas();
    }

    private void setSize(int height, int width) {
        this.thread = width/100;
        this.colors = new Color[width][height];
        this.height = height;
        this.width = width;
        this.halfHeight = height/2.0;
        this.halfWidth = width/2.0;
        this.widthPart = width/ thread;
        this.missing = width - (widthPart*thread);
        fill(Color.BLACK);
    }

    private void setCanvas() {
        canvas = new Canvas();
        canvas.setHeight(height);
        canvas.setWidth(width);
        pixels = canvas.getGraphicsContext2D().getPixelWriter();
        setMandelbrot();
    }

    public MainViewController set(MainViewController view) {
        this.view = view;
        return view;
    }

    public void recolor() {
        double max = ThreadLocalRandom.current().nextDouble(0.0, 255);
        Mandelbrot.rMod = ThreadLocalRandom.current().nextDouble(0.0, max);
        Mandelbrot.gMod = ThreadLocalRandom.current().nextDouble(0.0, max);
        Mandelbrot.bMod = ThreadLocalRandom.current().nextDouble(0.0, max);
        setMandelbrot();
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    private synchronized void fill(Color color) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                colors[i][j] = color;
            }
        }
    }

    private synchronized void setMandelbrot() {
        if (threadFinished == 0) {
            RenderTask task;
            for (int i = 0; i < thread - 1; i++) {
                int from = i * widthPart;
                int to = (i + 1) * widthPart;
                task = new RenderTask(from, to, this);
                task.setOnSucceeded(value -> hasFinished(from, to));
                new Thread(task).start();
            }
            int from = (thread-1) * widthPart;
            int to = (thread * widthPart) + missing;
            task = new RenderTask(from, to, this);
            task.setOnSucceeded(value -> hasFinished(from, to));
            new Thread(task).start();
        } else
            System.out.println("Rendering process incomplete");
    }

    public void setThread(int from, int to) {
        for (int i = from; i < to; i++)
            for (int j = 0; j < height; j++) {
                colors[i][j] = Mandelbrot.getColorDouble((i - (halfWidth)) * zoom + posX, (j - (halfHeight)) * zoom + posY);
            }
        //draw(from, to);
    }

    private synchronized void hasFinished(int from, int to) {
        threadFinished++;
        if (threadFinished == thread) {
            threadFinished = 0;
        }
        draw(from, to);
    }

    private synchronized void draw() {
        draw(0, width);
    }

    private synchronized void draw(int from, int to) {
        for (int i = from; i < to; i++) {
            for (int j = 0; j < height; j++) {
                pixels.setColor(i,j,colors[i][j]);
            }
        }
        pixels.setColor(height/2, width/2, Color.RED);
    }

    public void zoom() {
        if (posX * 2 > 0 && posX < 0 || posX * 2 < 0 && posX > 0 || posY * 2 > 0 && posY < 0 || posY * 2 < 0 && posY > 0) {
            System.out.println("Zoom limit reached");
        } else {
            zoom *=0.5;
            setMandelbrot();
        }
    }

    public void dezoom() {
        zoom /=0.5;
        setMandelbrot();
    }

    public void up() {
        posY -= width/8.*zoom;
        setMandelbrot();
    }

    public void down() {
        posY += width/8.*zoom;
        setMandelbrot();
    }

    public void left() {
        posX -= width/8.*zoom;
        setMandelbrot();
    }

    public void right() {
        posX += width/8.*zoom;
        setMandelbrot();
    }

    public void save() {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));

        //Prompt user to select a file
        File file = fileChooser.showSaveDialog(null);

        if(file != null){
            try {
                //Pad the capture area
                WritableImage writableImage = new WritableImage(width,height);
                canvas.snapshot(null, writableImage);

                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                //Write the snapshot to the chosen file
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) { ex.printStackTrace(); }
        }
    }

    public void resize(int height, int width) {
        setSize(height, width);
        setCanvas();
        view.put(canvas);
        setMandelbrot();
    }
}
