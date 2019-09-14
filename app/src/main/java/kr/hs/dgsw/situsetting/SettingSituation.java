package kr.hs.dgsw.situsetting;

public enum SettingSituation {
    HOME("1"), OUTDOOR("2"), NIGHT("3"), ALPHA("4");

    private String id;

    SettingSituation(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
