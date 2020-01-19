
/**
 * @author Jordan Ruckle, Jared Spaulding, William Cary, Shin Yamagami, and Miles Golding
 * Date: May 3, 2019
 *
 *
 * Generates the tiles as well as loads new positions into board
 */

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.nio.*;
import java.awt.geom.Line2D;

public class TileGenerator {

  private Board board_;

  public TileGenerator(Board b) {
    this.board_ = b;
  }

  /**
   * Initial setup of tiles This is where tiles will always be created DO NOT
   * create new instnces of tiles or will lose interactability
   */
  public void setupTiles() {
    for (int i = 0; i < 16; i++) {
      GameTile tile = new GameTile();

      tile.setId(i);
      board_.getAllTiles().add(tile);
      tile.addMouseListener(board_.getTileListener());
      if (i < 8) {
        board_.getLeftSide().add(tile);
      } else {
        board_.getRightSide().add(tile);
      }
    }

    for (int i = 0; i < 16; i++) {
      HomeTile tile = new HomeTile();
      tile.setId(i + 16);
      board_.getGrid().add(tile);
      board_.getAllTiles().add(tile);
      tile.addMouseListener(board_.getTileListener());
    }
  }

  /**
   * @param lines     all the lines for all the tiles
   * @param ids       all the ids for all GameTiles
   * @param rotations all the rotations for each tile fills a temp array to set
   *                  boards reset array with this temp array is in order of the
   *                  tile ids provided from loading a file
   */
  public void generate(ArrayList<Line2D[]> lines, int[] ids, int[] rotations) {
    ArrayList<GameTile> gms = board_.getGameTiles();
    ArrayList<HomeTile> hms = board_.getHomeTiles();
    ArrayList<Tile> temp = new ArrayList<Tile>();

    int k = 0;
    int l = 0;

    for (int i = 0; i < 32; i++) {
      boolean exists = false;
      for (int j = 0; j < ids.length; j++) {
        if (ids[j] == i) {
          GameTile t = gms.get(k);
          t.setId(ids[j]);
          t.setLines(getLines(lines, j));
          t.setRotation(rotations[j]);
          temp.add(t);
          k++;
          exists = true;
        }
      }
      if (!exists) {
        HomeTile t = hms.get(l);
        temp.add(t);
        l++;
      }
    }

    board_.setAllTiles(temp);

  }

  /**
   * randomizes the game board both rotations and IDs are randomized
   */
  public void randomize() {
    ArrayList<GameTile> gameTiles = board_.getGameTiles();
    ArrayList<HomeTile> homeTiles = board_.getHomeTiles();
    ArrayList<Tile> allTiles = new ArrayList<Tile>();

    // randomize rotations
    int zeros = 4, rest1 = 1, rest2 = 1, rest3 = 1;
    for (int i = 0; i < gameTiles.size(); i++) {
      if (zeros > 0) {
        gameTiles.get(i).setRotation(0);
        zeros--;
      } else if (rest1 > 0) {
        gameTiles.get(i).setRotation(1);
        rest1 = 0;
      } else if (rest2 > 0) {
        gameTiles.get(i).setRotation(2);
        rest2 = 0;
      } else if (rest3 > 0) {
        gameTiles.get(i).setRotation(3);
        rest3 = 0;
      } else {
        gameTiles.get(i).setRotation(new Random().nextInt(4 - 1) + 1);
      }
    }

    // randomize positions
    Collections.shuffle(gameTiles);
    allTiles.addAll(gameTiles);
    allTiles.addAll(homeTiles);

    board_.setAllTiles(allTiles);
  }

  /**
   * @param allLines Line2D array of all lines on entire game board
   * @param tilenum  the set of lines for the desired tile
   * @return all the lines that need drawn on a specific tile
   */
  private ArrayList<Line2D> getLines(ArrayList<Line2D[]> allLines, int tilenum) {
    ArrayList<Line2D> lines = new ArrayList<Line2D>();
    for (Line2D line : allLines.get(tilenum))
      lines.add(line);
    return lines;
  }

}