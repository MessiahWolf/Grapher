/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grapher;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author rcher
 */
public class BarGraph implements Graph {

    // -> Variable Declaration
    private Rectangle2D.Float[] shapeArr;
    private Point2D.Float[] pointArr;
    private Color[] colorArr;
    // Arrays
    private float[] dataArr;
    private float[] percentArr;
    private Pair[] breakpoints;
    // Data types
    private int heightConstraint;
    private int widthConstraint;
    // Rand. Generator
    private final Random generator = new Random();

    // <- End of Variable Declaration
    @Override
    public void init(float[] data) {
        this.dataArr = data;

        //
        shapeArr = new Rectangle2D.Float[data.length];
        colorArr = new Color[data.length];
        pointArr = new Point2D.Float[data.length];
        percentArr = null;
    }

    private float getStandardDeviation(float[] input) {

        float[] adjust = new float[input.length];
        float mean = 0;
        float adjMean = 0;

        for (float f : input) {
            mean += f;
        }

        // The simple mean of the data.
        mean /= input.length;

        //
        for (int i = 0; i < input.length; i++) {
            adjMean += (float) Math.pow(input[i] - mean, 2);
        }

        //
        adjMean /= adjust.length;
        adjMean = (float) Math.sqrt(adjMean);

        //
        System.out.printf("Mean: %f\nPopulation Standard Dev.: %f\n", mean, adjMean);

        //
        return adjMean;
    }

    @Override
    public void resize(int widthConstraint, int heightConstraint) {

        //
        this.widthConstraint = widthConstraint;
        this.heightConstraint = heightConstraint;

        // We're going to need a modified version of the data to fit within the panel, but still retain our original datum.
        final float[] copy = Arrays.copyOf(dataArr, dataArr.length);
        shapeArr = new Rectangle2D.Float[dataArr.length];
        colorArr = new Color[dataArr.length];

        // Values for keeping the data in respect to the heightConstraint.
        float highestValue = 0;

        // Find the highest value in the datum.
        for (float f : dataArr) {
            highestValue = f > highestValue ? f : highestValue;
        }

        // Creating the breakpoints
        int breakpointsNum = dataArr.length;
        float datumMultiplier;
        
        // Limit the breakpoints amount so we don't end up with hundreds to thousands of breakpoints
        breakpointsNum = breakpointsNum < 2 ? 2 : breakpointsNum;
        breakpointsNum = breakpointsNum > 8 ? 8 : breakpointsNum;

        // Create the array to hold them.
        breakpoints = new Pair[breakpointsNum];

        // Creating those breakpoints.
        for (int i = 0; i < breakpoints.length; i++) {
            breakpoints[i] = new Pair(heightConstraint - (i * (heightConstraint / breakpointsNum)), i * (highestValue / breakpointsNum));
        }

        // If the highest value in the data exceeds the height of the panel then we're going to scale it down to the height of the panel.
        if (highestValue >= heightConstraint) {
            
            // Assume a multiplier of zero when scaling down.
            datumMultiplier = 0;

            // Building up a multiplier to scale down by
            while (highestValue / datumMultiplier > heightConstraint) {
                datumMultiplier += .1;
            }

            // Now we divide all the values in data by the scale factor and we're done.
            for (int i = 0; i < copy.length; i++) {
                copy[i] /= datumMultiplier;
            }
        } else if (highestValue <= (heightConstraint / 2)) {

            // For values less than half the screen width we can save some processing power by forcing it to start at two.
            datumMultiplier = 2;

            //
            while (highestValue * datumMultiplier < heightConstraint) {
                datumMultiplier++;
            }
            
            //
            for (int i = 0; i < copy.length; i++) {
                copy[i] *= datumMultiplier;
            }
        }

        // Create the proportion that each bar needs to stay of the panel height
        if (percentArr == null) {
            
            //
            percentArr = new float[dataArr.length];

            //
            for (int i = 0; i < copy.length; i++) {
                percentArr[i] = copy[i] / heightConstraint;
            }
        }

        // Making the rectangles out of that.
        for (int i = 0; i < copy.length; i++) {

            // Keep the bars the same thickness and even with the width of the panel.
            float posw = (widthConstraint - 32) / copy.length;
            float posx = i * (posw);

            // Since the datum in copy has already been scaled down to respect the heightConstraint then we just need the value.
            float posy = heightConstraint * (percentArr[i]);
            float posh = heightConstraint - (posy);

            //
            shapeArr[i] = new Rectangle2D.Float(posx + 32, posh, posw, posy);
            pointArr[i] = new Point2D.Float((posx + 32) + posw, posh - 1.5f);
            colorArr[i] = new Color((int) (generator.nextDouble() * 255), (int) (generator.nextDouble() * 255), (int) (generator.nextDouble() * 255));
        }

        // There's always a cleaner more effiecient way to achieve this, and some math or just smart person can improve on this.
    }

