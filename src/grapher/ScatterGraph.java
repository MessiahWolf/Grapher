/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grapher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author rcher
 */
public class ScatterGraph implements Graph {

    // -> Variable Declaration
    private Ellipse2D.Float[] shapeArr;
    private Color[] colorList;
    // Arrays
    private float[] data;
    private float[] copy;
    // Data types
    private int widthConstraint;
    private int heightConstraint;
    // Rand. Generator
    private final Random generator = new Random();
    // <- End of Variable Declaration

    @Override
    public void init(float[] data) {
        this.data = data;

        //
        shapeArr = new Ellipse2D.Float[data.length];
        colorList = new Color[data.length];
    }

    @Override
    public void resize(int widthConstraint, int heightConstraint) {

        // Keep these for future reference in the paint call.
        this.widthConstraint = widthConstraint;
        this.heightConstraint = heightConstraint;

        // We're going to need a modified version of the data to fit within the panel, but still retain our original datum.
        copy = Arrays.copyOf(data, data.length);
        shapeArr = new Ellipse2D.Float[data.length];
        colorList = new Color[data.length];

        // Values for keeping the data in respect to the heightConstraint.
        int temp = heightConstraint;
        int scaleCount = 1;
        float high = 0;

        // Find the highest value in the datum.
        for (float f : data) {
            high = f > high ? f : high;
        }

        // If the highest value in the data exceeds the height of the panel then we're going to scale it down to the height of the panel.
        if (high > temp) {

            // Keep going until we're on the same scale as the high
            while (temp <= high) {
                temp *= 2;
                scaleCount++;
            }

            // Add a few buffer scales to keep it better inside the constraints.
            scaleCount += 2;

            // Now we divide all the values in data by the scale factor and we're done.
            for (int i = 0; i < copy.length; i++) {

                // Scale them down.
                copy[i] /= scaleCount;
            }
        } else if (high < temp) {

            // Scale it up
            while (temp >= high) {
                temp /= 2;
                scaleCount++;
            }

            // Buffer with some additional scaling
            scaleCount += 2;

            // Scale em up, baby!
            for (int i = 0; i < copy.length; i++) {
                copy[i] *= scaleCount;
            }
        }

        // Make the shapes out of the values
        for (int i = 0; i < copy.length; i++) {

            // Keep the bars the same thickness and even with the width of the panel.
            float posw = (widthConstraint - 5) / copy.length;
            float posx = i * (posw);

            // Since the datum in copy has already been scaled down to respect the heightConstraint then we just need the value.
            float posy = copy[i];
            float posh = (heightConstraint - 5) - posy;

            //
            shapeArr[i] = new Ellipse2D.Float(copy[i], posh, 6, 6);
            colorList[i] = new Color((int) (generator.nextDouble() * 255), (int) (generator.nextDouble() * 255), (int) (generator.nextDouble() * 255));
        }

        // There's always a cleaner more effiecient way to achieve this, and some math or just smart person can improve on this.
    }

    @Override
    public void paint(Graphics monet, Point position) {

        // Cast to 2D Graphics
        final Graphics2D manet = (Graphics2D) monet;

        // Apply the anti-aliasing
        manet.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Just going over the array of edited values
        for (int i = 0; i < copy.length; i++) {

            // Keep the bars the same thickness and even with the width of the panel.
            float posw = (widthConstraint - 5) / copy.length;
            float posx = i * (posw);

            // Since the datum in copy has already been scaled down to respect the heightConstraint then we just need the value.
            float posy = copy[i];
            float posh = (heightConstraint - 5) - posy;

            // Make actual shapes out of the that so we can keep our float precisions.
            final Ellipse2D.Float rf = shapeArr[i];
            final Rectangle2D rect = manet.getFontMetrics().getStringBounds(String.valueOf(data[i]), monet);
            final boolean inside = rf.contains(position);

            //
            if (inside) {
                
                // Fill in the bar with a random color for now.
                manet.setColor(colorList[i]);
                manet.fill(rf);
            }

            // Draw the actual value on the bar and outline the bar
            manet.setColor(Color.BLACK);
            manet.draw(rf);

            //
            if (inside) {
                manet.drawString(String.valueOf(data[i]), rf.x + (rf.width / 2) - (int) (rect.getWidth() / 2), rf.y - (int) (rect.getHeight()));
            }
        }

        // Apply the anti-aliasing
        manet.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
}
