import java.awt.Rectangle;
public class rect {
	int x;
	int y;
	int width;
	int height;
	
	public rect(int startX, int startY, int startWidth, int startHeight) {
		x = startX;
		y = startY;
		width = startWidth;
		height = startHeight;
	}
	
	public Rectangle bounds() {
		return (new Rectangle(x, y, width, height));
	}
}
