class Player {
   private final String name;
   private String evidenceBag;

    Player(String name) {
        this.name = name;
    }

     String getName() {
        return name;
    }

     String getEvidence() {
        return evidenceBag;
    }

     void setEvidence(String evidence) {
        this.evidenceBag = evidence;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", evidenceBag='" + evidenceBag + '\'' +
                '}';
    }

}
