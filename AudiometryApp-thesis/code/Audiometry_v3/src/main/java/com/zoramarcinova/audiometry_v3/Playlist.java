package com.zoramarcinova.audiometry_v3;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Playlist {

    private List<Sound> soundtrackList = new ArrayList<>();
    private List<Sound> pauseList = new ArrayList<>();

    public Playlist() throws UnsupportedAudioFileException, IOException, URISyntaxException {

        this.soundtrackList.add(new Sound("sounds/nizke/phone_vibration_250_Hz_rms 1.wav", Frequency.LOW, "Vybracie telefonu"));
        this.soundtrackList.add(new Sound("sounds/nizke/Thunder_Rumble  80Hz_rms 1.wav", Frequency.LOW, "Hrom"));
        this.soundtrackList.add(new Sound("sounds/nizke/wind2__35-45Hz_rms 1.wav", Frequency.LOW, "Vietor"));

        this.soundtrackList.add(new Sound("sounds/stredne/Cash_Register_2_rms 1.wav", Frequency.MEDIUM, "Pokladnica"));
        this.soundtrackList.add(new Sound("sounds/stredne/elephant_550Hz_rms 1.wav", Frequency.MEDIUM,"Slon"));
        this.soundtrackList.add(new Sound("sounds/stredne/Sloppy_Eating 1_rms 1.wav", Frequency.MEDIUM, "Jedenie"));

        this.soundtrackList.add(new Sound("sounds/vysoke/bird-whistling-single-robin__7_3_kHz_rms 1.wav", Frequency.HIGH, "Spievanie vtaka"));
        this.soundtrackList.add(new Sound("sounds/vysoke/Chewing gum 5kHz-6,5 kHz_rms 1.wav", Frequency.HIGH, "Zutie zuvacky"));
        this.soundtrackList.add(new Sound("sounds/vysoke/Crickets_chirping. 8-8_rms 1.wav", Frequency.HIGH, "Zvuk cikad"));


        this.pauseList.add(new Sound("pauses/pause1.wav", Frequency.LOW, "Pauza (1sekunda)"));
        this.pauseList.add(new Sound("pauses/pause2.wav", Frequency.LOW, "Pauza (2sekundy)"));
        this.pauseList.add(new Sound("pauses/pause3.wav", Frequency.LOW, "Pauza (3sekundy)"));
        this.pauseList.add(new Sound("pauses/pause4.wav", Frequency.LOW, "Pauza (4sekundy)"));
        this.pauseList.add(new Sound("pauses/pause5.wav", Frequency.LOW, "Pauza (5sekund)"));

    }


    public void addSound(String path, Frequency frequency, String name) throws UnsupportedAudioFileException, IOException, URISyntaxException {
        this.soundtrackList.add(new Sound(path, frequency,name));
    }

    public void removeSound(String path) {
        Iterator<Sound> iterator = this.soundtrackList.iterator();
        while (iterator.hasNext()) {
            Sound toDelete = iterator.next();
            if (toDelete.getPath().equals(path)) {
                iterator.remove();
            }
        }
    }

    public List<Sound> getSoundtrackList() {
        return this.soundtrackList;
    }
    public List<Sound> getPauseList() {
        return this.pauseList;
    }

    public int numberOfSoundEntries(){
        return this.soundtrackList.size();
    }


}
