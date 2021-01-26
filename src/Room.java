import java.util.HashMap;
import java.util.Map;

class Room {
    private String roomTitle;
    private String description;
    private Ghost roomEvidence;
    Map<String, Room> roomExits = new HashMap<>();
    String roomItems;

    public Room(String title) {
        this.roomTitle = title;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Ghost getRoomEvidence() {
        return roomEvidence;
    }

    public void setRoomEvidence(Ghost roomEvidence) {
        this.roomEvidence = roomEvidence;
    }

    public Room(String description, String ghostTracks) {
        this.description = description;

    }

}
