package kr.hs.dgsw.situsetting;

public class SettingBean {
    private int ringVolume;
    private int musicVolume;
    private int brightness;
    private int ringerMode;

    public SettingBean() {
    }

    public SettingBean(int ringVolume, int musicVolume, int brightness, int ringerMode) {
        this.ringVolume = ringVolume;
        this.musicVolume = musicVolume;
        this.brightness = brightness;
        this.ringerMode = ringerMode;
    }

    public int getRingVolume() {
        return ringVolume;
    }

    public void setRingVolume(int ringVolume) {
        this.ringVolume = ringVolume;
    }

    public int getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(int musicVolume) {
        this.musicVolume = musicVolume;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getRingerMode() {
        return ringerMode;
    }

    public void setRingerMode(int ringerMode) {
        this.ringerMode = ringerMode;
    }

    @Override
    public String toString() {
        return "Brightness: "+brightness+", MusicVolume: "+musicVolume+", RingVolume: "+ringVolume+", RingerMode: "+ringerMode;
    }
}
