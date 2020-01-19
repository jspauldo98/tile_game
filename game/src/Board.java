
/**
 * @author Jordan Ruckle, Jared Spaulding, William Cary, Shin Yamagami, and Miles Golding
 * Date: Feb 27, 2019
 *
 * This is the actual "game". Will have to make some major changes.
 */

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.MouseAdapter;

public class Board extends JFrame {
  private static final Color COLOR_BACKGROUND_ = new Color(46, 65, 89);
  private static final Dimension WINDOW_SIZE_ = new Dimension(900, 1000);
  private static final Insets TILE_PADDING_ = new Insets(10, 10, 10, 10);
  private static final Insets MENU_PADDING_ = new Insets(5, 5, 5, 5);

  private GridBagConstraints layout_ = new GridBagConstraints();

  private TileSwapper tileSwapper_ = new TileSwapper(this);
  private Menu menu_ = new Menu(this);
  private JPanel rightSide = new JPanel();
  private JPanel leftSide = new JPanel();
  private JPanel grid_ = new JPanel();
  private FileManager file_ = new FileManager();
  private TileGenerator tg_;
  private boolean hasChanged = false;

  private ArrayList<Tile> allTiles = new ArrayList<Tile>();
  private ArrayList<GameTile> gameTiles = new ArrayList<GameTile>();

  /**
   * because it is a serializable object, need this or javac complains <b>a
   * lot</b>
   */
  public static final long serialVersionUID = 1;

  /**
   * Constructor sets the window name using super(), changes the layout, sets the
   * window size and close operation. Loads default file
   *
   * @param s
   */
  public Board(String s) {
    super(s);
    this.setLayout(new GridBagLayout());
    this.setSize(WINDOW_SIZE_);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    tg_ = new TileGenerator(this);
    tg_.setupTiles();

    File currentDirectory = new File(".");
    File parentDirectory = currentDirectory.getParentFile();
    File file = new File(parentDirectory, "input/default.mze");
    int result = file_.defaultLoad(file);
    if (result == -1) {
      File file2 = new File(parentDirectory, "../input/default.mze");
      result = file_.defaultLoad(file2);
      if (result == 0) {
        tg_.generate(file_.getCoordinantes(), file_.getIds(), file_.getRotations());
        tg_.randomize();
        menu_.startClock();
      } else if (result == -1) {
        JOptionPane.showMessageDialog(null, "File Not Found");
        menu_.load();
      } else if (result == -2) {
        JOptionPane.showMessageDialog(null, "Invalid file format");
        menu_.load();
      }

    } else if (result == -2) {
      JOptionPane.showMessageDialog(null, "Invalid file format");
      menu_.load();
    } else {
      tg_.generate(file_.getCoordinantes(), file_.getIds(), file_.getRotations());
      tg_.randomize();
      menu_.startClock();
    }
  }

  /**
   * Establishes the initial board components
   */
  public void setup() {
    this.getContentPane().setBackground(COLOR_BACKGROUND_);

    setupMenu();
    setupLeftPanel();
    setupRightPanel();
    setupGrid();

    this.setVisible(true);

    return;
  }

  /**
   * Establishes the Menu with proper alignment
   */
  private void setupMenu() {
    layout_.gridwidth = 2;
    layout_.gridheight = 1;
    layout_.weightx = 1;
    layout_.weighty = 1;
    layout_.gridx = 1;
    layout_.gridy = 0;
    layout_.anchor = GridBagConstraints.PAGE_START;
    layout_.insets = MENU_PADDING_;
    this.add(menu_, layout_);
  }

  /**
   * Establishes the Grid with proper alignment
   */
  private void setupGrid() {
    layout_.anchor = GridBagConstraints.CENTER;
    layout_.gridy = 0;
    grid_.setLayout(new GridLayout(4, 4, 0, 0));
    grid_.setBackground(COLOR_BACKGROUND_);
    this.add(grid_, layout_);
  }

  /**
   * Establishes the right Panel of Tiles with proper alignment
   */
  private void setupRightPanel() {
    rightSide.setLayout(new GridLayout(8, 1, 10, 2));
    rightSide.setBackground(COLOR_BACKGROUND_);
    layout_.anchor = GridBagConstraints.LINE_END;
    layout_.insets = TILE_PADDING_;
    this.add(rightSide, layout_);
  }

