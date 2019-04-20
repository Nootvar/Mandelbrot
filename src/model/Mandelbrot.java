package model;


import javafx.scene.paint.Color;

import java.math.BigDecimal;
import java.math.MathContext;

public class Mandelbrot {

    private static final int MAXIMUM_ITERATION = 2000;
    private static final double JUMP = 255./MAXIMUM_ITERATION + 0.1;
    private static final MathContext context = MathContext.DECIMAL128;

    public static double rMod, gMod, bMod;

    private static final BigDecimal TWO = new BigDecimal(2, context);
    private static final BigDecimal FOUR = new BigDecimal(4, context);

    public static Color getColor(BigDecimal c_r, BigDecimal c_i) {
        BigDecimal z_r = new BigDecimal(0, context);
        BigDecimal z_i = new BigDecimal(0, context);
        BigDecimal tmp;
        BigDecimal tot;
        int i = 0;

        do {
            tmp = z_r;
            z_r = (z_r.multiply(z_r, context)).subtract(z_i.multiply(z_i, context), context).add(c_r, context);
            z_i = (z_i.multiply(TWO, context).multiply(tmp, context)).add(c_i, context);
            i++;
            tot = (z_r.multiply(z_r, context)).add(z_i.multiply(z_r, context));
        } while (tot.compareTo(FOUR) < 0 && i < MAXIMUM_ITERATION);

        if (i == MAXIMUM_ITERATION) {
            return Color.BLACK;
        }
        return getColor(i);
    }

    public static Color getColorBigDouble(BigDouble c_r, BigDouble c_i) {
        BigDouble z_r = new BigDouble(0);
        BigDouble z_i = new BigDouble(0);
        BigDouble tmp;
        BigDouble tot;
        int i = 0;

        do {
            tmp = z_r;
            z_r = (z_r.multiply(z_r)).subtract(z_i.multiply(z_i)).add(c_r);
            z_i = (z_i.multiply(new BigDouble(2)).multiply(tmp)).add(c_i);
            i++;
            tot = (z_r.multiply(z_r)).add(z_i.multiply(z_r));
        } while (tot.compareTo(new BigDouble(4)) < 0 && i < MAXIMUM_ITERATION);

        if (i == MAXIMUM_ITERATION) {
            return Color.BLACK;
        }
        return getColor(i);
    }

    public static Color getColorDouble(double c_r, double c_i) {
        double z_r = 0;
        double z_i = 0;
        double tmp;
        double tot;
        int i = 0;

        do {
            tmp = z_r;
            z_r = z_r*z_r-z_i*z_i+c_r;
            z_i = z_i*2*tmp+c_i;
            i++;
            tot = z_r*z_r+z_i*z_r;
        } while (Double.compare(tot, 4) < 0 && i < MAXIMUM_ITERATION);

        if (i == MAXIMUM_ITERATION) {
            return Color.BLACK;
        }
        return getColor(i * (c_i - c_r));
    }

    private static Color getColor(double value) {
        int r = (int)Math.round((value) * (JUMP + rMod));
        int g = (int)Math.round((value) * (JUMP + gMod));
        int b = (int)Math.round((value) * (JUMP + bMod));
        return Color.rgb(Math.abs(r)%255, Math.abs(g)%255, Math.abs(b)%255);
    }


}
