/*********************************
 * Picture.java
 * A picture class
 * The class allows users to create/modify/save pictures in jpg's, png's, 
 * and bitmap's.
 * 
 * @author Shaun M. McFall, Denison University
 * Summer, 2008
 **/
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.Color;
import javax.imageio.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.lang.Math;

public class Picture implements FocusListener, MouseListener, MouseMotionListener, KeyListener {
 protected int height;
 protected int width;
 protected BufferStrategy strategy;
 protected GraphicsDevice device;
 protected DisplayMode origDM;
 protected GraphicsConfiguration config;
 protected JFrame mainFrame;
 protected Insets insets;
 protected Color penColor = Color.black;
 protected double penX, penY;
 protected double penWidth = 1.0f;
 protected Stroke stroke;
 protected BufferedImage image;
 protected Graphics2D drawGraphics;
 protected boolean penUp = false;
 protected double penDirection = 0.0;
 protected Font font;
 // Mouse data
 private boolean mouse1Down = false;
 private boolean mouse2Down = false;
 private boolean mouse3Down = false;
 private int mouseX = 0;
 private int mouseY = 0;
 // Key data
 private int keyCode = 0;
 private char keyChar = '\0';
 private String keyString = null;
 private KeyEvent keyEvent = null;
 private boolean[] keyArray;


 /** 
  * Constructor that creates a picture of the given width w and height h
  * @param w The width of the picture
  * @param h The height of the picture
  **/
 public Picture(int w, int h) {
   height = h;
   width = w;
   setUpFrame(false);
   image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
   drawGraphics = (Graphics2D)image.getGraphics();
   drawGraphics.setColor(Color.white);
   drawGraphics.fillRect(0, 0, width, height);
   drawGraphics.setColor(penColor);
   drawGraphics.setFont(font);
   drawGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 }

 /** 
  * Constructor that creates a picture of the given width w and height h
  * @param w The width of the picture
  * @param h The height of the picture
  * @param isDecorated Whether or not the frame is decorated (has frames around it)
  **/
 public Picture(int w, int h, boolean isDecorated) {
   height = h;
   width = w;
   setUpFrame(!isDecorated);
   image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
   drawGraphics = (Graphics2D)image.getGraphics();
   drawGraphics.setColor(Color.white);
   drawGraphics.fillRect(0, 0, width, height);
   drawGraphics.setColor(penColor);
   drawGraphics.setFont(font);
   drawGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 }

 /**
  * Constructor that takes in the pathname as the parameter and creates a
  * picture. The pathname can be an absolute pathname or pathname relative to
  * the current working directory.
  * @param path The absolute pathname of the file.
  */
 public Picture (String path)
 {
   mainFrame = new JFrame(path);  // Invoke the JFrame constructor first

   //This is used to get the information to read a buffered image.
   File pathFile;  

   /*
    * Creates a file from the given string name. If the path name is null
    * and a file cannot be created, an error is sent to the console. The 
    * program sees if the file can be used to create a buffered image. If not,
    * an error is sent to the console.
    **/
   try {
     pathFile = new File(path); 
   }
   catch (NullPointerException e ){
     System.err.println(e);
     return;
   }
   try {  
     image = ImageIO.read(pathFile);  
   }
   catch(IOException e) {
     System.err.println(e);
     return;
   }

   width = image.getWidth();
   height = image.getHeight();
   setUpFrame(false);  //Sets the frame to the appropiate size to fully display
   //the image.

   /*
    * Get the graohics of the buffered Image and set the pen color to a
    * default black
    **/
   drawGraphics = (Graphics2D)image.getGraphics();
   drawGraphics.setColor(penColor);
   drawGraphics.setFont(font);
 }

