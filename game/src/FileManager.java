
/**
* @author Jordan Ruckle, Jared Spaulding, William Cary, Shin Yamagami, and Miles Golding
* @since Feb 27, 2019
*
* This a a file manager class. It will decide default, load, and save files
*/

import java.util.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.awt.geom.Line2D;
import javax.swing.JOptionPane;

public class FileManager {

	private ArrayList<Line2D[]> TileList = new ArrayList<Line2D[]>();
	private int[] IDs;
	private int[] rotations;
	private long time = 0;

	/**
	 * Creates a FileManager Object and initializes the rotations and the IDs to
	 * ints
	 */
	public FileManager() {
		rotations = new int[16];
		IDs = new int[16];
	}

	/**
	 * Loads the file and checks the first 4 bytes to test whether they are cafedeed
	 * or cafebeef
	 * 
	 * @param file the file to be loaded to the game
	 */
	public void load(File file) {

		TileList.clear();

		try {
			InputStream input = new FileInputStream(file);
			byte[] bytes = new byte[4];

			// get hex value first
			input.read(bytes);
			String hex = Integer.toHexString(ByteBuffer.wrap(bytes).getInt());

			// played
			if (hex.equals("cafedeed")) {
				// get number of tiles
				input.read(bytes);
				int numberOfTiles = ByteBuffer.wrap(bytes).getInt();

				// get time
				byte[] newBytes = new byte[8];
				input.read(newBytes);
				time = ByteBuffer.wrap(newBytes).getLong();

				IDs = new int[numberOfTiles];
				rotations = new int[numberOfTiles];

				for (int i = 0; i < numberOfTiles; i++) {
					// get tile id
					input.read(bytes);
					int tileId = ByteBuffer.wrap(bytes).getInt();
					IDs[i] = tileId;

					// get rotation
					input.read(bytes);
					int rotation = ByteBuffer.wrap(bytes).getInt();
					rotations[i] = rotation;

					// get number of lines on tile
					input.read(bytes);
					int numLines = ByteBuffer.wrap(bytes).getInt();

					Line2D[] valuesOfCoordinates = new Line2D[numLines];

					for (int k = 0; k < numLines; k++) {

						float[] coordinates = new float[4];

						// get line coords (x0, y0, x1, y1)
						for (int j = 0; j < 4; j++) {
							input.read(bytes);
							coordinates[j] = ByteBuffer.wrap(bytes).getFloat();
						}

						valuesOfCoordinates[k] = new Line2D.Float(coordinates[0], coordinates[1],
							 coordinates[2],
								coordinates[3]);

					}
					TileList.add(valuesOfCoordinates);

				}
			} else {
				defaultLoad(file);
			}

		} catch (Exception e) {

		}
		/*
		 * check if played file using first four bytes next is the number of tiles BUT
		 * this time ex. 31 for each tile tile id number 0-31 tile rotation 0-3 number
		 * of lines on the Tile list the four floats for each line x0, y0, x1, y1
		 **/
	}

	/**
	 * Saves the game to a file as a series of bytes in the specified format
	 * 
	 * @param filename The name the file will be saved as
	 * @param path     the path to the directory where the file will be saved
	 * @param the      tileArray List where all the tiles are kept.
	 * @param t        the number of seconds the game has been running for
	 */
	public void save(String filename, String path, ArrayList<GameTile> listTiles, long t) {
		/*
		 * make first four bytes say that its been played number of tiles 31 for each
		 * tile tile id number 0-31 tile rotation 0-3 number of lines on the Tile list
		 * the four floats for each line x0, y0, x1, y1
		 **/
		try {
			FileOutputStream fileWriter = new FileOutputStream(new File(path));
			byte[] bytes = new byte[4];
			int[] ints = new int[4];

			// played or not
			ints[0] = 0xca;
			ints[1] = 0xfe;
			ints[2] = 0xde;
			ints[3] = 0xed;
			for (int i = 0; i < 4; i++) {
				fileWriter.write(ints[i]);
			}

			// number of tiles
			fileWriter.write(convertToByteArray(16));

			// time
			fileWriter.write(convertToByteArray(t));

			for (int i = 0; i < 16; i++) {

				// get ids
				int tileID = (listTiles.get(i).getId());
				fileWriter.write(convertToByteArray(tileID));

				// get rotation
				int tileRotation = (listTiles.get(i).getRotation());
				fileWriter.write(convertToByteArray(tileRotation));

				// get lines
				ArrayList<Line2D> lines = listTiles.get(i).getLines();
				int numberOfLines = lines.size();
				fileWriter.write(convertToByteArray(numberOfLines));

				for (int j = 0; j < numberOfLines; j++) {
					GameTile tempTile = listTiles.get(i);
					fileWriter.write(convertToByteArray((float) lines.get(j).getX1()));
					fileWriter.write(convertToByteArray((float) lines.get(j).getY1()));
					fileWriter.write(convertToByteArray((float) lines.get(j).getX2()));
					fileWriter.write(convertToByteArray((float) lines.get(j).getY2()));
				}
			}

			fileWriter.close();
		} catch (Exception e) {
			System.out.println("save");
			return;
		}
	}

