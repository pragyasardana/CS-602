import java.io.Serializable;

public class Control implements Serializable{
	private String command,data;
	public void setCommand(String cmd) {
		command = cmd;
	}
	public String getCommand(){
		return command;
	}
	public void setData(String d){
		data=d;
	}
	public String getData(){
		return data;
	}
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}

}