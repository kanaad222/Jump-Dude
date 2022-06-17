// ICS3U By Kanaad and Lionel (99% Kanaad 1% Lionel)
// Jump Dude
// I hate Collision
// Lionel do your work



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

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
	
	//public static Box playerRect;
	public static Scanner s;
	
	
	// Player Stats
	public static int playerX = 100;
	public static int playerY = 550;
	public static int height = 560;
	public static boolean jump = true;
	public static int jumpLength = 5;
	public static int numberOfJumps = 0;
	public static int[] leaderboardScores = new int[3];
	public static int fallingSpeed = 0;
	public static int speed = 0;
	// 1 for right and -1 for left
	public static int direction = 1;
	
	// Hitboxes
	public static int playerXHitBox = playerX;
	public static int playerYHitBox = playerY;
	public static int player2YHitBox = playerY;
	
	// Levels (Place holders)
	public static BufferedImage level1;
	public static BufferedImage level2;
	public static BufferedImage Level3;
	
	// Probably not making these
	// public static BufferedImage level4;
	// public static BufferedImage level5;
	
	// Gamestate
	// 0 is main screen
	// 1 is level 1
	// 2 is level 2
	// 3 is level 3
	// 4 is the about page
	public static int gameState = 0;
	// private Image image;
	
	
	public JumpDude() {
		setPreferredSize(new Dimension(1280, 720));
		setBackground(new Color(255, 255, 255));
		
		//playerRect = new Box(playerX, playerY, playerRight.getHeight(), playerRight.getWidth());
		// Image image;
		//platform1 = new Box();
		
		try {
			// Initialize Variables
			menu = ImageIO.read(new File("menu.png"));
			level1 = ImageIO.read(new File("S1.1.jpg"));
			level2 = ImageIO.read(new File("level2placeholder.png"));
			playerRight = ImageIO.read(new File("Character Sprite1.png"));
			playerLeft = ImageIO.read(new File("Character Sprite3.png"));
			player2 = ImageIO.read(new File("Character Sprite2.png"));
			player4 = ImageIO.read(new File("Character Sprite4.png"));
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
	
	public static void main(String[] args) throws IOException{
		JFrame gameWindow = new JFrame("Jump Dude");
		JumpDude gamePanel = new JumpDude();
		gameWindow.add(gamePanel);
		gameWindow.pack();
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameWindow.setVisible(true);
	}
	
	public static void updatePlayer() {
		// If not jumping or not colliding with anything (easy implementation of gravity)
		if(!jump || (!checkCollision(playerX, playerY, playerRight.getHeight(), playerRight.getWidth()) && playerY < 560)) {
			// Horizontal In-Air  Movement
			// Cannot jump straight up (mechanic)
			playerX = playerX + jumpLength * direction;
			
			// Vertical In-Air Movement
			playerY += fallingSpeed;
			player2YHitBox += fallingSpeed;
			fallingSpeed += 1;
			if(checkCollision(playerX, playerY, playerRight.getHeight(), playerRight.getWidth())) { //&& playerY != floor1YHitBox && player2YHitBox != floor1YHitBox && playerX != floor1XHitBox && playerXHitBox != floor1XHitBox) {
				//playerY = height;
//				if(!(playerY > 560)) {
//					direction *= -1;	
//				}
				
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
			//g.fillRect(788, 492, 178, 52);
			g.setColor(new Color(255, 255, 255));
			g.setFont(new Font("Courier", Font.BOLD, 30));
			g.drawString("Number of Jumps: " + numberOfJumps, 850, 64);
			
			// g.drawRect(floor1XHitBox, floor1YHitBox, 1280, 52);
			// Jumping animations
			if(jump) {
				g.drawImage(playerRight, playerX, playerY, null);
				
				// Draw hitboxes for everything in the level
				//g.drawRect(playerXHitBox, playerYHitBox, playerRight.getWidth(), playerRight.getHeight());
				
				
				// If going left, draw the player facing left
				if(speed < 0) {
					g.drawImage(playerLeft, playerX, playerY, null);
				}
			}
			
			
			g.drawImage(jumpIndicator, 0, 0, null);
			if(!jump) {
				if(direction == 1) {
					g.drawImage(player2, playerX, playerY, null);
					// g.drawRect(playerXHitBox, player2YHitBox, playerRight.getWidth(), playerRight.getHeight());
				}
				if(direction == -1) {
					g.drawImage(player4, playerX, playerY, null);
				}
				
				
			}
			if(playerX >= 874 && playerY <= 103) {
				gameState = 2;
			}
			
		}
		if(gameState == 2) {
			g.drawImage(level2, 0, 0, null);
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
					//if(!checkCollision(playerX, playerY, playerRight.getHeight(), playerRight.getWidth())) {
					jump = false;
					fallingSpeed -= 20;
					numberOfJumps++;
//					while(checkCollision(playerX, playerY, playerRight.getHeight(), playerRight.getWidth()) && jump) {
//						fallingSpeed -= 20;
//					}
					
				}
			}
			// If 1 is pressed
			if(e.getKeyCode() == 49) {
				jumpLength = 7;
			}
			// If 2 is pressed
			if(e.getKeyCode() == 50) {
				jumpLength = 8;
			}
			// If 3 is pressed
			if(e.getKeyCode() == 51) {
				jumpLength = 9;
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
	
	// Checks for collision
	public static boolean checkCollision(int playerX, int playerY, int playerHeight, int playerWidth) {
		// Get the attributes
		int playerTopLeftY = playerY;
		int playerTopRightY = playerY + playerWidth;
		int playerBottomLeftY = playerY + playerHeight;
		int playerBottomRightY = playerY + playerHeight + playerWidth;
		
		int playerTopLeftX = playerX;
		int playerTopRightX = playerX + playerWidth;
		int playerBottomLeftX = playerX + playerHeight;
		int playerBottomRightX = playerX + playerHeight + playerWidth;
		
		if(gameState == 1) {
			
			// Platform dimensions
			//                 x, y, width, height
			int[] platform1 = {788, 543, 178, 52};
			int[] platform2 = {430, 450, 178, 52};
			int[] platform3 = {48, 310, 178, 52};
			int[] platform4 = {429, 175, 178, 52};
			int[] platform5 = {790, 175, 178, 52};
			
			
			if(((playerY > 560) || (playerTopLeftY > platform1[1] + platform1[3] && playerTopRightY > platform1[1] + platform1[3]) 
				&& (playerTopLeftY > platform1[1] + platform1[3] && playerTopRightY > platform1[1] + platform1[3] && playerTopLeftY > platform1[1] + platform1[3] + platform1[2] && playerTopRightY > platform1[1] + platform1[3] + platform1[2])
				&& (playerBottomLeftY > platform1[1] + platform1[3] && playerBottomRightY > platform1[1] + platform1[3] && playerBottomLeftY > platform1[1] + platform1[3] + platform1[2] && playerBottomRightY > platform1[1] + platform1[3] + platform1[2])
				|| !(playerBottomLeftY < platform1[1] && playerBottomRightY < platform1[1])
				&& (playerTopRightX > platform1[0] && playerBottomRightX > platform1[0] && playerBottomLeftX > platform1[0] && playerTopLeftX < platform1[0] + platform1[2])) ) {
						return true;
					}
			if(((playerTopLeftY > platform2[1] + platform2[3] && playerTopRightY > platform2[1] + platform2[3]) 
					&& (playerTopLeftY < platform2[1] + platform2[3] && playerTopRightY < platform2[1] + platform2[3] && playerTopLeftY > platform2[1] + platform2[3] + platform2[2] && playerTopRightY > platform2[1] + platform2[3] + platform2[2])
					&& (playerBottomLeftY > platform2[1] + platform2[3] && playerBottomRightY > platform2[1] + platform2[3] && playerBottomLeftY > platform2[1] + platform2[3] + platform2[2] && playerBottomRightY > platform2[1] + platform2[3] + platform2[2])
					|| !(playerBottomLeftY < platform2[1] && playerBottomRightY < platform2[1])
					&& (playerTopRightX > platform2[0] && playerBottomRightX > platform2[0] && playerBottomLeftX > platform2[0] && playerTopLeftX < platform2[0] + platform2[2]))){
						return true;
					}
			if(( (playerTopLeftY > platform3[1] + platform3[3] && playerTopRightY > platform3[1] + platform3[3]) 
					&& (playerTopLeftY < platform3[1] + platform3[3] && playerTopRightY < platform3[1] + platform3[3])
					|| !(playerBottomLeftY < platform3[1] && playerBottomRightY < platform3[1])
					&& (playerTopRightX > platform3[0] && playerBottomRightX > platform3[0] && playerBottomLeftX > platform3[0] && playerTopLeftX < platform3[0] + platform3[2]))){
						return true;
					}
			if(((playerTopLeftY > platform4[1] + platform4[3] && playerTopRightY > platform4[1] + platform4[3]) 
					&& (playerTopLeftY < platform4[1] + platform4[3] && playerTopRightY < platform4[1] + platform4[3])
					|| !(playerBottomLeftY < platform4[1] && playerBottomRightY < platform4[1])
					&& (playerTopRightX > platform4[0] && playerBottomRightX > platform4[0] && playerBottomLeftX > platform4[0] && playerTopLeftX < platform4[0] + platform4[2]))){
						return true;
					}
			else if(((playerTopLeftY > platform5[1] + platform5[3] && playerTopRightY > platform5[1] + platform5[3]) 
					&& (playerTopLeftY < platform5[1] + platform5[3] && playerTopRightY < platform5[1] + platform5[3])
					|| !(playerBottomLeftY < platform5[1] && playerBottomRightY < platform5[1])
					&& (playerTopRightX > platform5[0] && playerBottomRightX > platform5[0] && playerBottomLeftX > platform5[0] && playerTopLeftX < platform5[0] + platform5[2]))){
						return true;
					}
			

			
//			else if((playerY > 560) || (playerTopLeftY > platform2[1] + platform2[3] && playerTopRightY > platform2[1] + platform2[3]) 
//					|| (playerTopLeftY < platform2[1] + platform2[3] && playerTopRightY < platform2[1] + platform2[3])
//					&& !(playerBottomLeftY < platform2[1] && playerBottomRightY < platform2[1])
//					&& (playerTopRightX > platform2[0] && playerBottomRightX > platform2[0] && playerTopLeftX < platform2[0] + platform2[2])) {
//					return true;
//				}
		}
		return false;
	}

	// Unfinished Methods
	
	
	// Gets the angle of a line using 2 points
	// To check for slope direction
	public static double getAngle(int x1, int y1, int x2, int y2) {
		// atan2 is the inverse tangent
		// Difference between atan1 and atan2 is the number of arguments
		return Math.atan2(y2-y1, x2-x1) * 180 / Math.PI;
	}
	
	// Returns the slope
	public static float slope(float x1, float x2, float y1, float y2) {
		// Simple slope formula
		return (y2 - y1) / (x2 - x1);
	}
	
	
	
	// Unused methods

	@Override
	public void keyReleased(KeyEvent e) {
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
