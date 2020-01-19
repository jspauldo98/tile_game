
/**
 * @author Jordan Ruckle, Jared Spaulding, William Cary, Shin Yamagami, and Miles Golding
 * @since May 1, 2019
 *
 * This class controls the tile flash when an illegal move is made
 */

import java.util.TimerTask;
import java.awt.Color;

public class FlashTask extends TimerTask {

	Tile tile_;

	/**
	 * @param the tile to add this class too
	 */
	public FlashTask(Tile t) {
		this.tile_ = t;
	}

	/**
	 * Runs the flashing task on the tile. Changing the color of the tile to red
	 */
	public void run() {
		tile_.setBackground(new Color(71, 64, 115));
		this.cancel();
	}
}