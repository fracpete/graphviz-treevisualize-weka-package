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
 * GraphVizPanel.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.visualize.plugins;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import weka.gui.visualize.PrintablePanel;

/**
 * Displays a GraphViz graph in dotty notation.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class GraphVizPanel
  extends PrintablePanel {

  /** for serialization. */
  private static final long serialVersionUID = 1137451067421501306L;

  /** the dotty notation. */
  protected String m_Dotty;
  
  /** the generated image. */
  protected BufferedImage m_Image;
  
  /**
   * Initializes the panel.
   */
  public GraphVizPanel() {
    super();
    m_Dotty = null;
    m_Image = null;
  }
  
  /**
   * Sets the dotty graph.
   * 
   * @param value	the graph
   */
  public void setDotty(String value) {
    m_Dotty = value;
    m_Image = GraphVizTreeVisualization.toBufferedImage(m_Dotty);
    if (m_Image == null) {
      setSize(new Dimension(600, 400));
      setMinimumSize(new Dimension(600, 400));
      setPreferredSize(new Dimension(600, 400));
    }
    else {
      setSize(new Dimension(m_Image.getWidth(), m_Image.getHeight()));
      setMinimumSize(new Dimension(m_Image.getWidth(), m_Image.getHeight()));
      setPreferredSize(new Dimension(m_Image.getWidth(), m_Image.getHeight()));
    }
    repaint();
  }
  
  /**
   * Return the dotty graph.
   * 
   * @return		the graph, null if none set
   */
  public String getDotty() {
    return m_Dotty;
  }
  
  /**
   * Returns the image generated from the dotty graph.
   * 
   * @return		the image, null if not available
   */
  public BufferedImage getImage() {
    return m_Image;
  }
  
  /**
   * Paints the component.
   * 
   * @param g		the graphics context
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    // clear
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, getWidth(), getHeight());
    
    // paint image
    if (m_Image != null)
      g.drawImage(m_Image, 0, 0, null);
  }
}
