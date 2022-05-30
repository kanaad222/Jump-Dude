import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JumpDude extends JPanel implements Runnable, KeyListener{
	
	// Images
	public static BufferedImage menu;
	public static BufferedImage player;
	
	public static int playerX = 50;
	public static int playerY = 500;
	public static boolean jump = false;
	public static int fallingSpeed = 0;
	
	public static BufferedImage level1;
	public static BufferedImage level2;
	public static BufferedImage Level3;
	public static BufferedImage level4;
	public static BufferedImage level5;
	public static BufferedImage jumpIndicator1;
	
	public static int[] wallArrX = {};
	public static int[] wallArrY = {};
	
	
	public static int gameState = 0;
	public static boolean jumpState = false;
	
	
	
	
	public JumpDude() {
		setPreferredSize(new Dimension(1280, 720));
		setBackground(new Color(255, 255, 255));
		
		try {
			menu = ImageIO.read(new File("menu.png"));
			player = ImageIO.read(new File("testPlayer.png"));
			jumpIndicator1 = ImageIO.read(new File("jumpIndicator1.png"));
		}
		catch(Exception e) {
			System.out.println("IMAGE NOT FOUND");
		}
		
		Thread thread = new Thread(this);
		thread.start();
		
		addKeyListener(this);
		this.setFocusable(true);
	}
	
	public static void main(String[] args) {
		JFrame gameWindow = new JFrame("Jump Dude");
		JumpDude gamePanel = new JumpDude();
		gameWindow.add(gamePanel);
		gameWindow.pack();
		gameWindow.setVisible(true);
	}
	
	public static void updatePlayer() {
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if(gameState == 0) {
			 
			g.drawImage(jumpIndicator1, 0, 0, null);
			g.drawImage(player, playerX, playerY, null);
			g.drawLine(1, 400, 50, 400);
			
		}
		
		
	}
	

	public void run() {
		while(true) {
			repaint();
			updatePlayer();
			try {
				Thread.sleep(1000/60);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(gameState == 0) {
			if(e.getKeyChar() == ' ') {
				gameState = 1;
				paintComponent(this.getGraphics());
			}
		}
		else if(gameState == 1) {
			if(e.getKeyChar() == ' ') {
				
				
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyChar() == ' ') {
		}
	}
	
	public static boolean collision(int playerX, int playerY, int[] wallArrX, int[] wallArrY) {
		for(int x = 0; x < wallArrX.length; x++) {
			for(int y = 0; y < wallArrY.length; y++) {
				return false;
			}
		}
		return false;
	}
	
	// Gets the angle of a line using 2 points
	// To check for slope direction
	public static double getAngle(int x1, int y1, int x2, int y2) {
		return Math.atan2(y2-y1, x2-x1) * 180 / Math.PI;
	}
	public static int pointInBetween(int x1, int x2) {
		return (Integer) null;
	}
}
// https://medium.com/@brazmogu/physics-for-game-dev-a-platformer-physics-cheatsheet-f34b09064558
