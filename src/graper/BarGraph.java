/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graper;

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
    private float[] data;
    private float[] copy;
    private Pair[] breakpoints;
    // Data types
    private int heightConstraint;
    private int widthConstraint;
    private final int minimumBreakpoints = 4;
    private final int maximumBreakpoints = 8;
    private float highestValue;
    int staticScaleCount;
    // Rand. Generator
    private final Random generator = new Random();

    // <- End of Variable Declaration
    @Override
    public void init(float[] data) {
        this.data = data;

        //
        shapeArr = new Rectangle2D.Float[data.length];
        colorArr = new Color[data.length];
        pointArr = new Point2D.Float[data.length];
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
        this.heightConstraint = heightConstraint;
        this.widthConstraint = widthConstraint;

        // We're going to need a modified version of the data to fit within the panel, but still retain our original datum.
        copy = Arrays.copyOf(data, data.length);
        shapeArr = new Rectangle2D.Float[data.length];
        colorArr = new Color[data.length];

        // Values for keeping the data in respect to the heightConstraint.
        highestValue = 0;
        float lowestValue = Float.MAX_VALUE;
        boolean boost = false;
        
        // Here we're going to boost numbers into the tens place if they're single digits
        for (int i = 0; i < copy.length; i++) {
            
            // Anything greater than 10 and we're not boosting then kick out; we're done.
            if(copy[i] >= 10 && boost == false) {
                break;
            } else if (copy[i] < 10 && i == copy.length-1 && boost == false) {
                i = 0;
                boost = true;
            }
            
            // Now we're boosting without the use of multiple loops
            if (boost) {
                
                // Boost it
                copy[i] *=10;
                
                // We're done.
                if (i == copy.length-1) {
                    break;
                }
            }
        }
        
        // Find the highest and lowest values in the datum.
        for (float f : copy) {
            highestValue = f > highestValue ? f : highestValue;
            lowestValue = f < lowestValue ? f : lowestValue;
        }

        // If the highest value in the data exceeds the height of the panel then we're going to scale it down to the height of the panel.
        if (highestValue >= heightConstraint) {

            //
            System.err.println("Scaling DOWN.");

            //
            staticScaleCount = 2;

            //
            while (highestValue / staticScaleCount > heightConstraint) {
                staticScaleCount++;
            }

            //
            staticScaleCount = staticScaleCount < minimumBreakpoints ? minimumBreakpoints : staticScaleCount;

            // Limiter
            staticScaleCount = staticScaleCount > maximumBreakpoints ? maximumBreakpoints : staticScaleCount;

            // Now we divide all the values in data by the scale factor and we're done.
            for (int i = 0; i < copy.length; i++) {
                copy[i] /= staticScaleCount;
            }
        } else if (highestValue <= (heightConstraint / 2)) {

            //
            System.err.println("Scaling UP.");
            staticScaleCount = 1;

            //
            while (highestValue * staticScaleCount < (heightConstraint / 2)) {
                staticScaleCount++;
            }

            // Minimum Limiter
            staticScaleCount = staticScaleCount < minimumBreakpoints ? minimumBreakpoints : staticScaleCount;

            // Maximum Limiter
            staticScaleCount = staticScaleCount > maximumBreakpoints ? maximumBreakpoints : staticScaleCount;

            //
            for (int i = 0; i < copy.length; i++) {
                copy[i] *= staticScaleCount;
            }
        }
        
        float standardDeviation = getStandardDeviation(copy);
        int breakpointAmount = (int) Math.floor(Math.max(heightConstraint, standardDeviation) / Math.min(heightConstraint, standardDeviation)) - 1;
        breakpointAmount = breakpointAmount > maximumBreakpoints ? maximumBreakpoints :breakpointAmount;
        breakpointAmount = breakpointAmount < minimumBreakpoints ? minimumBreakpoints : breakpointAmount;

        // Now lets put a limit on the break points relative to the scale count 
        breakpoints = new Pair[breakpointAmount];

        //
        for (int i = 0; i < breakpoints.length; i++) {
            breakpoints[i] = new Pair((int) heightConstraint - (i * (heightConstraint/ breakpointAmount)), (i * standardDeviation));
        }

        //
        System.out.println("Breakpoints[" + breakpoints.length + "]: " + Arrays.toString(breakpoints));

        // Making the rectangles out of that.
        for (int i = 0; i < copy.length; i++) {

            // Keep the bars the same thickness and even with the width of the panel.
            float posw = (widthConstraint - 32) / copy.length;
            float posx = i * (posw);

            // Since the datum in copy has already been scaled down to respect the heightConstraint then we just need the value.
            float posy = copy[i]; //(int) (copy[i] * staticScaleCount) >= high ? copy[i]: copy[i];
            float posh = (heightConstraint) - posy;

            //
            shapeArr[i] = new Rectangle2D.Float(posx + 32, posh - 1, posw, posy - 1);
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
        for (int i = 0; i < copy.length; i++) {

            // Make actual shapes out of the that so we can keep our float precisions.
            final Point2D.Float point = pointArr[i];
            final Rectangle2D.Float rf = shapeArr[i];
            final Color color = colorArr[i];
            final Rectangle2D rsb = manet.getFontMetrics().getStringBounds(String.valueOf(data[i]), manet);
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
                final float diff = (float) Math.ceil((prev.y - point.y) * (staticScaleCount < 5 ? 1 : staticScaleCount));
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
                manet.drawString(String.valueOf(data[i]), rf.x + (rf.width / 2) - (int) (rsb.getWidth() / 2), rf.y - (int) (rsb.getHeight() / 3));
            }
        }

        // Setting up to draw the breakpoint lines.
        manet.setColor(Color.BLACK);
        manet.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .35f));

        // Going over them
        for (int i = 0; i < breakpoints.length; i++) {

            // Grab the boundaries for the text.
            final Rectangle2D bsb = manet.getFontMetrics().getStringBounds(String.valueOf(breakpoints[i]), monet);

            // Draw the line and the break.
            manet.drawLine(0, (int) breakpoints[i].y - 1, widthConstraint, (int) breakpoints[i].y - 1);
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
    
    private class Pair {
        
        int y;
        float value;
        
        Pair(int y, float value) {
            this.y = y;
            this.value = value;
        }
    }
}
