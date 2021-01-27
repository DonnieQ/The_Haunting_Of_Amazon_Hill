import java.util.ArrayList;
import java.util.List;

class Player {
   private final String name;
   private List<String> journal = new ArrayList<>();

    Player(String name) {
        this.name = name;
    }

     String getName() {
        return name;
    }

     List<String> getJournal() {
        return journal;
    }

     void setJournal(List<String> journal) {
        this.journal = journal;
    }

    @Override
    public String toString() {
        return getName() + "\'s" + " journal currently shows these items: " + getJournal();
    }

}