 /**
  * Constructor that takes in the pathname as the parameter and creates a
  * picture. The pathname can be an absolute pathname or pathname relative to
  * the current working directory.
  * @param path The absolute pathname of the file.
  * @param isDecorated Whether or not the frame is decorated (has frames around it)
  */
 public Picture (String path, boolean isDecorated)
 {
   mainFrame = new JFrame(path);  // Invoke the JFrame constructor first

   //This is used to get the information to read a buffered image.
   File pathFile;  

   /*
    * Creates a file from the given string name. If the path name is null
    * and a file cannot be created, an error is sent to the console. The 
    * program sees if the file can be used to create a buffered image. If not,
    * an error is sent to the console.
    **/
   try {
     pathFile = new File(path); 
   }
   catch (NullPointerException e ){
     System.err.println(e);
     return;
   }
   try {  
     image = ImageIO.read(pathFile);  
   }
   catch(IOException e) {
     System.err.println(e);
     return;
   }

   width = image.getWidth();
   height = image.getHeight();
   setUpFrame(!isDecorated);  //Sets the frame to the appropiate size to fully display
   //the image.

   /*
    * Get the graohics of the buffered Image and set the pen color to a
    * default black
    **/
   drawGraphics = (Graphics2D)image.getGraphics();
   drawGraphics.setColor(penColor);
   drawGraphics.setFont(font);
 }

 /**
  * sets up the frame with the appropiate sizes and insets
  * also adds the focus listener
  **/
 private void setUpFrame(boolean isUndecorated) {
   device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
   origDM = device.getDisplayMode();
   config = device.getDefaultConfiguration();
   mainFrame = new JFrame(config);
   mainFrame.setUndecorated(isUndecorated);
   mainFrame.addFocusListener(this);
   mainFrame.addMouseListener(this);
   mainFrame.addMouseMotionListener(this);
   mainFrame.addKeyListener(this);
   mainFrame.setResizable(false);
   mainFrame.setIgnoreRepaint(true);
   mainFrame.addNotify(); 
   insets = mainFrame.getInsets();
   mainFrame.setSize(width + insets.left + insets.right - 1,
                     height + insets.top + insets.bottom - 1);
   mainFrame.createBufferStrategy(2);
   strategy = mainFrame.getBufferStrategy();
   mainFrame.setVisible(false);
   keyArray = new boolean[KeyEvent.KEY_LAST];
   for (int i = 0; i < keyArray.length; i++)
     keyArray[i] = false;
   font = new Font("arial", Font.PLAIN, 12);
   mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 }

 /**
  * Method to set it so that the program terminates upon exiting the picture.
  * If you set exitOnClose to be true, Dr. Java will throw an error, but will
  * not crash.
  * @param exit Set whether to exit on close or not.
  **/
 public void exitOnClose(boolean exit) {
   if (exit) {
     mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   } else {
     mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
   }
 }

 /**
  * Method to display a picture. The method can also be invoked to display
  * the picture after it has been updated.
  **/
 public void display() {
   if (!mainFrame.isVisible())
     mainFrame.setVisible(true);
   ((Graphics2D)strategy.getDrawGraphics()).drawImage(image, null, insets.left, insets.top);
   strategy.show();
 }

 private void updateNoDisplay() {
   ((Graphics2D)strategy.getDrawGraphics()).drawImage(image, null, insets.left, insets.top);
   strategy.show();
 }

 /**
  * Method to close the picture
  **/
 public void close() {
   if (mainFrame.isVisible())
     mainFrame.setVisible(false);
 }

 /** 
  *  Method to get the height of the picture
  *  @return Picture height
  **/
 public int getHeight() {  
   return height; 
 }

 /** 
  * Method to get the width of the picture
  * @return Picture width
  **/
 public int getWidth() {   
   return width;
 }

 /** 
  * Returns the intensity of the red component at the coordinate (x,y)
  * @param x The x coordinate
  * @param y The y coordinate
  * 
  * @return Returns the red value at the given x and y
  **/
 public int getPixelRed(int x, int y) {
   return new Color(image.getRGB(x,y)).getRed();
 }

 /** 
  * Returns the intensity of the green component at the coordinate (x,y)
  * @param x The x coordinate
  * @param y The y coordinate
  * 
  * @return Returns the green value at the given x and y
  **/
 public int getPixelGreen(int x, int y) {
   return new Color(image.getRGB(x,y)).getGreen();
 }

 /** 
  * Returns the intensity of the blue component at the coordinate (x,y)
  * @param x The x coordinate
  * @param y The y coordinate
  * 
  * @return Returns the blue value at the given x and y
  **/
 public int getPixelBlue(int x, int y) {
   return new Color(image.getRGB(x,y)).getBlue();
 }

