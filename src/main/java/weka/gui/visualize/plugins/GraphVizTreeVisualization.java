/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * GraphVizTreeVisualization.java
 * Copyright (C) 2014-2018 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.visualize.plugins;

import weka.core.Utils;

import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Class for generating GraphViz graphs for trees.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class GraphVizTreeVisualization {

  /** the name of the props file to load. */
  public final static String PROPERTIES_FILE = "weka/gui/visualize/plugins/GraphVizTreeVisualization.props";

  /** the properties object. */
  protected Properties PROPERTIES;

  /** whether to output some debugging information. */
  protected boolean DEBUG = false;

  /** the singleton instance. */
  protected static GraphVizTreeVisualization m_Singleton;

  /**
   * To avoid instantiation of singleton.
   */
  protected GraphVizTreeVisualization() {
    super();
  }

  /**
   * Returns the configuration, loads it if required.
   *
   * @return		the configuration
   */
  protected synchronized Properties getProperties() {
    if (PROPERTIES == null) {
      try {
	PROPERTIES = Utils.readProperties(PROPERTIES_FILE, getClass().getClassLoader());
      }
      catch (Exception e) {
	PROPERTIES = new Properties();
	System.err.println("Failed to load properties: " + PROPERTIES_FILE);
	e.printStackTrace();
      }
      DEBUG = PROPERTIES.getProperty("Debug", "false").equalsIgnoreCase("true");
    }

    return PROPERTIES;
  }

  /**
   * Returns the graphviz executable for turning a dotty notation string
   * into an image. 
   *
   * @return		the executable
   */
  public String getExecutable() {
    return getProperties().getProperty("Executable", "dot");
  }

  /**
   * Returns the additional options for the executable.
   *
   * @return		the additional options, if any
   */
  public String getAdditionalOptions() {
    return getProperties().getProperty("AdditionalOptions", "");
  }

  /**
   * Returns the extension to use for the image file.
   *
   * @return		the extension for the image (not dot)
   */
  public String getImageExtension() {
    return getProperties().getProperty("ImageExtension", "png");
  }

  /**
   * Returns whether temp files get removed.
   *
   * @return		true if to remove
   */
  public boolean cleanUpTempFiles() {
    return getProperties().getProperty("CleanUpTempFiles", "true").equalsIgnoreCase("true");
  }

  /**
   * Returns the width for the dialog.
   *
   * @return		the width
   */
  public int getDialogWidth() {
    String 	value;
    int		defValue;

    defValue = 800;
    value    = getProperties().getProperty("DialogWidth", "" + defValue);
    try {
      return Integer.parseInt(value);
    }
    catch (Exception e) {
      System.err.println("Failed to parse dialog width: " + value);
      e.printStackTrace();
      return defValue;
    }
  }

  /**
   * Returns the height for the dialog.
   *
   * @return		the height
   */
  public int getDialogHeight() {
    String 	value;
    int		defValue;

    defValue = 600;
    value    = getProperties().getProperty("DialogHeight", "" + defValue);
    try {
      return Integer.parseInt(value);
    }
    catch (Exception e) {
      System.err.println("Failed to parse dialog height: " + value);
      e.printStackTrace();
      return defValue;
    }
  }

  /**
   * Deletes the specified file.
   *
   * @param filename	the file to delete
   * @return		true if successfully deleted (or not present), false otherwise
   */
  protected boolean deleteFile(String filename) {
    boolean	result;
    File	file;

    result = true;
    file   = new File(filename);
    if (file.exists()) {
      if (DEBUG)
	System.out.println("Deleting: " + filename);
      file.delete();
      result = !file.exists();
      if (DEBUG) {
	if (result)
	  System.out.println("Successfully deleted: " + filename);
	else
	  System.out.println("Failed to delete: " + filename);
      }
    }

    return result;
  }

  /**
   * Saves the dotty string to a file.
   *
   * @param dotty	the graph in dotty notation to turn into image
   * @param filename 	the file to save the dotty data to
   * @return		null if successful, otherwise error message
   */
  public String saveDotty(String dotty, String filename) {
    String		result;
    FileWriter		fwriter;
    BufferedWriter	bwriter;

    result = null;

    if (DEBUG)
      System.out.println("Saving dotty to: " + filename);

    fwriter = null;
    bwriter = null;
    try {
      fwriter = new FileWriter(filename);
      bwriter = new BufferedWriter(fwriter);
      bwriter.write(dotty);
      bwriter.newLine();
    }
    catch (Exception e) {
      result = "Failed to write dotty string to " + filename;
      System.err.println(result);
      e.printStackTrace();
      result += "\n" + e;
    }
    finally {
      if (bwriter != null) {
	try {
	  bwriter.flush();
	  bwriter.close();
	}
	catch (Exception e){
	  // ignored
	}
      }
      if (fwriter != null) {
	try {
	  fwriter.flush();
	  fwriter.close();
	}
	catch (Exception e){
	  // ignored
	}
      }
    }

    return result;
  }

  /**
   * Turns the dotty file into an image.
   *
   * @param dottyFilename	the file with the graph in dotty notation to turn into image
   * @param imageFilename 	the name of the image to generate
   * @return			null if successful, otherwise error message
   */
  public String generateGraph(String dottyFilename, String imageFilename) {
    return generateGraph(dottyFilename, null, imageFilename);
  }

  /**
   * Turns the dotty file into an image.
   *
   * @param dottyFilename	the file with the graph in dotty notation to turn into image
   * @param format 		the format to use, null to ignore
   * @param imageFilename 	the name of the image to generate
   * @return			null if successful, otherwise error message
   */
  public String generateGraph(String dottyFilename, String format, String imageFilename) {
    String		result;
    String		additional;
    List<String>	cmd;
    ProcessBuilder	pb;
    Process		proc;
    int			retVal;

    result = null;

    // assemble command
    cmd = new ArrayList<String>();
    cmd.add(getExecutable());
    cmd.add("-o");
    cmd.add(imageFilename);
    if (format != null) {
      cmd.add("-T");
      cmd.add(format);
    }
    additional = getAdditionalOptions();
    if (!additional.trim().isEmpty())
      cmd.addAll(Arrays.asList(additional.trim().split(" ")));
    cmd.add(dottyFilename);

    if (DEBUG)
      System.out.println("Executing:\n" + cmd);

    // execute command
    pb = new ProcessBuilder(cmd);
    try {
      proc   = pb.start();
      retVal = proc.waitFor();
      if (retVal != 0)
	throw new IOException("GraphViz returned " + retVal);
    }
    catch (Exception e) {
      result = "Failed to execute graphviz command: " + cmd;
      System.err.println(result);
      e.printStackTrace();
      result += "\n" + e;
    }

    return result;
  }

  /**
   * Loads the specified image file.
   *
   * @param imageFilename	the image to load
   * @return			the generated image, null if failed to load
   */
  public BufferedImage loadImage(String imageFilename) {
    ImageIcon		icon;
    BufferedImage 	result;
    Graphics2D 		g2d;

    if (DEBUG)
      System.out.println("Loading graph image: " + imageFilename);

    icon   = new ImageIcon(imageFilename);
    result = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    g2d    = (Graphics2D) result.getGraphics();
    icon.paintIcon(null, g2d, 0, 0);
    g2d.dispose();

    return result;
  }

  /**
   * Turns the dotty graph into an image and saves it under the specified filename.
   *
   * @param dotty		the graph in dotty notation to turn into image
   * @param imageFilename 	the output filename for the image
   * @return			null if successful, otherwise error message
   */
  public String saveImage(String dotty, String imageFilename) {
    String 	result;
    String	prefix;
    String	dottyFilename;
    int		rand;

    rand          = (int) (Math.random() * 1000);
    prefix        = System.getProperty("java.io.tmpdir") + File.separator + "gvtv" + Integer.toHexString(rand);
    dottyFilename = prefix + ".dot";

    // save dotty file
    result = saveDotty(dotty, dottyFilename);
    if (result != null)
      return result;

    // generate image
    result = generateGraph(dottyFilename, imageFilename);
    if (result != null) {
      if (cleanUpTempFiles())
	deleteFile(dottyFilename);
      return result;
    }

    if (cleanUpTempFiles())
      deleteFile(dottyFilename);

    return null;
  }

  /**
   * Turns the dotty graph into an image and saves it under the specified filename.
   *
   * @param dotty		the graph in dotty notation to turn into image
   * @param format		the format to use
   * @param filename 		the output filename for the image
   * @return			null if successful, otherwise error message
   */
  public String export(String dotty, String format, String filename) {
    String 	result;
    String	prefix;
    String	dottyFilename;
    int		rand;

    rand          = (int) (Math.random() * 1000);
    prefix        = System.getProperty("java.io.tmpdir") + File.separator + "gvtv" + Integer.toHexString(rand);
    dottyFilename = prefix + ".dot";

    // save dotty file
    result = saveDotty(dotty, dottyFilename);
    if (result != null)
      return result;

    // generate output
    result = generateGraph(dottyFilename, format, filename);
    if (result != null) {
      if (cleanUpTempFiles())
	deleteFile(dottyFilename);
      return result;
    }

    if (cleanUpTempFiles())
      deleteFile(dottyFilename);

    return null;
  }

  /**
   * Turns the dotty string into a {@link BufferedImage}.
   *
   * @param dotty	the graph in dotty notation to turn into image
   * @return		the generated image, null if failed to convert
   */
  public BufferedImage toBufferedImage(String dotty) {
    String		prefix;
    String		dottyFilename;
    String 		imageFilename;
    String		msg;
    int			rand;
    BufferedImage 	result;

    rand          = (int) (Math.random() * 1000);
    prefix        = System.getProperty("java.io.tmpdir") + File.separator + "gvtv" + Integer.toHexString(rand);
    dottyFilename = prefix + ".dot";
    imageFilename = prefix + "." + getImageExtension();

    // save dotty file
    msg = saveDotty(dotty, dottyFilename);
    if (msg != null)
      return null;

    // generate image
    msg = generateGraph(dottyFilename, imageFilename);
    if (msg != null) {
      if (cleanUpTempFiles()) {
	deleteFile(dottyFilename);
	deleteFile(imageFilename);
      }
      return null;
    }

    // load image
    result = loadImage(imageFilename);

    if (cleanUpTempFiles()) {
      deleteFile(dottyFilename);
      deleteFile(imageFilename);
    }

    return result;
  }

  /**
   * Return the singleton.
   *
   * @return		the singleton
   */
  public static synchronized GraphVizTreeVisualization getSingleton() {
    if (m_Singleton == null)
      m_Singleton = new GraphVizTreeVisualization();
    return m_Singleton;
  }
}
