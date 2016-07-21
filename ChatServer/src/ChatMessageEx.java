import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.plaf.SeparatorUI;


public class ChatMessageEx extends ChatMessage implements Serializable{
	public Color colour;
	public String toName;
	public ArrayList<String> userList;
	public ChatMessageEx(){
		super();
		userList=new ArrayList<String>();
	}
	public void setColour(Color c){
		colour=c;
	}
	public Color getColor(){
		return colour;
	}
	public void setToName(String toName){
		this.toName=toName;
	}
	public String getToName(){
		return this.toName;
	}
	public void setUserList(ArrayList<String> n){
		this.userList=n;
	}
	public ArrayList<String> getUserList(){
		return userList;
	}
	public ChatMessageEx copy(){
		ChatMessageEx temp=new ChatMessageEx();
		temp.setName(this.getName());
		temp.setMessage(this.getMessage());
		temp.setColour(this.getColor());
		temp.setToName(this.getToName());
		return temp;
	}
}