 /** 
  * Sets the red component of the pixel at coordinate (x,y) to intensity
  * given by r. Valid values are in the range 0-255
  * Returns an error message to the console if the r parameter is not within range.
  * @param x The x coordinate
  * @param y The y coordinate
  * @param r The new value
  **/
 public void setPixelRed(int x, int y, int r) {
   if (r >= 0 && r <= 255)  {
     Color c = new Color(image.getRGB(x,y));
     image.setRGB(x, y, new Color(r, c.getGreen(), c.getBlue()).getRGB());
   } else {
     System.err.println("Color value must be from 0-255");
   }
 }

 /** 
  * Sets the green component of the pixel at coordinate (x,y) to intensity
  * given by g. Valid values are in the range 0-255
  * Returns an error message to the console if the g parameter is not within range.
  * @param x The x coordinate
  * @param y The y coordinate
  * @param g The new value
  */
 public void setPixelGreen(int x, int y, int g) {
   if (g >= 0 && g <= 255)  {
     Color c = new Color(image.getRGB(x,y));
     image.setRGB(x, y, new Color(c.getRed(), g, c. getBlue()).getRGB());
   } else {
     System.err.println("Color value must be from 0-255");
   }
 }

 /** 
  * Sets the blue component of the pixel at coordinate (x,y) to intensity
  * given by b. Valid values are in the range 0-255
  * Returns an error message to the console if the b parameter is not within range.
  * @param x The x coordinate
  * @param y The y coordinate
  * @param b The new value
  **/
 public void setPixelBlue(int x, int y, int b) {
   if (b >= 0 && b <= 255)  {
     Color c = new Color(image.getRGB(x,y));
     image.setRGB(x, y, new Color(c.getRed(), c.getGreen(), b).getRGB());
   } else {
     System.err.println("Color value must be from 0-255");
   }
 }

 /** 
  * Method to get a color object for the pixel given by (x,y)
  * @param x The x coordinate
  * @param y The y coordinate
  * 
  * @return Returns the color at the given x and y
  **/
 public Color getPixelColor(int x, int y) {
   return new Color(image.getRGB(x, y));
 }

 /** 
  * Sets the color of the pixel at (x,y) to c
  * @param x The x coordinate
  * @param y The y coordinate
  * @param c The new color to be set
  **/
 public void setPixelColor(int x, int y, Color c) {
   image.setRGB(x, y, c.getRGB());
 }

 /** 
  * Sets the color of the pixel at (x,y) to the given
  * red, green, and blue values
  * @param x The x coordinate
  * @param y The y coordinate
  * @param r The red value (0-255)
  * @param g The green value (0-255)
  * @param b The blue value (0-255)
  **/
 public void setPixelColor(int x, int y, int r, int g, int b) {
   image.setRGB(x, y, new Color(r, g, b).getRGB());
 }

 /**
  * Method saves the picture to the user's computer. It supports jpg's, 
  * bitmap's, and png's
  * @param s The absolute pathname
  * @return true if the file has been created successfully and false
  *          if the file is not created.
  * @return false if pathname is null
  **/
 public boolean writeFile(String s) {
   display();
   //If the pathname is null, the method discontinues and false is returned.
   if (s == null)
     return false;

   // Get the extension and determine what to do with it
   int dot = s.lastIndexOf(".");
   int val;
   if (dot != -1) {
     String ext = s.substring(dot);
     if (ext.equalsIgnoreCase(".jpg") || ext.equalsIgnoreCase(".jpeg"))
       val = 1;
     else if (ext.equalsIgnoreCase(".png"))
       val = 2;
     else if (ext.equalsIgnoreCase(".bmp"))
       val = 3;
     else {
       System.err.println("You can only save as .jpg, .png, or .bmp");
       return false;
     }
   } else {
     s += ".png";
     val = 2;
   }

   boolean flag = false;
   File filename = new File(s);

   /*
    * According to the extension inputed by the user, the image is then
    * saved on the disk with the required file extension
    **/
   if(val >3 || val < 1){
     System.err.println("Wrong input for saving a file.");
     flag = false;
   }else if(val == 1){
     flag = errorManagement("jpg", filename);
   }else if(val == 2){
     flag = errorManagement("png", filename);
   }else if(val == 3){
     flag = errorManagement("bmp", filename);
   }
   return flag;
 }

 /** 
  * Method is a part of the writeFile method and checks to see if the image
  * can be written and returns false if it cannot.
  */
 private boolean errorManagement(String ext, File f) {
   try {
     ImageIO.write(image, ext, f);
   }
   catch(IOException ioException) {
     System.err.println(ioException);
     return false;
   }
   catch(IllegalArgumentException e) {
     System.err.println(e);
     return false;
   }
   return true;

 }

