package com.zoramarcinova.audiometry_v3;

import java.util.ArrayList;
import java.util.List;

public class ResultEntry {

        //private Sound sound;
        private String soundName;
        private String soundPath;
        private float duration = 0f;
        private String timeStarted = "";
        private List<String> clicks = new ArrayList<>();
        private float volume = 0.0f;

        public ResultEntry(String name, String url, float duration, String timeStarted, float volume) {
            //this.sound = sound;
            this.soundName = name;
            this.soundPath = url;
            this.duration = duration;
            this.timeStarted = timeStarted;
            this.volume = volume;
        }

//        public Sound getSound() {
//            return this.sound;
//        }

        public List<String> getClicks() {
            return clicks;
        }

        public float getDuration() {
            return this.duration;
        }

        public void markClick(String time) {
            clicks.add(time);
        }

        public float getVolume() {
            return this.volume;
        }

        public String getTimeStarted() {
            return this.timeStarted;
        }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public String getSoundPath() {
        return soundPath;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }

    public String getSoundFilename() {
            return this.soundPath.substring(soundPath.lastIndexOf('/') + 1);
    }
}