    @Override
    public void paint(Graphics monet, Point position) {

        // Cast to 2D Graphics
        final Graphics2D manet = (Graphics2D) monet;
        manet.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //
        Point2D.Float prev = null;

        // Just going over the array of edited values
        for (int i = 0; i < dataArr.length; i++) {

            // Make actual shapes out of the that so we can keep our float precisions.
            final Point2D.Float point = pointArr[i];
            final Rectangle2D.Float rf = shapeArr[i];
            final Color color = colorArr[i];
            final Rectangle2D rsb = manet.getFontMetrics().getStringBounds(String.valueOf(dataArr[i]), manet);
            final boolean inside = rf.contains(position) || (position.x >= rf.x && position.x <= rf.x + rf.width);

            // When mouse cursor ventures inside.
            if (inside) {

                // Fill in the bar with a random color for now.
                manet.setColor(color);
                manet.fill(rf);
            }

            // Draw the actual value on the bar and outline the bar
            manet.setColor(Color.BLACK);
            manet.draw(rf);

            // Drawing the lines and points over the datum
            manet.setColor(Color.RED);
            manet.fillOval((int) point.x - 1, (int) point.y, 3, 3);
            manet.setColor(new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue()));

            // Previous point must exist to draw a line to it, duh.
            if (prev != null && inside) {

                // Draw the line to the next point
                manet.drawLine((int) point.x + 1, (int) point.y + 1, (int) prev.x + 1, (int) prev.y + 1);

                //
                final float diff = (float) Math.ceil((dataArr[i] - dataArr[i-1]));
                final String diffString = String.valueOf((int) diff);
                final Rectangle2D diffBounds = manet.getFontMetrics().getStringBounds(diffString, manet);

                // If there was growth, then green; depreciation then red.
                manet.setColor(diff > 0 ? new Color(0, 128, 0) : new Color(156, 42, 0));
                manet.drawString(diffString, rf.x + (rf.width / 2) - (int) (diffBounds.getWidth() / 2), rf.y - (int) (diffBounds.getHeight() + 1));
            }

            // Set the previous point to draw the line to
            prev = point;

            // Show the value on mouse enter.
            if (inside) {
                manet.setColor(Color.BLACK);
                manet.drawString(String.valueOf(dataArr[i]), rf.x + (rf.width / 2) - (int) (rsb.getWidth() / 2), rf.y - (int) (rsb.getHeight() / 3));
            }
        }

        // Setting up to draw the breakpoint lines.
        manet.setColor(Color.BLACK);
        manet.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .35f));

        // Going over them
        for (int i = 0; i < breakpoints.length; i++) {

            // Draw the line and the break.
            manet.drawLine(0, (int) breakpoints[i].y, widthConstraint, (int) breakpoints[i].y);
            manet.drawString(String.valueOf((int) breakpoints[i].value), 2, breakpoints[i].y - 2);
        }

        //
        manet.drawLine(32, 0, 32, heightConstraint);

        // Reset the alpha.
        manet.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        //
        manet.setColor(Color.BLACK);
        manet.drawRect(0, 0, widthConstraint - 1, heightConstraint - 1);

        // Reset the rendering hint
        manet.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    // Inner Class Declaration
    private class Pair {

        // Variable Declaration
        public int y;
        public float value;
        // End of Variable Declaration

        public Pair(int y, float value) {
            this.y = y;
            this.value = value;
        }
    }
}
