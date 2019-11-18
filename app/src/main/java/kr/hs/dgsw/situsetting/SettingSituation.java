package kr.hs.dgsw.situsetting;

public enum SettingSituation {
    HOME(1L), OUTDOOR(2L), NIGHT(3L), ALPHA(4L);

    private long id;

    SettingSituation(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
