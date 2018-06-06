/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graper;

import java.awt.Point;

/**
 *
 * @author rcher
 */
public interface Graph {

    abstract void init(float[] data);

    abstract void resize(int widthConstraint, int heightConstraint);

    abstract void paint(java.awt.Graphics monet, Point mouse);
}
