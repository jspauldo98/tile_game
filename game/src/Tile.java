
/**
* @author Jordan Ruckle, Jared Spaulding, William Cary, Shin Yamagami, and Miles Golding
* Date: Feb 27, 2019
* 
* Tile superclass, contains details about the Tiles
*/

import java.awt.*;
import javax.swing.*;
import java.awt.geom.Line2D;
import java.util.*;

public class Tile extends JPanel {

   private static final Dimension SIZE_ = new Dimension(100, 100);
   private static final Color SELECT_COLOR_ = new Color(191, 7, 7);
   private int initRotate;
   private int rotate;
   private int id_ = -1;

   public Tile() {
      this.setPreferredSize(SIZE_);
   }

   /**
    * makes the border of the tile red to depict selection
    */
   public void select() {
      this.setBorder(BorderFactory.createLineBorder(SELECT_COLOR_, 5));
   }

   /**
    * removes the red border to depict deselect
    */
   public void deselect() {
      this.setBorder(null);
   }

   /**
    * sets the rotation of the tile
    */
   public void setRotation(int r) {
      rotate = r;
      initRotate = r;
      this.repaint();
   }

   /**
    * this temp array is in order of the tile ids provided from loading a file
    */
   public int getInitialRotation() {
      return initRotate;
   }

   /**
    * gets the rotation of the tile
    */
   public int getRotation() {
      return rotate;
   }

   /**
    * sets the ID of the tile
    */
   public void setId(int id) {
      id_ = id;
   }

   /**
    * gets the ID of the tile
    */
   public int getId() {
      return id_;
   }

   /**
    * @parameter g The tile to be rotated rotates the tile and
    */
   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      for (int i = 0; i < rotate; i++) {
         g2.rotate(Math.toRadians(90), 50, 50);
      }
   }

   /**
    * sets the rotation of the tile resets to 0 degrees in the case of reaching 360
    * degrees (int of 4)
    */
   public void rotateT() {
      if (rotate < 3) {
         rotate++;
      } else {
         rotate = 0;
      }

      this.repaint();
   }
}
