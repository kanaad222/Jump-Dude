import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
 
public class JumpDude extends JPanel implements Runnable, KeyListener{ 
 
	// Images
	public static BufferedImage menu;
	public static BufferedImage rules;
	public static BufferedImage youWin;
	public static BufferedImage playerRight;
	public static BufferedImage playerLeft;
	public static BufferedImage player2;
	public static BufferedImage player3;
	public static BufferedImage player4;
	public static BufferedImage jumpIndicator;
	public static BufferedImage jumpIndicator2;
	public static BufferedImage jumpIndicator3;
	public static BufferedImage trophy;
	
	public static File jumpSound;
	public static AudioInputStream audioStream;
	public static Clip clip;
	public static FloatControl gainControl;
	
	public static Scanner readFile;
	public static PrintWriter outputFile;
 
 
	// Player Stats
	public static int playerX = 100;
	public static int playerX2 = 1052;
	public static int playerX3 = 1098;
	public static int playerY = 550;
	public static int playerY2 = 500;
	public static int playerY3 = 471;
	public static int lives = 3;
	public static boolean infiniteLives = false;
	public static int height = 560;
	public static boolean jump = true;
	public static int jumpLength = 7;
	public static int numberOfJumps = 0;
	public static int[] leaderboardScores = new int[0];
	public static int[] top3 = new int[3];
	public static int fallingSpeed = 0;
	public static int speed = 0;
	public static boolean collision = false;
	// 1 for right and -1 for left
	public static int direction = 1;
 
	// Hitboxes
	// gamestate 1
	public static rect player = new rect(playerX, playerY, 45, 110);
	public static rect floor = new rect(0, 668, 1280, 52);
	public static rect rect1 = new rect(788, 491, 178, 52);
	public static rect rect2 = new rect(430, 398, 178, 52);
	public static rect rect3 = new rect(47, 268, 178, 52);
	public static rect rect4 = new rect(429, 142, 178, 52);
	public static rect rect5 = new rect(789, 141, 178, 52);
	
	// gamestate 2
	public static rect rect6 = new rect(913, 643, 367, 77);
	public static rect rect7 = new rect(183, 550, 595, 26);
	public static rect rect8 = new rect(0, 435, 181, 26);
	public static rect rect9 = new rect(186, 276, 212, 26);
	public static rect rect10 = new rect(573, 273, 195, 26);
	public static rect rect11 = new rect(871, 175, 234, 26);
	
	// gamestate 3
	public static rect rect12 = new rect(1089, 629, 52, 57);
	public static rect rect13 = new rect(1143, 581, 21, 45);
	public static rect rect14 = new rect(787, 686, 416, 34);
	public static rect rect15 = new rect(576, 580, 161, 40);
	public static rect rect16 = new rect(360, 472, 161, 40);
	public static rect rect17 = new rect(582, 314, 161, 40);
	public static rect rect18 = new rect(1014, 311, 161, 40);
	public static rect rect19 = new rect(1202, 189, 161, 40);
	public static rect rect20 = new rect(789, 78, 282, 50);
	public static rect rect21 = new rect(453, 78, 159, 43);
	public static rect rect22 = new rect(28, 130, 277, 45);
	public static rect rect23 = new rect(800, 311, 161, 40);
	
 
	// Levels (Place holders)
	public static BufferedImage level1;
	public static BufferedImage level2;
	public static BufferedImage level3;
 
	// Probably not making these
	// public static BufferedImage level4;
	// public static BufferedImage level5;
 
