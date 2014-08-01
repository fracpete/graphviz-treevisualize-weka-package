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

/**
 * GraphVizTreeVisualization.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.visualize.plugins;

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

import javax.swing.ImageIcon;

import weka.core.Utils;

/**
 * Class for generating GraphViz graphs for trees.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class GraphVizTreeVisualization {

  /** the name of the props file to load. */
  public final static String PROPERTIES_FILE = "weka/gui/visualize/plugins/GraphVizTreeVisualization.props";
  
  /** the properties object. */
  protected static Properties PROPERTIES;
  
  /**
   * Returns the configuration, loads it if required.
   * 
   * @return		the configuration
   */
  protected static synchronized Properties getProperties() {
    if (PROPERTIES == null) {
      try {
	PROPERTIES = Utils.readProperties(PROPERTIES_FILE);
      }
      catch (Exception e) {
	PROPERTIES = new Properties();
	System.err.println("Failed to load properties: " + PROPERTIES_FILE);
	e.printStackTrace();
      }
    }
    
    return PROPERTIES;
  }
  
  /**
   * Returns the graphviz executable for turning a dotty notation string
   * into an image. 
   * 
   * @return		the executable
   */
  public static String getExecutable() {
    return getProperties().getProperty("Executable", "dot");
  }
  
  /**
   * Returns the additional options for the executable.
   * 
   * @return		the additional options, if any
   */
  public static String getAdditionalOptions() {
    return getProperties().getProperty("AdditionalOptions", "");
  }
  
  /**
   * Turns the dotty string into a {@link BufferedImage}.
   * 
   * @param dotty	the graph in dotty notation to turn into image
   * @return		the generated image, null if failed to convert
   */
  public static BufferedImage toBufferedImage(String dotty) {
    String		prefix;
    String		dottyFilename;
    String		pngFilename;
    int			rand;
    BufferedWriter	writer;
    String		additional;
    List<String>	cmd;
    ProcessBuilder	pb;
    Process		proc;
    int			retVal;
    ImageIcon		icon;
    BufferedImage 	result;
    Graphics2D 		g2d;
    
    rand          = (int) (Math.random() * 1000);
    prefix        = System.getProperty("java.io.tmpdir") + File.separator + "gvtv" + Integer.toHexString(rand);
    dottyFilename = prefix + ".dot";
    pngFilename   = prefix + ".png";
    
    // save dotty file
    writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(dottyFilename));
      writer.write(dotty);
      writer.newLine();
    }
    catch (Exception e) {
      System.err.println("Failed to write dotty string to " + dottyFilename);
      e.printStackTrace();
      return null;
    }
    finally {
      if (writer != null) {
	try {
	  writer.flush();
	  writer.close();
	}
	catch (Exception e){
	  // ignored
	}
      }
    }
    
    // assemble command
    cmd = new ArrayList<String>();
    cmd.add(getExecutable());
    cmd.add("-o");
    cmd.add(pngFilename);
    additional = getAdditionalOptions();
    if (!additional.trim().isEmpty())
      cmd.addAll(Arrays.asList(additional.trim().split(" ")));
    cmd.add(dottyFilename);
    
    // execute command
    pb = new ProcessBuilder(cmd);
    try {
      proc   = pb.start();
      retVal = proc.waitFor();
      if (retVal != 0)
	throw new IOException("GraphViz returned " + retVal);
    }
    catch (Exception e) {
      System.err.println("Failed to execute graphviz command: " + cmd);
      e.printStackTrace();
      new File(dottyFilename).delete();
      return null;
    }
    
    // load image
    icon   = new ImageIcon(pngFilename);
    result = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    g2d    = (Graphics2D) result.getGraphics();
    icon.paintIcon(null, g2d, 0, 0);
    g2d.dispose();

    new File(dottyFilename).delete();
    new File(pngFilename).delete();
    
    return result;
  }
}