  /**
   * Establishes the left Panel of Tiles with proper alignment
   */
  private void setupLeftPanel() {
    leftSide.setLayout(new GridLayout(8, 1, 10, 2));
    leftSide.setBackground(COLOR_BACKGROUND_);
    layout_.gridheight = 10;
    layout_.gridwidth = 10;
    layout_.anchor = GridBagConstraints.LINE_START;
    layout_.insets = TILE_PADDING_;
    this.add(leftSide, layout_);
  }

  /**
   * Clears the board clears leftside, rightside, and grid
   */
  public void clear() {
    leftSide.removeAll();
    rightSide.removeAll();
    grid_.removeAll();
  }

  /**
   * Removes all tiles from the board and replaces with the current tiles array in
   * order from leftside to rightside to grid area in accordance with array
   * indices
   */
  public void reset() {
    clear();
    menu_.restartClock();

    this.validate();

    for (int i = 0; i < 32; i++) {
      if (i < 8) {
        // fill left panel
        leftSide.add(allTiles.get(i));

      } else if (i > 7 && i < 16) {
        // fill right panel
        rightSide.add(allTiles.get(i));

      } else if (i > 15) {
        // fill grid area
        grid_.add(allTiles.get(i));
      }
      allTiles.get(i).setRotation(allTiles.get(i).getInitialRotation());
    }

    this.validate();
    hasChanged = false;
  }

  /**
   * @return the listener
   */
  public MouseAdapter getTileListener() {
    return tileSwapper_;
  }

  /**
   * @return right side JPanel
   */
  public JPanel getRightSide() {
    return rightSide;
  }

  /**
   * @return left side JPanel
   */
  public JPanel getLeftSide() {
    return leftSide;
  }

  /**
   * @return grid JPanel
   */
  public JPanel getGrid() {
    return grid_;
  }

  /**
   * @return menu Menu
   */
  public Menu getMenu() {
    return menu_;
  }

  /**
   * @return current tiles array
   */
  public ArrayList<Tile> getAllTiles() {
    return allTiles;
  }

  /**
   * sets current tiles and resets board
   * 
   * @param newTiles
   */
  public void setAllTiles(ArrayList<Tile> newTiles) {
    allTiles.clear();

    allTiles = newTiles;

    reset();
  }

  /**
   * @return all tiles of type HomeTile
   */
  public ArrayList<HomeTile> getHomeTiles() {
    ArrayList<HomeTile> tiles = new ArrayList<HomeTile>();

    for (int i = 0; i < leftSide.getComponents().length; i++) {
      if ((Tile) leftSide.getComponent(i) instanceof HomeTile) {
        HomeTile tile = (HomeTile) leftSide.getComponent(i);
        // tile.setId(i);
        tiles.add(tile);
      }
    }

    for (int i = 0; i < rightSide.getComponents().length; i++) {
      if ((Tile) rightSide.getComponent(i) instanceof HomeTile) {
        HomeTile tile = (HomeTile) rightSide.getComponent(i);
        // tile.setId(i + 8);
        tiles.add(tile);
      }
    }

    for (int i = 0; i < grid_.getComponents().length; i++) {
      if ((Tile) grid_.getComponent(i) instanceof HomeTile) {
        HomeTile tile = (HomeTile) grid_.getComponent(i);
        // tile.setId(i + 16);
        tiles.add(tile);
      }
    }

    return tiles;
  }

  /**
   * @return all tiles of type GameTile
   */
  public ArrayList<GameTile> getGameTiles() {
    gameTiles.clear();

    for (int i = 0; i < leftSide.getComponents().length; i++) {
      if ((Tile) leftSide.getComponent(i) instanceof GameTile) {
        GameTile tile = (GameTile) leftSide.getComponent(i);
        tile.setId(i);
        gameTiles.add(tile);
      }
    }

    for (int i = 0; i < rightSide.getComponents().length; i++) {
      if ((Tile) rightSide.getComponent(i) instanceof GameTile) {
        GameTile tile = (GameTile) rightSide.getComponent(i);
        tile.setId(i + 8);
        gameTiles.add(tile);
      }
    }

    for (int i = 0; i < grid_.getComponents().length; i++) {
      if ((Tile) grid_.getComponent(i) instanceof GameTile) {
        GameTile tile = (GameTile) grid_.getComponent(i);
        tile.setId(i + 16);
        gameTiles.add(tile);
      }
    }

    return gameTiles;
  }

  /**
   * Updates whether the game has changed
   * 
   * @param change
   */
  public void updateChanges(boolean change) {
    hasChanged = change;
  }

  /**
   * @return if the board has unsaved changes
   */
  public boolean getHasChanged() {
    return hasChanged;
  }
}