	// Gamestate
	// 0 is main screen
	// 1 is level 1
	// 2 is level 2
	// 3 is level 3
	// 4 is the about page
	// 5 is the you win screen
	public static int gameState = 0;
 
 
	public JumpDude() {
		setPreferredSize(new Dimension(1280, 720));
		setBackground(new Color(255, 255, 255));
 
 
		try {
			// Import Variables
			menu = ImageIO.read(new File("menu.png"));
			rules = ImageIO.read(new File("rules.png"));
			youWin = ImageIO.read(new File("youWin.png"));
			level1 = ImageIO.read(new File("S1.1.jpg"));
			level2 = ImageIO.read(new File("Background_S1.2.png"));
			level3 = ImageIO.read(new File("Stage_3.png"));
			playerRight = ImageIO.read(new File("Character Sprite1.png"));
			playerLeft = ImageIO.read(new File("Character Sprite3.png"));
			player2 = ImageIO.read(new File("Character Sprite2.png"));
			player4 = ImageIO.read(new File("Character Sprite4.png"));
			jumpIndicator = ImageIO.read(new File("jumpIndicator2.png"));
			jumpIndicator2 = ImageIO.read(new File("JumpBar_2.png"));
			jumpIndicator3 = ImageIO.read(new File("JumpBar_3.png"));
			trophy = ImageIO.read(new File("trophy.png"));
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
 
	public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException{
		JFrame gameWindow = new JFrame("Jump Dude");
		JumpDude gamePanel = new JumpDude();
		gameWindow.add(gamePanel);
		gameWindow.pack();
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameWindow.setVisible(true);
		
		// Sound
		jumpSound = new File("jumpSound.wav");
		audioStream = AudioSystem.getAudioInputStream(jumpSound);
		clip = AudioSystem.getClip();
		clip.open(audioStream);
		
		// Text file
		outputFile = new PrintWriter(new FileWriter("highscores.txt", true));
		
		
	}
	
	// gets the top 3 lowest values from the file
	public static void getTop3() throws FileNotFoundException {
		readFile = new Scanner(new File("highscores.txt"));
		int first = 1000000000;
		int second = 1000000000;
		int third = 1000000000;
		
		while(readFile.hasNextLine()) {
			int curLine = Integer.parseInt(readFile.nextLine());
			if(curLine < first && curLine < second && curLine < third) {
				top3[0] = curLine;
				first = curLine;
			}
			if(curLine < second && curLine < third && curLine > first) {
				top3[1] = curLine;
				second = curLine;
			}
			if(curLine < third && curLine > second && curLine > first) {
				top3[2] = curLine;
				third = curLine;
			}
		}

	}
	
	// outputs the highscores on the screen
	public static void outputHighscores(Graphics g) throws FileNotFoundException {
		getTop3();
		// Converts to string
		g.drawString(String.valueOf(top3[0]), 610, 160);
		g.drawString(String.valueOf(top3[1]), 610, 200);
		g.drawString(String.valueOf(top3[2]), 610, 240);
		readFile.close();
	}
 
	// Physics
	public static void updatePlayer() {
		
		if(gameState == 1) {
			player = new rect(playerX, playerY, 45, 110);
			collision();
			// If not jumping or not colliding with anything (easy implementation of gravity)
	 
			if(!jump && !collision) { 
				// Horizontal In-Air  Movement
				// Cannot jump straight up (mechanic)
				playerX = playerX + jumpLength * direction;
	 
				// Vertical In-Air Movement
				playerY += fallingSpeed;
	 
				fallingSpeed += 1;
	 
			}
			// If not collision
			else if(!collision){
				playerY += fallingSpeed;
	 
				fallingSpeed += 1;
			}else {
	 
				jump = true;
	 
	 
			}
		}
		if(gameState == 2) {
			player = new rect(playerX2, playerY2, 45, 110);
			collision();
			jump = false;
			// If not jumping or not colliding with anything (easy implementation of gravity)
	 
			if(!jump && !collision) { 
				// Horizontal In-Air  Movement
				// Cannot jump straight up (mechanic)
				playerX2 = playerX2 + jumpLength * direction;
	 
				// Vertical In-Air Movement
				playerY2 += fallingSpeed;
	 
				fallingSpeed += 1;
	 
			}
			else if(!collision){
				playerY2 += fallingSpeed;
	 
				fallingSpeed += 1;
			}else {
	 
				jump = true;
	 
	 
			}
		}
		if(gameState == 3) {
			player = new rect(playerX3, playerY3, 45, 110);
			collision();
			jump = false;
			// If not jumping or not colliding with anything (easy implementation of gravity)
	 
			if(!jump && !collision) { 
				// Horizontal In-Air  Movement
				// Cannot jump straight up (mechanic)
				playerX3 = playerX3 + jumpLength * direction;
	 
				// Vertical In-Air Movement
				playerY3 += fallingSpeed;
	 
				fallingSpeed += 1;
	 
			}
			else if(!collision){
				playerY3 += fallingSpeed;
	 
				fallingSpeed += 1;
			}else {
	 
				jump = true;
	 
	 
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
			g.setColor(new Color(255, 255, 255));
			g.setFont(new Font("Courier", Font.BOLD, 30));
			g.drawString("Number of Jumps: " + numberOfJumps, 850, 64);
			g.drawString("Lives: " + lives, 1027, 100);
			g.drawString("Infinite Lives: " + infiniteLives, 510, 40);
 
			// Jumping animations
			if(jump) {
				g.drawImage(playerRight, playerX, playerY, null);
 
 
				// Draw hitboxes for everything in the level
				//g.drawRect(playerXHitBox, playerYHitBox, playerRight.getWidth(), playerRight.getHeight());
 
 
				// If going left, draw the player facing left
				if(speed < 0 || direction == -1) {
					g.drawImage(playerLeft, playerX, playerY, null);
				}
			}
 
			if(jumpLength == 7) {
				g.drawImage(jumpIndicator, 24, 24, null);
			}
			if(jumpLength == 8) {
				g.drawImage(jumpIndicator2, 24, 24, null);
			}
			if(jumpLength == 9) {
				g.drawImage(jumpIndicator3, 24, 24, null);
			}
			
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
				gameState++;
			}
 
		}
		if(gameState == 2) {
			//jump = true;
			collision = false;
			
			g.drawImage(level2, 0, 0, null);
			player = new rect(playerX2, playerY2, 45, 110);
			g.setColor(new Color(255, 255, 255));
			g.setFont(new Font("Courier", Font.BOLD, 30));
			g.drawString("Number of Jumps: " + numberOfJumps, 850, 64);
			g.drawString("Lives: " + lives, 1027, 100);
			g.drawString("Infinite Lives: " + infiniteLives, 510, 40);
			
			if(lives <= 0 && infiniteLives == false) {
				gameState = 0;
				lives = 3;
			}
			
			
			
			// Jumping animations
			if(jump) {
				g.drawImage(playerRight, playerX2, playerY2, null);
 
 
				// Draw hitboxes for everything in the level
				//g.drawRect(playerXHitBox, playerYHitBox, playerRight.getWidth(), playerRight.getHeight());
 
 
				// If going left, draw the player facing left
				if(speed < 0 || direction == -1) {
					g.drawImage(playerLeft, playerX2, playerY2, null);
				}
			}
 
 
			if(jumpLength == 7) {
				g.drawImage(jumpIndicator, 24, 24, null);
			}
			if(jumpLength == 8) {
				g.drawImage(jumpIndicator2, 24, 24, null);
			}
			if(jumpLength == 9) {
				g.drawImage(jumpIndicator3, 24, 24, null);
			}
			if(!jump) {
				if(direction == 1) {
					g.drawImage(player2, playerX2, playerY2, null);
					// g.drawRect(playerXHitBox, player2YHitBox, playerRight.getWidth(), playerRight.getHeight());
				}
				if(direction == -1) {
					g.drawImage(player4, playerX2, playerY2, null);
				}
			}
			// 505, 498
			// Spikes
			if(playerY2 > 723) {
				playerX2 = 1052;
				playerY2 = 500;
				lives--;
			}
			if((playerX2 <= 512 || playerX2 + 45 <= 512) && (playerX2 >= 502 || playerX2 + 45 >= 502) && (playerY2 <= 537 || playerY2 + 110 <= 537) && (playerY2 >= 439 || playerY2 + 110 >= 439)) {
				playerX2 = 1052;
				playerY2 = 500;
				lives--;
			}
			
			if((playerX2 <= 469 || playerX2 + 45 <= 469) && (playerX2 >= 461 || playerX2 + 45 >= 461) && (playerY2 <= 537 || playerY2 + 110 <= 537) && (playerY2 >= 439 || playerY2 + 110 >= 439)) {
				playerX2 = 1052;
				playerY2 = 500;
				lives--;
			}
			
			// 184, 274
			if((playerX2 <= 656 || playerX2 + 45 <= 656) && (playerX2 >= 650 || playerX2 + 45 >= 650) && (playerY2 <= 274 || playerY2 + 110 <= 274) && (playerY2 >= 184 || playerY2 + 110 >= 184)) {
				playerX2 = 1052;
				playerY2 = 500;
				lives--;
			}
			
			// 687, 681
			if((playerX2 <= 687 || playerX2 + 45 <= 687) && (playerX2 >= 681 || playerX2 + 45 >= 681) && (playerY2 <= 274 || playerY2 + 110 <= 274) && (playerY2 >= 184 || playerY2 + 110 >= 184)) {
				playerX2 = 1052;
				playerY2 = 500;
				lives--;
			}
			
			if((playerX2 <= 870 || playerX2 + 45 <= 870) && (playerX2 >= 865 || playerX2 + 45 >= 865) && (playerY2 == 201 || playerY2 + 110 == 201)) {
				playerX2 = 1052;
				playerY2 = 500;
				lives--;
			}
			if(playerX2 >= 1038 && playerY2 <= 173) {
				gameState = 3;
			}
		}
		
		if(gameState == 3) {
			//jump = true;
			collision = false;
			
			// Draw the image of the level
			g.drawImage(level3, 0, 0, null);
			g.setColor(new Color(0, 0, 0));
			g.setFont(new Font("Courier", Font.BOLD, 30));
			g.drawString("Number of Jumps: " + numberOfJumps, 850, 64);
			g.drawString("Lives: " + lives, 1027, 100);
			g.drawString("Infinite Lives: " + infiniteLives, 510, 40);
 
			
			// Jumping animations
			if(jump) {
				g.drawImage(playerRight, playerX3, playerY3, null);
 
 
				// Draw hitboxes for everything in the level
				//g.drawRect(playerXHitBox, playerYHitBox, playerRight.getWidth(), playerRight.getHeight());
 
 
				// If going left, draw the player facing left
				if(speed < 0 || direction == -1) {
					g.drawImage(playerLeft, playerX3, playerY3, null);
				}
			}
 
 
			if(jumpLength == 7) {
				g.drawImage(jumpIndicator, 24, 24, null);
			}
			if(jumpLength == 8) {
				g.drawImage(jumpIndicator2, 24, 24, null);
			}
			if(jumpLength == 9) {
				g.drawImage(jumpIndicator3, 24, 24, null);
			}
			if(!jump) {
				if(direction == 1) {
					g.drawImage(player2, playerX3, playerY3, null);
					// g.drawRect(playerXHitBox, player2YHitBox, playerRight.getWidth(), playerRight.getHeight());
				}
				if(direction == -1) {
					g.drawImage(player4, playerX3, playerY3, null);
				}
 
 
			}
			if(playerY3 > 730) {
				playerX3 = 1098;
				playerY3 = 471;
				lives--;
			}
			
			
			if(playerX3 < 61 && playerY3 < 128) {
				gameState = 5;
			}
 
		}
		
		if(gameState == 4) {
			g.drawImage(rules, 0, 0, null);
		}
		if(gameState == 5) {
			g.drawImage(youWin, 0, 0, null);
			outputFile.println(numberOfJumps);
			System.out.println(numberOfJumps);
			outputFile.close();
		}
		if(gameState == 6) {
			g.setColor(new Color(0,0,0));
			
			g.fillRect(0, 0, 1280, 720);
			g.setColor(new Color(255,255,255));
			g.setFont(new Font("Courier", Font.BOLD, 50));
			g.drawString("HIGHSCORES", 490, 50);
			g.setFont(new Font("Courier", Font.BOLD, 30));
			g.drawImage(trophy, 0, 0, null);
			g.drawString("Press 'k'", 575, 700);
			try {
				outputHighscores(g);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
 
	}
 
 
 
 
	// Run method updates every frame
	public void run() {
		while(true) {
			repaint();
			updatePlayer();
			if(gameState == 5) {
				outputFile.println(numberOfJumps);
			}
			

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
		if(e.getKeyChar() == 'g') {
			infiniteLives = true;
		}
		
		// If menu screen 
		if(gameState == 0) {
			if(e.getKeyChar() == ' ') {
				gameState = 1;
			}
			if(e.getKeyChar() == 'x') {
				gameState = 4;
			}
			if(e.getKeyChar() == 'a') {
				gameState = 6;
			}
		}
		else if(gameState == 4) {
			if(e.getKeyChar() == 'f') {
				gameState = 1;
			}
		}
		else if(gameState == 5) {
			if(e.getKeyChar() == 't') {
				gameState = 0;
			}
		}
		else if(gameState == 6) {
			if(e.getKeyChar() == 'k') {
				gameState = 0;
			}
		}
		else if(gameState == 1) {
 
			System.out.println("X: " + playerX + "| Y: " + playerY);
			if(e.getKeyChar() == ' ') {
				System.out.println(jump);
				if(jump) {
					clip.setFramePosition(0);
					clip.start();
					fallingSpeed = 0;
					jump = false;
					fallingSpeed -= 20;
					numberOfJumps++;
 
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
			}
			// If left arrow key is pressed
			if(e.getKeyCode() == 37) {
				speed = -5;
				direction = -1;
				playerX += speed;
			}
 
		}
		if(gameState == 2) {
			System.out.println("X: " + playerX2 + "| Y: " + playerY2);
			if(e.getKeyChar() == ' ') {
				System.out.println(jump);
				if(jump) {
					fallingSpeed = 0;
					jump = false;
					fallingSpeed -= 20;
					numberOfJumps++;
 
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
				playerX2 += speed;

			}
			// If left arrow key is pressed
			if(e.getKeyCode() == 37) {
				speed = -5;
				direction = -1;
				playerX2 += speed;

			}
		}
		if(gameState == 3) {
			System.out.println("X: " + playerX3 + "| Y: " + playerY3);
			if(e.getKeyChar() == ' ') {
				System.out.println(jump);
				if(jump) {
					fallingSpeed = 0;
					jump = false;
					fallingSpeed -= 20;
					numberOfJumps++;
 
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
				speed = 7;
				direction = 1;
				playerX3 += speed;

			}
			// If left arrow key is pressed
			if(e.getKeyCode() == 37) {
				speed = -7;
				direction = -1;
				playerX3 += speed;

			}
		}
 
	}
 
	public static void collision() {
		Rectangle player1 = player.bounds();
 
		if(gameState == 1) {
			Rectangle platform2 = rect1.bounds();
			Rectangle platform3 = rect2.bounds();
			Rectangle platform4 = rect3.bounds();
			Rectangle platform5 = rect4.bounds();
			Rectangle platform6 = rect5.bounds();

 
			if(playerY > 557 || playerY + fallingSpeed > 557) {
				collision = true;
				playerY = 556;
			}

			else if(check(platform2, player1)) {
				 
			}
			else if(check(platform3,player1) ) {
 
			}
			else if(check(platform4, player1)) {
				
			}
			else if(check(platform5, player1)) {
				
			}
			else if(check(platform6, player1)) {
				
			}
			else {
				collision = false;
			}
		}
		if(gameState == 2) {
			Rectangle platform7 = rect6.bounds();
			Rectangle platform8 = rect7.bounds();
			Rectangle platform9 = rect8.bounds();
			Rectangle platform10 = rect9.bounds();
			Rectangle platform11 = rect10.bounds();
			Rectangle platform12 = rect11.bounds();
			
			
			if(check(platform7, player1)) {
				
			}
			else if(check(platform8, player1)) {
				
			}
			else if(check(platform9, player1)) {
				
			}
			else if(check(platform10, player1)) {
				
			}
			else if(check(platform11, player1)) {
				
			}
			else if(check(platform12, player1)) {
				
			}
			else {
				collision = false;
			}
		}
		if(gameState == 3) {
			Rectangle platform13 = rect12.bounds();
			Rectangle platform14 = rect13.bounds();
			Rectangle platform15 = rect14.bounds();
			Rectangle platform16 = rect15.bounds();
			Rectangle platform17 = rect16.bounds();
			Rectangle platform18 = rect17.bounds();
			Rectangle platform19 = rect18.bounds();
			Rectangle platform20 = rect19.bounds();
			Rectangle platform21 = rect19.bounds();
			Rectangle platform22 = rect20.bounds();
			Rectangle platform23 = rect21.bounds();
			Rectangle platform24 = rect22.bounds();
			Rectangle platform25 = rect23.bounds();
			
			if(check(platform13, player1)) {
				
			}
			else if(check(platform14, player1)) {
				
			}
			else if(check(platform15, player1)) {
				
			}
			else if(check(platform16, player1)) {
				
			}
			else if(check(platform17, player1)) {
				
			}
			else if(check(platform18, player1)) {
				
			}
			else if(check(platform19, player1)) {
				
			}
			else if(check(platform20, player1)) {
				
			}
			else if(check(platform21, player1)) {
				
			}
			else if(check(platform22, player1)) {
				
			}
			else if(check(platform23, player1)) {
				
			}
			else if(check(platform24, player1)) {
				
			}
			else if(check(platform25, player1)) {
				
			}
			else {
				collision = false;
			}
			
		}
 
	}
 
	// Unfinished Methods
	public static boolean check(Rectangle plat, Rectangle player1) {
		// Checks if it is intersecting or if it will intersect
		if(gameState == 1) {
			if((player1.intersects(plat)||(playerY + fallingSpeed + player1.height >= plat.y&&playerY+fallingSpeed <plat.y+plat.height&&(player1.x<plat.x+plat.width && player1.x+player1.width> plat.x)))   ) {
				
				// Checks to see if going up or down
				// > 0 is going down
				if(fallingSpeed> 0) {
					// if player y intersects from the top then move it to the top of the platform and collision is true
					if(playerY - fallingSpeed + player1.height < plat.y) {
						collision = true;
						playerY = plat.y - player1.height - 1;
						return true;
					
					}
					// if player hits the side then reverse direction
					else {
	 
						playerX = plat.x + ((direction == -1) ? plat.width: -player1.width) - direction;
						direction = -direction;
					}
				// < 0 is going up
				}else if(fallingSpeed < 0) {
					// Checks if it enters from the bottom and reverse the falling speed
					if(playerY - fallingSpeed > plat.y + plat.height) {
						playerY = plat.y + plat.height + 1;
						fallingSpeed = -fallingSpeed;
					}
					else {
	 
						playerX = plat.x + ((direction == -1) ? plat.width: -player1.width) - direction;
						direction = -direction;
					}
				}
				return false;
	 
			}
			return false;
		}
		if(gameState == 2) {
			if((player1.intersects(plat)||(playerY2 + fallingSpeed + player1.height >= plat.y&&playerY2+fallingSpeed <plat.y+plat.height&&(player1.x<plat.x+plat.width && player1.x+player1.width> plat.x)))   ) {
				
				// Checks to see if going up or down
				// > 0 is going down
				if(fallingSpeed> 0) {
					// if player y intersects from the top then move it to the top of the platform and collision is true
					if(playerY2 - fallingSpeed + player1.height < plat.y) {
						collision = true;
						playerY2 = plat.y - player1.height - 1;
						return true;
					
					}
					// if player hits the side then reverse direction
					else {
	 
						playerX2 = plat.x + ((direction == -1) ? plat.width: -player1.width) - direction;
						direction = -direction;
					}
				// < 0 is going up
				}else if(fallingSpeed < 0) {
					// Checks if it enters from the bottom and reverse the falling speed
					if(playerY2 - fallingSpeed > plat.y + plat.height) {
						playerY2 = plat.y + plat.height + 1;
						fallingSpeed = -fallingSpeed;
					}
					else {
	 
						playerX2 = plat.x + ((direction == -1) ? plat.width: -player1.width) - direction;
						direction = -direction;
					}
				}
				return false;
	 
			}
			return false;
		}
		if(gameState == 3) {
			if((player1.intersects(plat)||(playerY3 + fallingSpeed + player1.height >= plat.y&&playerY3+fallingSpeed <plat.y+plat.height&&(player1.x<plat.x+plat.width && player1.x+player1.width> plat.x)))) {
				
				// Checks to see if going up or down
				// > 0 is going down
				if(fallingSpeed> 0) {
					// if player y intersects from the top then move it to the top of the platform and collision is true
					if(playerY3 - fallingSpeed + player1.height < plat.y) {
						collision = true;
						playerY3 = plat.y - player1.height - 1;
						return true;
					
					}
					// if player hits the side then reverse direction
					else {
	 
						playerX3 = plat.x + ((direction == -1) ? plat.width: -player1.width) - direction;
						direction = -direction;
					}
				// < 0 is going up
				}else if(fallingSpeed < 0) {
					// Checks if it enters from the bottom and reverse the falling speed
					if(playerY3 - fallingSpeed > plat.y + plat.height) {
						playerY3 = plat.y + plat.height + 1;
						fallingSpeed = -fallingSpeed;
					}
					else {
	 
						playerX3 = plat.x + ((direction == -1) ? plat.width: -player1.width) - direction;
						direction = -direction;
					}
				}
				return false;
	 
			}
			return false;
		}
		return false;
		
	}
	
	
	public static int[] extendIntArr(int[] arr, int n) {
		int[] arr2 = new int[arr.length + 1];
		
		for(int i = 0; i < arr.length; i++) {
			arr2[i] = arr[i];
		}
		arr2[arr2.length-1] = n;
		return arr2;
	}
 
	// Unused methods
 
	@Override
	public void keyReleased(KeyEvent e) {
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}
 
 
}
