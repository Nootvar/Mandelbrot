package controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import model.BigDouble;
import model.Mandelbrot;
import view.MainViewController;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class SemiPreciseMainController implements IMainController {
    private int height, width;
    private double halfHeight, halfWidth;
    private int widthPart;
    private int thread;

    private int missing;
    private int threadFinished;
    private Color[][] colors;
    private Canvas canvas;
    private PixelWriter pixels;
    private BigDouble zoom;
    private BigDouble posX, posY;
    private MainViewController view;

    public SemiPreciseMainController(int height, int width) {
        this.zoom = new BigDouble(0.005);
        this.posX = new BigDouble(-1);
        this.posY = new BigDouble(0);
        this.threadFinished = 0;
        setSize(height, width);
        setCanvas();
    }

    private void setSize(int height, int width) {
        this.thread = width/100;
        if (thread < 10)
            thread = 10;
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
        return posX.doubleValue();
    }

    public void setPosX(double posX) {
        this.posX = new BigDouble(posX);
    }

    public double getPosY() {
        return posY.doubleValue();
    }

    public void setPosY(double posY) {
        this.posY = new BigDouble(posY);
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
            for (int i = 0; i < thread - 1; i++) {
                int index = i;
                new Thread(() -> setThread(index * widthPart, (index + 1) * widthPart)).start();
            }
            new Thread(() -> setThread((thread-1) * widthPart, (thread * widthPart) + missing)).start();
        } else
            System.out.println("Rendering process incomplete");
    }

    public void setThread(int from, int to) {
        for (int i = from; i < to; i++)
            for (int j = 0; j < height; j++) {
                BigDouble x = new BigDouble(i - (halfWidth)).multiply(zoom).add(posX);
                BigDouble y = new BigDouble(j - (halfHeight)).multiply(zoom).add(posY);

                colors[i][j] = Mandelbrot.getColorBigDouble(x, y);
            }
        hasFinished();
        //draw(from, to);
    }

    private synchronized void hasFinished() {
        threadFinished++;
        System.out.println(threadFinished);
        if (threadFinished == thread) {
            threadFinished = 0;
            draw();
        }
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
        zoom = zoom.multiply(new BigDouble(0.5));
        setMandelbrot();
    }

    public void dezoom() {
        zoom = zoom.divide(new BigDouble(0.5));
        setMandelbrot();
    }

    public void up() {
        posY = posY.subtract(new BigDouble(width/8.).multiply(zoom));
        setMandelbrot();
    }

    public void down() {
        posY = posY.add(new BigDouble(width/8.).multiply(zoom));
        setMandelbrot();
    }

    public void left() {
        posX = posX.subtract(new BigDouble(width/8.).multiply(zoom));
        setMandelbrot();
    }

    public void right() {
        posX = posX.add(new BigDouble(width/8.).multiply(zoom));
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