 /**
  * Sets the color for the drawing of 2D objects
  * @param c The Color that you wish to set to
  **/
 public void setPenColor(Color c) {
   penColor = c;
   drawGraphics.setColor(c);
 }

 /**
  * The method returns a color object for the pen color at current coordinate.
  * @return Color object for the present pen color.
  **/
 public Color getPenColor() {
   return penColor;
 }

 /** 
  * Draws a line betweeen (x1,y1) and (x2,y2). After drawing the line, the 
  * current position is (x2,y2).
  * @param x1 The x co-ordinate of the first point
  * @param y1 The y co-ordinate of the first point
  * @param x2 The x co-ordinate of the second point
  * @param y2 The y co-ordinate of the second point
  **/
 public void drawLine(int x1, int y1, int x2, int y2) {
   drawGraphics.drawLine(x1, y1, x2, y2);
   penX = x2;
   penY = y2;
 }

 /**
  * Draws a line from the present coordinate to the other coordinate 
  * specified (x,y).After drawing the line, the current position is (x,y)
  * which are the coordinates passed in.
  * @param x The x co-ordinate of the second point
  * @param y The y co-ordinate of the second point
  **/
 public void drawLine(int x, int y) {
   drawGraphics.drawLine((int)penX, (int)penY, x, y);
   penX = x;
   penY = y;
 }

 /**
  * Draws an ouline of a circle at the x and y coordinates and the radius
  * given by user. The current x and y positions is set to the centre of 
  * the circle.
  * @param x The x coordinate of the centre of the circle.
  * @param y The y coordinate of the centre of the circle.
  * @param radius The radius of the circle to be drawn
  **/
 public void drawCircle(int x, int y, int radius) {
   drawGraphics.drawOval(x - radius, y - radius, radius*2, radius*2);
   penX = x - radius;
   penY = y - radius;
 }

 /**
  * Draws a filled circle of the radius given at the coordinate (x,y). The 
  * circle is drawn with the current color. The current positon is set to the
  * centre of the circle.
  * @param x The x coordinate of the centre of the circle.
  * @param y The y coordinate of the centre of the circle.
  * @param radius The radius of the circle to be drawn
  **/
 public void drawCircleFill(int x, int y, int radius) {
   drawGraphics.fillOval(x - radius, y - radius, radius*2, radius*2);
   penX = x - radius;
   penY = y - radius;
 }

 /**
  * Method draws the outline of an Ellipse given the x and y coordinates 
  * with the lenght of the major and minor axis. The current position is set
  * to the centre of the ellipse
  * @param x The x coordinate
  * @param y The y coordinate
  * @param minor The lenght of the minor axis
  * @param major The lenght of the major axis.
  **/
 public void drawEllipse(int x,int y, int minor, int major) {
   penX = x;
   penY = y;
   drawGraphics.draw(new Ellipse2D.Double(penX,penY,minor,major));
 }

 /**
  * Method draws a filled Ellipse given the x and y coordinates with the 
  * lenght of the major and minor axis. The current position is set
  * to the centre of the ellipse
  * @param x The x coordinate
  * @param y The y coordinate
  * @param minor The lenght of the minor axis
  * @param major The lenght of the major axis.
  **/
 public void drawEllipseFill(int x, int y, int minor, int major) {
   penX = x;
   penY = y;
   drawGraphics.fill(new Ellipse2D.Double(penX,penY,minor,major));
 }

 /**
  * Draws the outline of the rectangle at (x,y) with the height h and width w.
  * The current position is set to the top left corner of the rectangle.
  * @param x The x coordinate of the top left corner of the rectangle
  * @param y The y coordinate of the top left corner of the rectangle
  * @param w The width of the rectangle
  * @param h The height of the rectangle
  **/
 public void drawRect(int x, int y, int w, int h) { 
   drawGraphics.drawRect(x, y, w, h);
   penX = x;
   penY = y;
 }

