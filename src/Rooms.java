import java.util.HashMap;
import java.util.Map;

class Rooms {
    private String roomTitle;
    private String description;
    private Ghosts roomEvidence;
    Map<String, Rooms> roomExits = new HashMap<>();
    String roomItems;

    public Rooms(String title) {
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

    public Ghosts getRoomEvidence() {
        return roomEvidence;
    }

    public void setRoomEvidence(Ghosts roomEvidence) {
        this.roomEvidence = roomEvidence;
    }

    public Rooms(String description, String ghostTracks) {
        this.description = description;

    }

}
