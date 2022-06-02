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
	public static BufferedImage player2;
	public static BufferedImage player3;
	public static BufferedImage player4;
	public static BufferedImage jumpIndicator;
	
	// Player Stats
	public static int playerX = 100;
	public static int playerY = 560;
	public static int height = 560;
	public static boolean grounded = true;
	public static int fallingSpeed = 0;
	
	// Levels (Place holders)
	public static BufferedImage level1;
	public static BufferedImage level2;
	public static BufferedImage Level3;
	public static BufferedImage level4;
	public static BufferedImage level5;
	
	// Collision arrays
	public static int[] wallArrX = {};
	public static int[] wallArrY = {};
	
	// Gamestate
	public static int gameState = 0;

	
	
	public JumpDude() {
		setPreferredSize(new Dimension(1280, 720));
		setBackground(new Color(255, 255, 255));
		
		try {
			// Initialize Variables
			menu = ImageIO.read(new File("menu.png"));
			level1 = ImageIO.read(new File("S1.1.jpg"));
			player = ImageIO.read(new File("Character Sprite1.png"));
			player2 = ImageIO.read(new File("Character Sprite2.png"));
			jumpIndicator = ImageIO.read(new File("jumpIndicator1.png"));
		}
		catch(Exception e) {
			System.out.println("IMAGE NOT FOUND");
		}
		
		// Create Thread
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
		if(grounded == false) {
			playerY += fallingSpeed;
			fallingSpeed += 1;
			if(playerY >= height) {
				playerY = height;
				grounded = true;
				fallingSpeed = 0;
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		
		// Clears screen
		super.paintComponent(g);
		
		// Menu screen
		if(gameState == 0) {
			g.drawImage(menu, 0, 0, null);
			
		}
		// Level 1
		if(gameState == 1) {
			g.drawImage(level1, 0, 0, null);
			if(grounded) {
				g.drawImage(player, playerX, playerY, null);
			}
			
			g.drawLine(1, 400, 50, 400);
			g.drawImage(jumpIndicator, 0, 0, null);
			if(!  grounded) {
				g.drawImage(player2, playerX, playerY, null);
			}
			
		}
		
		
	}
	
	// Run method updates every frame
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
		// If menu screen 
		if(gameState == 0) {
			if(e.getKeyChar() == ' ') {
				gameState = 1;
				paintComponent(this.getGraphics());
			}
		}
		else if(gameState == 1) {
			if(e.getKeyChar() == ' ') {
				if(grounded) {
					fallingSpeed = 0;
					fallingSpeed -= 20;
					grounded = false;
				}
			}
			if(e.getKeyCode() == 49) {
				height = 500;
			}
			if(e.getKeyCode() == 50) {
				height -= 100;
			}
			if(e.getKeyCode() == 51) {
				height -= 200;
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
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
}
// https://medium.com/@brazmogu/physics-for-game-dev-a-platformer-physics-cheatsheet-f34b09064558