 /**
  * Draws the rectangle at (x,y) with the height h and width w. Also fills up
  * the rectangle with the current color. The current position is set to the 
  * top left corner of the rectangle.
  * @param x The x coordinate of the top left corner of the rectangle
  * @param y The y coordinate of the top left corner of the rectangle
  * @param w The width of the rectangle
  * @param h The height of the rectangle
  **/
 public void drawRectFill(int x , int y , int w, int h) {
   drawGraphics.fillRect(x, y, w, h);
   penX = x;
   penY = y;
 }

 /**
  * Draws a single pixel using the current pen color. The current position is set to the 
  * given coordinates of the rectangle.
  * @param x The x coordinate
  * @param y The y coordinate
  **/
 public void drawPixel(int x, int y) {
   image.setRGB(x, y, penColor.getRGB());
 }

 /**
  * Draws a single pixel at the current pen location
  **/
 public void drawPixel() {
   image.setRGB((int)penX, (int)penY, penColor.getRGB());
 }

 /**
  * Sets the font given the parameters.
  * @param fontName The name of the font style (i.e. Arial, Helvetica)
  * @param style The style, use 0 for plain, 1 for italic, and 2 for bold
  * @param size The size of the font
  **/
 public void setFont(String fontName, int style, double size) {
   font = new Font(fontName, style, (int)size);
   drawGraphics.setFont(font);
 }

 /**
  * Set the size of the font.
  * @param size The new size for this font
  **/
 public void setFontSize(double size) {
   font = font.deriveFont((float)size);
   drawGraphics.setFont(font);
 }

 /**
  * Draws a given string to the screen. The current position is set to the 
  * top left corner of the rectangle.
  * @param x The X coordinate
  * @param y The Y coordinate
  * @param s The string
  **/
 public void drawString(int x, int y, String s) {
   penX = x;
   penY = y;
   drawGraphics.drawString(s, x, y+font.getSize());
 }

 /**
  * Sets the pen up.
  **/
 public void setPenUp() {
   penUp = true;
 }

 /**
  * Sets the pen down
  **/
 public void setPenDown() {
   penUp = false;
 }

 /**
  * Checks to see if the Pen is up. Returns true if the pen is up and false 
  * if it is not.
  * @return boolean to show pen status
  **/
 public boolean isPenUp() {
   return penUp;
 }

 /**
  * Changes the x coordinate to that specified by the user
  * @param x The x co-ordinate of the new point.
  **/
 public void setX (double x) {
   penX = x;
 }

 /**
  * Changes the y coordinate to that specified by the user
  * @param y The y co-ordinate of the new point.
  **/
 public void setY (double y) {
   penY = y;
 }

 /**
  * Method sets the pen to the x,y coordinate of the user's choice.
  * @param x the new x coordinate
  * @param y the new y coordinate
  **/
 //-------------------------------------------------------------------------
 public void setPosition(double x, double y) {
   penX = x;
   penY = y;
 }

 /** 
  * Method sets the current direction to that of the user's choice.
  * Negative degrees are mesured clockwise. Positive degrees are measure
  * counterclockwise
  * @param d The degree of the direction
  **/
 public void setDirection(double d) {
   penDirection = d;
 }

 public void rotate(double d) {
   penDirection += d;
 }
 
 /** 
  * Method returns the current direction in degrees.
  * @return The degree of current direction
  **/
 public double getDirection() {
   return penDirection;
 }

