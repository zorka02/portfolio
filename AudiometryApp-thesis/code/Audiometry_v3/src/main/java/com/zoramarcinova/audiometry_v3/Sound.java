package com.zoramarcinova.audiometry_v3;

import org.apache.commons.io.FileUtils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class Sound {
    private File file;
    private AudioInputStream audioInputStream;
    private Frequency frequency;
    private String name;
    private String path;
    private boolean heard = false;
    private URL resourceUrl;
    private int volume;
    private String filename;



    public Sound(String path, Frequency frequency, String name) throws UnsupportedAudioFileException, IOException, URISyntaxException {

        this.resourceUrl = Sound.class.getResource(path);
        this.path = path;
        this.name = name;
        //this.audioInputStream = AudioSystem.getAudioInputStream(file);
        this.filename = path.substring(path.lastIndexOf('/') + 1);

        this.frequency = frequency;


    }


    public AudioInputStream getAudioInputStream() {
        return audioInputStream;
    }

    public URL getFile() {

        //return file;
        return resourceUrl;
    }

    public Frequency getFrequency() {
        return frequency;
    }
    /*
    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }*/

    public String getPath() {
        return this.path;
    }

    public String getName() {
        return name;
    }


    public void markAsHeard() {
        this.heard = true;
    }

    public boolean isHeard() {
        return heard;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getFilename() {
        return filename;
    }
}
