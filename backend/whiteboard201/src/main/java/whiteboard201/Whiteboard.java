package whiteboard201;

import java.sql.Timestamp;

public class Whiteboard {
	
	private int whiteboardId;
	private String username;
	private Timestamp updatedAt;
	
	public Whiteboard() {
		// TODO Auto-generated constructor stub
	}
	public Whiteboard(int id, String uname, Timestamp updated) {
		whiteboardId = id;
		username = uname;
		updatedAt = updated;
	}

	public int getWhiteboardId() {
		return whiteboardId;
	}

	public void setWhiteboardId(int whiteboardId) {
		this.whiteboardId = whiteboardId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