	/**
	 * The default load that is called if the file has not been played. Loads the
	 * file into readable information.
	 * 
	 * @param defaultFile the file to be loaded
	 * 
	 */
	public int defaultLoad(File defaultFile) {

		TileList.clear();

		int numberOfTiles;
		// arrays for specifications of each tile
		int[] numberOfLines;

		// each line has two pairs(four values) of floats with (x1, y1, x2, y2)
		// File object for a maze file

		// array for tiles with line info

		File MazeFile = defaultFile;
		try {
			// for reading binaries
			byte[] bytes = new byte[4];

			InputStream input;

			try {
				// get an InputStream object
				input = new FileInputStream(MazeFile.getAbsolutePath());
			} catch (FileNotFoundException error) {
				return -1;
			}

			// read the first line in binary
			input.read(bytes);
			// get the number of tiles by converting from 4 bytes in binary
			// to an integer
			numberOfTiles = ByteBuffer.wrap(bytes).getInt();

			// ID of each tile
			IDs = new int[numberOfTiles];
			// number of lines of each tile
			numberOfLines = new int[numberOfTiles];

			// array for information of lines of tiles
			Line2D[] valuesOfCoordinates;

			for (int i = 0; i < numberOfTiles; i++) {
				// read a line to get the ID of a tile
				input.read(bytes);
				IDs[i] = ByteBuffer.wrap(bytes).getInt();

				input.read(bytes);

				numberOfLines[i] = ByteBuffer.wrap(bytes).getInt();
				// numberOfLines[i] = numberOfLines;
				valuesOfCoordinates = new Line2D[numberOfLines[i]];

				for (int j = 0; j < numberOfLines[i]; j++) {
					float[] coordinates = new float[4];

					for (int k = 0; k < 4; k++) {
						input.read(bytes);
						coordinates[k] = ByteBuffer.wrap(bytes).getFloat();
					}

					valuesOfCoordinates[j] = new Line2D.Float(coordinates[0], coordinates[1], coordinates[2],
							coordinates[3]);
				}

				TileList.add(valuesOfCoordinates);
			}
			input.close();
			return 0;
		} catch (IOException ex1) {
			return -1;
		} catch (OutOfMemoryError ex2) {
			return -2;
		}
	}

	/**
	 * gets the coordinates of the of all of the lines.
	 */
	public ArrayList<Line2D> getCoordinantes(int tileNum) {
		ArrayList<Line2D> lines = new ArrayList<Line2D>();
		for (Line2D line : TileList.get(tileNum)) {
			lines.add(line);
		}

		return lines;
	}

	/**
	 * Each element in the arrayList represents a tile and its set of lines, and
	 * each element in the array of Line2D represents a line on tha tile.
	 * 
	 * @return An arrayList of Line2D lines
	 */
	public ArrayList<Line2D[]> getCoordinantes() {
		return TileList;
	}

	/**
	 * @return the array of tile IDs
	 */
	public int[] getIds() {
		return IDs;
	}

	/**
	 * @return returns the array of tile rotations
	 */
	public int[] getRotations() {
		return rotations;
	}

	/**
	 * @return returns the seconds of the loaded file
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param value the value of to be returned as a byte array
	 * @return the byte array of the paramater
	 */
	public static byte[] convertToByteArray(int value) {
		byte[] bytes = new byte[4];
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.putInt(value);
		return buffer.array();
	}

	/**
	 * @param value the float to be returned as a byte array
	 * @return the byte array created from the float value passed
	 */
	public static byte[] convertToByteArray(float value) {
		byte[] bytes = new byte[4];
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.putFloat(value);
		return buffer.array();
	}

	/**
	 * @param value the long to be returned as a byte array
	 * @return the byte array created from the float value passed
	 */
	public static byte[] convertToByteArray(long value) {
		byte[] bytes = new byte[8];
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.putLong(value);
		return buffer.array();
	}
}