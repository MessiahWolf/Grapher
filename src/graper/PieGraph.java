/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author rcher
 */
public class PieGraph implements Graph {

    // -> Variable Declaration
    // Java Native Classes
    private final ArrayList<Arc2D.Float> arcList;
    private final ArrayList<Color> colorList;
    // Data Types
    private float[] copy;
    private float[] data;
    // Integers
    private double b;
    private int widthConstraint;
    private int heightConstraint;
    // Rand. Generator

                // Generate a new color every time
    private final Random generator = new Random();
    // <- End of Variable Declaration

    public PieGraph() {
        arcList = new ArrayList();
        colorList = new ArrayList();
    }

    @Override
    public void init(float[] data) {
        this.data = data;
    }

    @Override
    public void resize(int widthConstraint, int heightConstraint) {

        // Clear on every resize event.
        arcList.clear();

        // Keep these for future reference in the paint call.
        this.widthConstraint = widthConstraint;
        this.heightConstraint = heightConstraint < 360 ? 360 : heightConstraint;
        final Point center = new Point(widthConstraint / 2, heightConstraint / 2);

        // We're going to need a modified version of the data to fit within the panel, but still retain our original datum.
        copy = Arrays.copyOf(data, data.length);
        final float[] percents = new float[copy.length];

        // Values for keeping the data in respect to the heightConstraint.
        final int count = data.length;
        int angle = 360;
        float total = 0;

        // Get a total from the data to later make percents.
        for (float f : data) {
            total += f;
        }

        // Make our percent as a ratio of the total from data.
        for (int i = 0; i < count; i++) {
            percents[i] = data[i] / total;
        }

        // It's time to create our arcs by getting the percent that these values are of a complete circle.
        for (int i = 0; i < percents.length; i++) {

            // Our modified value is what each values percent is of 360.
            copy[i] = 360.0f * percents[i];

            // Create arcs from those values
            final Arc2D.Float arc = new Arc2D.Float();
            arc.setArc(center.x - this.widthConstraint / 4, center.y - this.heightConstraint / 4, this.widthConstraint / 2, this.heightConstraint / 2, angle - copy[i], copy[i], Arc2D.PIE);

            //  Subtract from the toal
            angle -= Math.round(copy[i]);

            // And add to the list.
            colorList.add(new Color((int) (generator.nextDouble() * 255), (int) (generator.nextDouble() * 255), (int) (generator.nextDouble() * 255)));
            arcList.add(arc);
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
        for (int i = arcList.size() - 1; i >= 0; i--) {

            // Grab that arc.
            final Arc2D.Float arc = arcList.get(i);
            final boolean inside = arc.contains(position);

            // When the mouse is inside the arc.
            if (inside) {

                // Generate a random color and draw the arc
                manet.setColor(colorList.get(i));
                manet.fill(arc);
            }

            // Setup to draw the arc.
            manet.setColor(Color.BLACK);
            manet.draw(arc);

            Rectangle2D r = manet.getFontMetrics().getStringBounds(String.valueOf(data[i]), monet);

            //
            if (inside) {
                manet.drawString(String.valueOf(data[i]), (int) arc.getStartPoint().getX() + (int) (arc.start <= 90 || arc.start >= 270 ? Math.abs(r.getWidth() / 2 + 24) : -Math.abs(r.getWidth() / 2 + 24)), (int) (arc.getStartPoint().getY() + (arc.start > 180 ? r.getHeight() * 2.6 : -r.getHeight())));
            }
        }

        // Return the rendering hints
        manet.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
}