 /**
  * Method shifts the current positon by moving forward with the value that
  * the user wishes in accordance with the current pen width.
  * @param dist The number of pixels by which to go forward
  **/
 public void drawForward(double dist) {
   double deltaX, deltaY;
   double radians;

   /*
    * The line needs to be drawn in the same direction as the amount of
    * degree. Trignometry is used to find the coordinates of the podouble to 
    * which the line needs to be drawn using the distance that has been 
    * received.The cosine of the angle gives the x coordinate and the sine of
    * the angle gives the y coordinate of the new podouble.
    **/
   radians = Math.toRadians(90 + penDirection);
   deltaX = (penWidth) * Math.cos(radians);
   deltaY = (penWidth) * Math.sin(radians);

   /*
    * since deltaX and deltaY represent the value of the new podouble in a 
    * coordinate system where the starting podouble(x,y) is considered as (0,0);
    * they need to be converted to the values in the coordinate system of the 
    * picture. The new coordinates also have to be represented as doubleegers so
    * as to draw the line.
    **/
   double x1 = penX + deltaX/2;
   double y1 = penY - deltaY/2;

   radians = Math.toRadians(penDirection);
   deltaX = (dist) * Math.cos(radians);
   deltaY = (dist) * Math.sin(radians);
   double x2 = x1 + deltaX;
   double y2 = y1 - deltaY;

   radians = Math.toRadians(270 + penDirection);
   deltaX = penWidth * Math.cos(radians);
   deltaY = penWidth * Math.sin(radians);
   double x3 = x2 + deltaX;
   double y3 = y2 - deltaY;

   radians = Math.toRadians(180 + penDirection);
   deltaX = (dist) * Math.cos(radians);
   deltaY = (dist) * Math.sin(radians);
   double x4 = x3 + deltaX;
   double y4 = y3 - deltaY;

   /*
    * Creating two separate arrays storing the values of the x and y 
    * coordinates of the rectangle that needs to be drawn.
    **/
   int[] xarray = {(int)x1,(int)x2,(int)x3,(int)x4};
   int[] yarray = {(int)y1,(int)y2,(int)y3,(int)y4};
   int npoints = 4;
   if (!penUp) {
      drawGraphics.fillPolygon(xarray, yarray, npoints);
   }

   /*
    * Set current position to the mid-podouble of the other height of the
    * rectangle created.
    **/
   penX = (double)(x3+x2)/2f;
   penY = (double)(y3+y2)/2f;  
 }

 public void fillPoly(int[] X, int[] Y, int n) {
    drawGraphics.fillPolygon(X,Y,n);
 }
 
 /**
  * Method returns the current X Position
  * @return The X coordinate of the current position
  **/
 public double getX() {
   return penX;
 }

 /**
  * Method returns the current Y Position
  * @return The Y coordinate of the current position
  **/
 public double getY() {
   return penY;
 }

 /**
  * Method sets the width of the pen
  *  @param pWidth The new width of the pen
  **/
 public void setPenWidth(double pWidth) {
   penWidth = pWidth;
   stroke = new BasicStroke((float)penWidth,BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND);
   drawGraphics.setStroke(stroke);
 }

 /**
  * Method returns the width of the pen
  *  @return The width of the pen
  **/
 public double getPenWidth() {
   return penWidth;
 }

 /**
  * Method returns the red channel of the
  * current pen color
  * 
  * @return the red channel of the pen color
  **/
 public int getPenRed() {
   return penColor.getRed();
 }

 /**
  * Method returns the green channel of the
  * current pen color
  * 
  * @return the green channel of the pen color
  **/
 public int getPenGreen() {
   return penColor.getGreen();
 }

 /**
  * Method returns the blue channel of the
  * current pen color
  * 
  * @return the blue channel of the pen color
  **/
 public int getPenBlue() {
   return penColor.getBlue();
 }

 /**
  * Set the red channel of the pen, leaving the
  * rest of the colors intact.
  * 
  * @param r The new red value (0-255)
  **/
 public void setPenRed(int r) {
   penColor = new Color(r, getPenGreen(), getPenBlue());
   drawGraphics.setColor(penColor);
 }

 /**
  * Set the green channel of the pen, leaving the
  * rest of the colors intact.
  * 
  * @param g The new green value (0-255)
  **/
 public void setPenGreen(int g) {
   penColor = new Color(getPenRed(), g, getPenBlue());
   drawGraphics.setColor(penColor);
 }

 /**
  * Set the blue channel of the pen, leaving the
  * rest of the colors intact.
  * 
  * @param b The new red value (0-255)
  **/
 public void setPenBlue(int b) {
   penColor = new Color(getPenRed(), getPenGreen(), b);
   drawGraphics.setColor(penColor);
 }

 /**
  * Set the color of the pen.
  * 
  * @param r The red value (0-255)
  * @param g The green value (0-255)
  * @param b The blue value (0-255)
  **/
 public void setPenColor(int r, int g, int b) {
   penColor = new Color(r, g, b);
   drawGraphics.setColor(penColor);
 }

 /**
  * Returns whether the mouse1 button is down
  * 
  * @return true if the mouse1 button is down, 
  * false if the mouse1 button is up
  **/
 public boolean isMouse1Down() {
   return mouse1Down;
 }

 /**
  * Returns whether the mouse2 button is down
  * 
  * @return true if the mouse2 button is down, 
  * false if the mouse2 button is up
  **/
 public boolean isMouse2Down() {
   return mouse2Down;
 }

