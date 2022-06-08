import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JumpDude extends JPanel implements Runnable, KeyListener{
	
	// Images
	public static BufferedImage menu;
	public static BufferedImage playerRight;
	public static BufferedImage playerLeft;
	public static BufferedImage player2;
	public static BufferedImage player3;
	public static BufferedImage player4;
	public static BufferedImage jumpIndicator;
	
	// Player Stats
	public static int playerX = 100;
	public static int playerY = 560;
	public static int height = 560;
	public static boolean jump = true;
	public static int fallingSpeed = 0;
	public static int speed = 0;
	// 1 for right and -1 for left
	public static int direction = 1;
	
	// Hitboxes
	public static int playerXHitBox = playerX;
	public static int playerYHitBox = playerY;
	public static int player2YHitBox = playerY;
	public static int floor1XHitBox = 0;
	public static int floor1YHitBox = 668;
	public static int towerXHitBox = 1132;
	
	// Levels (Place holders)
	public static BufferedImage level1;
	public static BufferedImage level2;
	public static BufferedImage Level3;
	public static BufferedImage level4;
	public static BufferedImage level5;
	
	// Gamestate
	public static int gameState = 0;
	
	public JumpDude() {
		setPreferredSize(new Dimension(1280, 720));
		setBackground(new Color(255, 255, 255));
		
		try {
			// Initialize Variables
			menu = ImageIO.read(new File("menu.png"));
			level1 = ImageIO.read(new File("S1.1.jpg"));
			playerRight = ImageIO.read(new File("Character Sprite1.png"));
			playerLeft = ImageIO.read(new File("Character Sprite3.png"));
			player2 = ImageIO.read(new File("Character Sprite2.png"));
			jumpIndicator = ImageIO.read(new File("jumpIndicator2.png"));
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
		if(!jump) {
			// Horizontal In-Air  Movement
			playerX = playerX + 5 * direction;
			playerXHitBox = playerXHitBox + 5 * direction;
			
			// Vertical In-Air Movement
			playerY += fallingSpeed;
			player2YHitBox += fallingSpeed;
			fallingSpeed += 1;
			if(playerY >= height && player2YHitBox >= height && playerY != floor1YHitBox && player2YHitBox != floor1YHitBox && playerX != floor1XHitBox && playerXHitBox != floor1XHitBox) {
				playerY = height;
				player2YHitBox = height;
				jump = true;
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
			// Draw the image of the level
			g.drawImage(level1, 0, 0, null);
			
			g.drawRect(floor1XHitBox, floor1YHitBox, 1280, 52);
			// Jumping animations
			if(jump) {
				g.drawImage(playerRight, playerX, playerY, null);
				
				// Draw hitboxes for everything in the level
				g.drawRect(playerXHitBox, playerYHitBox, playerRight.getWidth(), playerRight.getHeight());
				
				// If going left, draw the player facing left
				if(speed < 0) {
					g.drawImage(playerLeft, playerX, playerY, null);
				}
			}
			
			
			g.drawImage(jumpIndicator, 0, 0, null);
			if(!jump) {
				g.drawImage(player2, playerX, playerY, null);
				g.drawRect(playerXHitBox, player2YHitBox, playerRight.getWidth(), playerRight.getHeight());
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
	public void keyPressed(KeyEvent e) {
		// If menu screen 
		if(gameState == 0) {
			if(e.getKeyChar() == ' ') {
				gameState = 1;
			}
		}
		else if(gameState == 1) {
			
			if(e.getKeyChar() == ' ') {
				if(jump) {
					fallingSpeed = 0;
					fallingSpeed -= 20;
					jump = false;
					
					
				}
			}
			// If 1 is pressed
			if(e.getKeyCode() == 49) {
				height = 500;
			}
			// If 2 is pressed
			if(e.getKeyCode() == 50) {
				height += 100;
			}
			// If 3 is pressed
			if(e.getKeyCode() == 51) {
				height += 200;
			}
			// If right arrow key is pressed
			if(e.getKeyCode() == 39) {
				speed = 5;
				direction = 1;
				playerX += speed;
				playerXHitBox += speed;
			}
			// If left arrow key is pressed
			if(e.getKeyCode() == 37) {
				speed = -5;
				direction = -1;
				playerX += speed;
				playerXHitBox += speed;
			}
			
		}
		
	}

	public static boolean collision(int playerX, int playerY, int XHitBox, int YHitBox, int gameState) {
		int[] collisionArrX = {};
		int[] collisionArrY = {};
		if(playerY > 545) {
			return true;
		}
		return false;
	}
	
	// Gets the angle of a line using 2 points
	// To check for slope direction
	public static double getAngle(int x1, int y1, int x2, int y2) {
		return Math.atan2(y2-y1, x2-x1) * 180 / Math.PI;
	}
	
	public static float slope(float x1, float x2, float y1, float y2) {
		return (y2 - y1) / (x2 - x1);
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
