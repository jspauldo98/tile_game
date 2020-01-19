
/**
 * @author Jordan Ruckle, Jared Spaulding, William Cary, Shin Yamagami, and Miles Golding
 * Date: Mar 28, 2019
 *
 * This is the Tile Swapper class. It manages what happens when tiles are slected
 */

import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;

import javax.swing.JOptionPane;

public class WinCondition extends MouseAdapter {

	// Creates the array
	private File file_ = new File("input/default.mze");
	private Board board_;

	public WinCondition(Board board) {
		board_ = board;
	}

	// Checks the state of the current tiles against default.mze
	// If the lines on the tiles match, the game has been won!
	public void checkWinConditions(ArrayList<GameTile> gameTiles) {

		for (int i = 0; i < 16; i++) {
			if (gameTiles.get(i).getRotation() != 0) {
				return;
			} else if (gameTiles.get(i).getId() < 16) {
				return;
			}
		}

		int numberOfTiles;
		// arrays for specifications of each tile
		int[] numberOfLines;

		// each line has two pairs(four values) of floats with (x1, y1, x2, y2)
		// File object for a maze file

		// array for tiles with line info

		File MazeFile = file_;
		try {
			// for reading binaries
			byte[] bytes = new byte[4];

			InputStream input;

			try {
				// get an InputStream object
				input = new FileInputStream(MazeFile.getAbsolutePath());
			} catch (FileNotFoundException error) {
				System.out.println("Win Conditions: File Error");
				return;
			}

			// read the first line in binary
			input.read(bytes);
			// get the number of tiles by converting from 4 bytes in binary
			// to an integer
			numberOfTiles = ByteBuffer.wrap(bytes).getInt();

			// number of lines of each tile
			numberOfLines = new int[numberOfTiles];

			// array for information of lines of tiles
			ArrayList<Line2D> valuesOfCoordinates;

			for (int i = 0; i < numberOfTiles; i++) {
				// read a line to get the ID of a tile
				input.read(bytes);

				input.read(bytes);

				numberOfLines[i] = ByteBuffer.wrap(bytes).getInt();
				// numberOfLines[i] = numberOfLines;
				valuesOfCoordinates = new ArrayList<Line2D>();

				ArrayList<Line2D> currentGameTileLines = gameTiles.get(i).getLines();

				for (int j = 0; j < numberOfLines[i]; j++) {
					float[] coordinates = new float[4];

					for (int k = 0; k < 4; k++) {
						input.read(bytes);
						coordinates[k] = ByteBuffer.wrap(bytes).getFloat();
					}

					valuesOfCoordinates
							.add(new Line2D.Float(coordinates[0], coordinates[1], coordinates[2], coordinates[3]));
				}
				// Checks the line coordinates of default.mze tiles against current game tiles
				for (int j = 0; j < currentGameTileLines.size(); j++) {
					if (currentGameTileLines.get(j).getX1() != valuesOfCoordinates.get(j).getX1()
							|| currentGameTileLines.get(j).getY1() != valuesOfCoordinates.get(j).getY1()
							|| currentGameTileLines.get(j).getX2() != valuesOfCoordinates.get(j).getX2()
							|| currentGameTileLines.get(j).getY2() != valuesOfCoordinates.get(j).getY2()) {
						return;
					}
				}

			}
			input.close();

			JOptionPane.showMessageDialog(null, "You have won the game!\nTime: " + board_.getMenu().getWinTime());
			return;
		} catch (IOException ex1) {
			return;
		} catch (OutOfMemoryError ex2) {
			return;
		}
	}
}