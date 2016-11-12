//import javax.swing.*;
//import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.applet.Applet;
import java.io.*;
import java.net.URL;
import javax.imageio.*;

public class VandyHacks extends Applet implements MouseListener, MouseMotionListener, FocusListener
{
	// Objective: Find as many Squirrels as you can within timelimit
	// Time Limit: 60s
	// ImageIO.read(getClass().getResource());	
	
	private static int mouseX, mouseY, mouseB;
	int appletWidth;
	int appletHeight;
	Color air, grass;
	BufferedImage im1, im3, im4, im5;
	private Graphics gBuffer;
    private Image virtualMem;  	
	boolean focus;
	int squX;
	int squY;
	Squirrel squirrel;
	boolean found;
	double score;
	Timer bob;
	long end,nochange;
	int once;
	String yes;
	
	public void init()
	{
  		appletWidth = getWidth();
		appletHeight = getHeight();

		virtualMem = createImage(appletWidth,appletHeight);
		gBuffer = virtualMem.getGraphics();
		
		air = new Color(151, 215, 212); // Air
		grass = new Color(125, 206, 92); // grass
		squX = random(0,1000);
		squY = random(504, 554);
		found = true;
		score = 0;
		bob = new Timer();
		once = -1;
		yes = "";
		
		mouseX = -1;
		mouseY = -1;
		mouseB = -1;

		im1 = tempRead(im1,("/Users/Kyler/workspace/VandyHacks/src/img1.png"));
		im3 = tempRead(im1,("/Users/Kyler/workspace/VandyHacks/src/img3.png"));
		im4 = tempRead(im1,("/Users/Kyler/workspace/VandyHacks/src/img4.png"));
		im5 = tempRead(im1,("/Users/Kyler/workspace/VandyHacks/src/img5.png"));
		
		squirrel = new Squirrel(squX,squY);
		
		
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addFocusListener(this);
		focus = false; // title screen only
	}

	public BufferedImage tempRead(BufferedImage name, String path)
	{
		try 
		{
			return (name = ImageIO.read(new File(path)));
		} 
		catch (IOException e) 
		{
		    System.out.println("The image was not loaded.");			
		}
		return null;
	}
	
	/*public BufferedImage getImage(String filename) {BufferedImage exp = null;try {InputStream in = getClass().getResourceAsStream(filename);return ImageIO.read(in);} catch (IOException e) {System.out.println("The image was not loaded.");}return null;}*/

	public void paint(Graphics g)
	{
		if(!focus)
		{
			titleScreen(g);
		}
		else
		{
			once++;
			// Don't update if the squirrel hasn't been clicked yet!!
			if(found)
			{
				squirrelFound(g);
				redrawMap(g);
				found = false;
			}
			
			if(once == 0)
			{
				nochange = System.currentTimeMillis();
				end = (nochange+(10000));
			}
			time(g);
		}
		
		repaint();
	}
	
	public void update(Graphics g)
	{	
		gBuffer.drawImage (virtualMem,0,0,this);
		paint(g);
	}
	
	public void squirrelFound(Graphics g)
	{
		// Squirrel has been clicked
		// (1) redraw map
		// (2) score += ((1/x)*100), where x is the time it took to click squirrel
		// (3) squirrel object re-assigned parameters
		
		//score += ;
		
		squX = random(0,1000);
		squY = random(504, 554);
		squirrel = new Squirrel(squX,squY);
		
	}
	
	public static void delay(int n)
	{
		long startDelay = System.currentTimeMillis();
		long endDelay = 0;
		while (endDelay - startDelay < n)
			endDelay = System.currentTimeMillis();
	}

	public void time(Graphics g)
	{	
		g.setFont(new Font("Times New Roman",Font.BOLD,36));
		g.setColor(air);
		g.drawString(yes, 50, 50);
		
		long dan = System.currentTimeMillis();
		System.out.println("Countdown - "+(dan - nochange));
		System.out.println("Countup   - "+(end - dan));
		
		if(((dan - nochange)) >= (end - dan))
			System.exit(0);
		
		g.setColor(Color.white);
		yes = Integer.toString((int)(System.currentTimeMillis()-nochange)/1000);
		g.drawString(yes, 50, 50);
	}
	