 /**
  * Returns whether the mouse3 button is down
  * 
  * @return true if the mouse3 button is down, 
  * false if the mouse3 button is up
  **/
 public boolean isMouse3Down() {
   return mouse3Down;
 }

 /**
  * Returns the current mouse X position
  * 
  * @return The X position of the mouse
  **/
 public int getMouseX() {
   return mouseX;
 }

 /**
  * Returns the current mouse Y position
  * 
  * @return The Y position of the mouse
  **/
 public int getMouseY() {
   return mouseY;
 }

 /**
  * Sets the title of the picture.
  * @param title The title of the picture.
  **/
 public void setTitle(String title) {
   mainFrame.setTitle(title);
 }

 /**
  * Sets the location of the picture on the screen.
  * @param x The x coordinate of the upper left corner of the picture on screen
  * @param y The y coordinate of the upper left corner of the picture on screen.
  **/
 public void setWindowLocation(int x, int y) {
   mainFrame.setLocation(x, y);
 }

 /**
  * Get whether or not the given key is down.
  * Make sure to 'import java.awt.event.KeyEvent'.
  * See http://java.sun.com/javase/6/docs/api/java/awt/event/KeyEvent.html
  * @see java.awt.event.KeyEvent
  * @param keyCode The code corresponding to the key.
  * @return true if the key is down.
  **/
 public boolean isKeyDown(int keyCode) {
   return keyArray[keyCode];
 }

 /**
  * Allows you to draw one picture on to another one
  * at the given coordinates (specified by the upper left
  * corner of the source picture)
  * @param x The X value of the upper left corner of the source picture
  * @param y The Y value of the upper left corner of the source picture
  * @param pic The source Picture (the picture you want to draw)
  **/
 public void drawPicture(int x, int y, Picture pic) {
   pic.updateNoDisplay();
   drawGraphics.drawImage(pic.image, null, x, y);
 }

 /**
  * Internal method. Used to redraw the
  * picture when focus is regained.
  **/
 public void focusGained(FocusEvent e) {
   display();
 }

 /**
  * Internal method. Used when the
  * picture loses focus.
  **/
 public void focusLost(FocusEvent e) {
 }

 /**
  * Internal method. Used when the
  * mouse is pressed.
  **/
 public void mousePressed(MouseEvent e) {
   if (e.getButton() == MouseEvent.BUTTON1)
     mouse1Down = true;
   else if (e.getButton() == MouseEvent.BUTTON2)
     mouse2Down = true;
   else if (e.getButton() == MouseEvent.BUTTON3)
     mouse3Down = true;
 }

 /**
  * Internal method. Used when the
  * mouse is released.
  **/
 public void mouseReleased(MouseEvent e) {
   if (e.getButton() == MouseEvent.BUTTON1)
     mouse1Down = false;
   else if (e.getButton() == MouseEvent.BUTTON2)
     mouse2Down = false;
   else if (e.getButton() == MouseEvent.BUTTON3)
     mouse3Down = false;
 }

 /**
  * Internal method. Used when the
  * mouse enters the frame.
  **/
 public void mouseEntered(MouseEvent e) {
 }

 /**
  * Internal method. Used when the
  * mouse exits the frame.
  **/
 public void mouseExited(MouseEvent e) {
 }

 /**
  * Internal method. Used when the
  * mouse is clicked.
  **/
 public void mouseClicked(MouseEvent e) {
 }

 /**
  * Internal method. Used when the
  * mouse is dragged across the frame.
  **/
 public void mouseDragged(MouseEvent e) {
   mouseX = e.getX() - insets.left;
   mouseY = e.getY() - insets.top;
 }

 /**
  * Internal method. Used when the
  * mouse is moved.
  **/
 public void mouseMoved(MouseEvent e) {
   mouseX = e.getX() - insets.left;
   mouseY = e.getY() - insets.top;
 }

 /**
  * Internal method. Used when a
  * key is pressed.
  **/
 public void keyPressed(KeyEvent e) {
   keyArray[e.getKeyCode()] = true;
 }

 /**
  * Internal method. Used when a
  * key is released.
  **/
 public void keyReleased(KeyEvent e) {
   keyArray[e.getKeyCode()] = false;
 }

 /**
  * Internal method. Used when a
  * key is typed.
  **/
 public void keyTyped(KeyEvent e) {
 }

}

