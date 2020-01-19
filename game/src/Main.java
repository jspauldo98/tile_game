/**
 * @author Jordan Ruckle, Jared Spaulding, William Cary, Shin Yamagami, and Miles Golding
 * Date: Feb 27, 2019
 *
 *
 * Sets up the game and launches it
 */

//package game;
import java.io.File;

import javax.swing.*;

public class Main {
  private static final String GROUP_NAME_ = "Group Afa Maze";
  private static Board board_;

  public static void main(String[] args) {
    board_ = new Board(GROUP_NAME_);
    board_.setup();

    try {
      // The 4 that are installed on Linux here
      // May have to test on Windows boxes to see what is there.
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
      // This is the "Java" or CrossPlatform version and the default
      // UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
      // Linux only.
      //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
      // really old style Motif 
      //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
    } 
    catch (UnsupportedLookAndFeelException e) {
     // handle possible exception crash and burn
     JOptionPane.showMessageDialog(null, "UnsupportedLookAndFeelException");
    }
    catch (ClassNotFoundException e) {
     // handle possible exception
     JOptionPane.showMessageDialog(null, "ClassNotFoundException");
    }
    catch (InstantiationException e) {
     // handle possible exception
     JOptionPane.showMessageDialog(null, "InstantiationException");
    }
    catch (IllegalAccessException e) {
     // handle possible exception
     JOptionPane.showMessageDialog(null, "IllegalAccessException");
    }
  }

};