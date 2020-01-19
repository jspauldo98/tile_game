
/**
 * @author Jordan Ruckle, Jared Spaulding, William Cary, Shin Yamagami, and Miles Golding
 * Date: Mar 28, 2019
 *
 * This is the Tile Swapper class. It manages what happens when tiles are slected
 */

import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import java.awt.*;

public class TileSwapper extends MouseAdapter {

	private Tile t1_, t2_;
	private boolean clipBoardFull_ = true;
	private Board board_;
	private WinCondition winCondition_;

	public TileSwapper(Board b) {
		this.board_ = b;
		this.winCondition_ = new WinCondition(b);
	}

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 */
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (clipBoardFull_) {
				if ((Tile) e.getSource() instanceof GameTile) {
					t1_ = (Tile) e.getSource();
					t1_.select();
					clipBoardFull_ = false;
				}
			} else {
				if ((Tile) e.getSource() instanceof GameTile && (Tile) e.getSource() != t1_) {
					flashIllegalMove();
				} else if ((Tile) e.getSource() instanceof HomeTile) {
					t2_ = (Tile) e.getSource();
					t1_.deselect();
					swap();
					clipBoardFull_ = true;
					winCondition_.checkWinConditions(board_.getGameTiles());
				} else if ((Tile) e.getSource() == t1_) {
					t1_.deselect();
					clipBoardFull_ = true;
				}
			}
		}
		if (SwingUtilities.isRightMouseButton(e)) {
			Tile t = (Tile) e.getSource();
			t.rotateT();
			winCondition_.checkWinConditions(board_.getGameTiles());
		}

		board_.validate();
	}

	/**
	 * Swapping of Tiles occurs here. Searches through all the components to find
	 * index of t1 and t2 then remove and add respectively
	 */
	private void swap() {
		ArrayList<Component[]> components = new ArrayList<Component[]>();
		components.add(board_.getRightSide().getComponents());
		components.add(board_.getLeftSide().getComponents());
		components.add(board_.getGrid().getComponents());

		for (int i = 0; i < components.size(); i++) {
			int index1 = getIndex(components.get(i), t1_);
			int index2 = getIndex(components.get(i), t2_);
			if (index1 != -1) {
				switch (i) {
				case 0:
					board_.getRightSide().add(t2_, index1);
					break;
				case 1:
					board_.getLeftSide().add(t2_, index1);
					break;
				case 2:
					board_.getGrid().add(t2_, index1);
					break;
				}
			}
			board_.validate();
			if (index2 != -1) {
				switch (i) {
				case 0:
					board_.getRightSide().add(t1_, index2);
					break;
				case 1:
					board_.getLeftSide().add(t1_, index2);
					break;
				case 2:
					board_.getGrid().add(t1_, index2);
					break;
				}
			}
			board_.validate();
		}
		board_.validate();
		board_.updateChanges(true);
	}

	/**
	 * @param comps array of components to be searched
	 * @param c     component searching for
	 * @return index of c. -1 if not found
	 */
	private int getIndex(Component[] comps, Component c) {
		for (int i = 0; i < comps.length; i++) {
			if (c == comps[i]) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @param value bool to set clipboard to
	 */
	public void setClipBoard(boolean value) {
		clipBoardFull_ = value;
	}

	/**
	 * Invoked when an illegal move is made IE gametile on gametile move The tile
	 * background color will flash red for 1 second before returing to original
	 * color
	 */
	public void flashIllegalMove() {
		t1_.setBackground(new Color(220, 20, 60));
		Timer timer = new Timer();
		timer.schedule(new FlashTask(t1_), 1000);
	}
}