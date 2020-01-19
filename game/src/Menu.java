
/**
* @author Jordan Ruckle, Jared Spaulding, William Cary, Shin Yamagami, and Miles Golding
* Date: Feb 27, 2019
*
* Provides functionality to the three buttons at the top of the game.
*
*
*/

import javax.swing.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

public class Menu extends JPanel implements ActionListener {

  private static final String BTN_TITLE_L_ = "File", BTN_TITLE_R_ = "Reset", BTN_TITLE_M_ = "Quit",
      BTN_TITLE_LOAD = "Load", BTN_TITLE_SAVE = "Save";

  private static final String Q_SAVE = "Would you like to save changes made to the game board?", T_SAVE = "Save?",
      Q_REPLACE = "This file exists. Would you like to overwrite?", T_REPLACE = "Overwrite?";

  private static final Color COLOR_BACKGROUND_ = new Color(46, 65, 89);

  private JButton lButton_, rButton_, mButton_;
  private JPopupMenu fileMenu_;
  private JMenuItem btnLoad_, btnSave_;
  private Board board_;
  private JLabel clock_;
  private GridBagConstraints layout_ = new GridBagConstraints();
  private ClockThread ct_ = new ClockThread();

  /**
   * Constructor initialises control left, middle, and right control buttons. Adds
   * those buttons to the ActionListener. Sets Jpanel border color and background
   * color.
   * 
   * @param board The board that the menu will be drawn on and interact with.
   */
  public Menu(Board board) {
    this.setLayout(new GridBagLayout());
    this.setBorder(BorderFactory.createLineBorder(Color.black));
    this.setBackground(COLOR_BACKGROUND_);

    this.board_ = board;

    clock_ = new JLabel("00:00:00");
    clock_.setForeground(Color.WHITE);

    fileMenu_ = new JPopupMenu();

    btnLoad_ = new JMenuItem(BTN_TITLE_LOAD);
    btnSave_ = new JMenuItem(BTN_TITLE_SAVE);

    fileMenu_.add(btnLoad_);
    fileMenu_.add(btnSave_);

    lButton_ = new JButton(BTN_TITLE_L_);
    rButton_ = new JButton(BTN_TITLE_R_);
    mButton_ = new JButton(BTN_TITLE_M_);

    lButton_.addActionListener(this);
    rButton_.addActionListener(this);
    mButton_.addActionListener(this);
    btnLoad_.addActionListener(this);
    btnSave_.addActionListener(this);

    lButton_.setBackground(Color.WHITE);
    rButton_.setBackground(Color.WHITE);
    mButton_.setBackground(Color.WHITE);

    layout_.gridy = 0;
    layout_.gridwidth = 3;

    this.add(clock_, layout_);

    layout_.gridwidth = 1;
    layout_.gridy = 1;
    layout_.gridx = 0;
    this.add(lButton_, layout_);
    layout_.gridy = 1;
    layout_.gridx = 1;
    this.add(rButton_, layout_);
    layout_.gridy = 1;
    layout_.gridx = 2;
    this.add(mButton_, layout_);
  }

  /**
   * starts the clock timer
   */
  public void startClock() {
    long start = System.currentTimeMillis();
    ct_ = new ClockThread(start, clock_);
    ct_.start();
  }

  public void startClock(long s) {
    ct_.restart(s);
  }

  /**
   * restarts the clock
   */
  public void restartClock() {
    ct_.restart();
  }

  /**
   * @return this current time elapsed
   */
  public long getTime() {
    return ct_.getTime();
  }

  /**
   * @return this formatted time elapsed
   */
  public String getWinTime() {
    return clock_.getText();
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed(ActionEvent e) {
    if (BTN_TITLE_M_.equals(e.getActionCommand()))
      System.exit(0);
    if (BTN_TITLE_R_.equals(e.getActionCommand()))
      board_.reset();
    if (BTN_TITLE_L_.equals(e.getActionCommand())) {
      fileMenu_.show(lButton_, lButton_.getBounds().x, lButton_.getBounds().y + lButton_.getBounds().height);
    }
    if (BTN_TITLE_LOAD.equals(e.getActionCommand())) {
      load();
    }
    if (BTN_TITLE_SAVE.equals(e.getActionCommand())) {
      save();
    }
  }

  /**
   * If changes have been made to the board, will prompt to save those changes
   * Opens a file window and allows the user to select a file to load
   */
  public void load() {

    // prompt to save if unsaved changes exist
    if (board_.getHasChanged()) {
      int reply = JOptionPane.showConfirmDialog(null, Q_SAVE, T_SAVE, JOptionPane.YES_OPTION);
      if (reply == JOptionPane.YES_OPTION)
        save();
    }

    // load file
    JFileChooser fc = new JFileChooser("../input");
    int returnVal = fc.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      load(fc.getSelectedFile());
    }

  }

  /**
   * Loads the file chosen by calling the filemanager class
   * 
   * @param the file to load from
   */
  public void load(File file) {
    FileManager fm = new FileManager();
    try {
      fm.load(file);
      TileGenerator tg = new TileGenerator(board_);
      tg.generate(fm.getCoordinantes(), fm.getIds(), fm.getRotations());
      startClock(fm.getTime());
    } catch (OutOfMemoryError ex1) {
      JOptionPane.showMessageDialog(null, "Invalid File Format");
      load();
    } catch (IndexOutOfBoundsException ex2) {
      JOptionPane.showMessageDialog(null, "Invalid File Format");
      load();
    }
  }

  /**
   * Opens a file window and allows the user to create a file to save to. If user
   * selects a file to save over or writes the name of a file that exists, prompt
   * to ask if they wish to overwrite the file.
   */
  public void save() {
    JFileChooser fc = new JFileChooser("../input");
    ArrayList<GameTile> saveTiles = board_.getGameTiles();
    FileManager fm = new FileManager();
    int returnVal = fc.showSaveDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {

      // if file exists prompt to overwrite
      if (fc.getSelectedFile().exists()) {
        int reply = JOptionPane.showConfirmDialog(null, Q_REPLACE, T_REPLACE, JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        if (reply == JOptionPane.NO_OPTION)
          save();
        else {
          fm.save(fc.getSelectedFile().getName(), fc.getSelectedFile().getAbsolutePath(), saveTiles, getTime());
          load(fc.getSelectedFile());
        }
      } else {
        fm.save(fc.getSelectedFile().getName(), fc.getSelectedFile().getAbsolutePath(), saveTiles, getTime());
        load(fc.getSelectedFile());
      }

    }
  }
}
