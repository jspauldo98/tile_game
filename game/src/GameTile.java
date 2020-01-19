
/**
* @author Jordan Ruckle, Jared Spaulding, William Cary, Shin Yamagami, and Miles Golding
* Date: Feb 27, 2019
* 
* Tile that will have maze segments
*/

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.geom.Line2D;

public class GameTile extends Tile {

   private static final Color BG_COLOR = new Color(71, 64, 115);
   private ArrayList<Line2D> lines_ = new ArrayList<Line2D>();

   /**
    * Creates a Gametile and set the background color to a dark blue.
    */
   public GameTile() {
      super();
      this.setBackground(BG_COLOR);
   }

   /**
    * @param lines paints the lines of game tile
    */
   public void setLines(ArrayList<Line2D> lines) {
      lines_ = lines;
      this.repaint();
   }

   /**
    * Paints the components on the tile
    * 
    * @param g The graphics object to be painted onto the tile
    */
   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      g2.setStroke(new BasicStroke(3));
      g2.setPaint(Color.WHITE);
      for (Line2D line : lines_) {
         g2.drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());
      }
   }

   /**
    * @return lines drawn to tile
    */
   public ArrayList<Line2D> getLines() {
      return lines_;
   }

}