	public static void setBackground(Graphics g, Color bgColor)
	{   
		g.setColor(bgColor);
	    fillRectangle(g,0,0,4800,3600);
	}
	
	public static void fillRectangle(Graphics g, int x1, int y1, int x2, int y2)
	{
		int temp;
		if (x1 > x2)
			{ temp = x1; x1 = x2; x2 = temp; }
		if (y1 > y2)
			{ temp = y1; y1 = y2; y2 = temp; }
		int width  = x2 - x1 + 1;
		int height = y2 - y1 + 1;
		g.fillRect(x1,y1,width,height);
	}
	
	public static void setColor(Graphics g, int red, int green, int blue)
	{
		Color newColor = new Color(red, green, blue);
		g.setColor(newColor);
	}
	
	public static void setColor(Graphics g, Color mk)
	{
		g.setColor(mk);
	}
	
	public void titleScreen(Graphics g)
	{
			setBackground(g,grass);
			g.setColor(air);
	
			Font title = new Font("Algerian",Font.ITALIC,72);
			Font directions = new Font("Times New Roman",Font.BOLD,36);
			Font footNote = new Font("Times New Roman",Font.PLAIN,24);
	
			g.setFont(title);
			g.setColor(Color.white);
			g.drawString("DETECTIVE SQUIRREL",150,150);
			g.setFont(directions);
			g.drawString("   By: some UF students",320,235);
	
			g.drawString("Instructions:",400,400);
			g.setFont(footNote);
			g.drawString("Locate the hidden squirrel and score points the faster you are!" ,245,305);
			g.drawString("Click inside the window to begin/resume the game.",255,470);
	}
	
	public static int random(int min, int max)
	{
		int range = max - min + 1;
		int number = (int) (range * Math.random() + min);
		return number;
	}
	
	public void redrawMap(Graphics g)
	{
		// TREES
		int treeX;
		int treeY;
		
		// Squirrel
		
		// (1) Randomize map each time that the squirrel is found
		// (2) Place squirrel in map BEFORE Trees
		// (3) Determine Hit box based on placement of squirrel
		
		setBackground(g,air);
		setColor(g,grass);
		fillRectangle(g,0,550,1000,650);
		g.drawImage(im1,squX,squY,this);
		
		for(int i = 0; i < 27; i++)
		{
			treeX = random(0,1000);
			if(i < 10)
			{
				treeY = random(328, 352);
				g.drawImage(im3,treeX,treeY,this);
			}
			else if(i > 10 && i < 20)
			{
				treeY = random(352, 376);
				g.drawImage(im4,treeX,treeY,this);
			}
			else
			{
				treeY = random(376, 400);
				g.drawImage(im5,treeX,treeY,this);
			}
		}
	}
	
	public int getX() {
		return mouseX;
	}
	
	public int getY() {
		return mouseY;
	}
	
	public static int getButton() {
		return mouseB;
	}
	
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	public void mouseClicked(MouseEvent e) 
	{
		System.out.println("MOUSE Clicked");
	
		if(!focus)
		{
			if(e.getX() >= squirrel.getX() && e.getX() <= squirrel.getX()-44 && e.getY() >= squirrel.getY() && e.getY() <= squirrel.getY()-46)
			{
				found = true;
			}
		}
	}
	
	public void mouseEntered(MouseEvent e) 
	{
		System.out.println("MOUSE Entered: X "+e.getX());	
		System.out.println("MOUSE Entered: Y "+e.getY());
	}
	
	public void mouseExited(MouseEvent e) 
	{
		
	}
	
	public void mousePressed(MouseEvent e) {
		mouseB = e.getButton();
	}
	
	public void mouseReleased(MouseEvent e) {
		mouseB = -1;
	}
	
	public void focusGained(FocusEvent e)  { focus = true;  }

	public void focusLost(FocusEvent e) 	{}
	
}

class Squirrel
{
	// Dimensions are 44 x 46
	int x;
	int y;
	
	public Squirrel(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX(){return x;}
	public int getY(){return y;}

}