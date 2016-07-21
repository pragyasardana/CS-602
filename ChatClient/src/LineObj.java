import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;


public class LineObj implements Serializable {
	ArrayList<Line> lines;
	public LineObj(){
		lines=new ArrayList<Line>();
	}
	public void addLine(Line l){
		lines.add(l);
	}
	public ArrayList<Line> retLines(){
		return lines;
	}
}
