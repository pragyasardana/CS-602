import java.awt.Color;
import java.io.Serializable;

public class Line implements Serializable {
	int startx, starty, endx, endy;
	Color drawColor;

	public Line() {
		setStartX(startx);
		setStartY(starty);
		setEndX(endx);
		setEndY(endy);
		setColor(drawColor);
	}

	public void setColor(Color drawColor) {
		this.drawColor = drawColor;
	}

	public Color getColor() {
		return drawColor;
	}

	public Line(int startx, int starty, int endx, int endy, Color c) {
		setStartX(startx);
		setStartY(starty);
		setEndX(endx);
		setEndY(endy);
		setColor(c);
	}

	public void setStartX(int startx) {
		this.startx = startx;
	}

	public void setStartY(int starty) {
		this.starty = starty;
	}

	public void setEndX(int endx) {
		this.endx = endx;
	}

	public void setEndY(int endy) {
		this.endy = endy;
	}

	public int getStartX() {
		return startx;
	}

	public int getStartY() {
		return starty;
	}

	public int getEndX() {
		return endx;
	}

	public int getEndY() {
		return endy;
	}
}