
/**
* @author Jordan Ruckle, Jared Spaulding, William Cary, Shin Yamagami, and Miles Golding
* @since Feb 27, 2019
* 
* Tile with no lines cannot be selected
*/

import java.awt.*;
import javax.swing.*;

public class HomeTile extends Tile {

   private static final Color BG_COLOR = new Color(143, 142, 191);

   /**
    * Initializes the tile with black borders and a width of 1 and a light blue
    * background
    */
   public HomeTile() {
      super();
      this.setBackground(BG_COLOR);
      setBorder(BorderFactory.createLineBorder(Color.black, 1));

   }

   /**
    * deselects the tile by setting the border to black
    */
   @Override
   public void deselect() {
      setBorder(BorderFactory.createLineBorder(Color.black, 1));
   }
}