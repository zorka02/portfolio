package com.zoramarcinova.audiometry_v3;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Test {

    private List<Sound> soundList = new ArrayList<>();
    private Sound currentlyPlayingSound = null;
    private int currentlyPlayingSoundIndex = 0;
    private Clip clip = null;
    private Playlist playlist;
    // private boolean testFinished = false;
    private volatile BooleanProperty testFinished = new SimpleBooleanProperty(false);
    private int upperBound;
    private int lowerBound;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private UserEntry userEntry;
    private int currentVolumeIndex = 0;
    private int[] volumeLevels;

    private Thread soundThread = new Thread(() -> {
        try {
            playAll();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | InterruptedException |
                 URISyntaxException e) {
            throw new RuntimeException(e);
        }
    });

    private volatile boolean stopPlaying = false;

    public Test (int lowerBound, int upperBound, int[] volumeLevels) throws UnsupportedAudioFileException, IOException, LineUnavailableException, URISyntaxException {

        this.playlist = new Playlist();
        this.clip = AudioSystem.getClip();

        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.volumeLevels = volumeLevels;

        this.userEntry = new UserEntry();
    }

//    public boolean isTestFinished() {
//        return testFinished;
//    }
    public void initiateSoundList() throws UnsupportedAudioFileException, IOException, URISyntaxException {
        soundList.clear();

        List<Sound> tempList = new ArrayList<>(playlist.getSoundtrackList());

        Collections.shuffle(tempList);

        for (Sound sound : tempList) {
            if (!sound.isHeard()) {
                int randomIndex = (int) (Math.random() * 5);

                soundList.add(playlist.getPauseList().get(randomIndex));
                soundList.add(sound);
            }



        }
    }

    public boolean playSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if (currentlyPlayingSoundIndex >= soundList.size()) {
            return false;
        }


        clip.close();

        if (isStopPlaying()) return false;
        currentlyPlayingSound = soundList.get(currentlyPlayingSoundIndex);
        //System.out.println(currentlyPlayingSound.getFilename());
        //System.out.println(playlist.getSoundtrackList().get(7).getName());
        currentlyPlayingSound.setVolume(volumeLevels[currentVolumeIndex]);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(currentlyPlayingSound.getFile());
        //AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(playlist.getSoundtrackList().get(7).getFile());
        AudioFormat format = audioInputStream.getFormat();
        clip.open(audioInputStream);

        LocalTime currentTime = LocalTime.now();
        int frameLength = clip.getFrameLength();
        float durationInSeconds = (frameLength+ 0.0f) / format.getFrameRate();

        userEntry.getResults().add(new ResultEntry(currentlyPlayingSound.getName(), currentlyPlayingSound.getPath(), durationInSeconds, currentTime.format(timeFormatter), volumeLevels[currentVolumeIndex] ));
        clip.start();


        clip.addLineListener(new LineListener() {
            @Override
            public void update(LineEvent event) {
                if (event.getType() == LineEvent.Type.STOP) {
                    // Close the clip when it finishes playing
                    event.getLine().close();
                    synchronized (clip) {
                        // Notify the waiting thread
                        clip.notify();
                    }
                }
            }
        });

        // Start playing the clip
        clip.start();

        // Wait until the clip finishes playing
        synchronized (clip) {
            clip.wait();
        }

        return true;
    }

    public void stopPlaying() {

        clip.close();
        stopPlaying = true;
    }



    public void playAll() throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException, URISyntaxException {
        if(playlist.getSoundtrackList().size() == 0) {
            throw new RuntimeException("Error: No sounds to play.");
        }
        LocalDateTime currentTime = LocalDateTime.now();
        userEntry.setDateStarted(currentTime.format(dateFormatter));
        userEntry.setTimeStarted(currentTime.format(timeFormatter));

        int currentRound = 1;
        currentVolumeIndex = 0;

        while (currentRound <= volumeLevels.length){
            initiateSoundList();
            if (currentRound == 1) {
                soundList.remove(0);
            }
            if (soundList.size() == 0) break;

            //System.out.println("ROUND " + currentRound);
            currentlyPlayingSoundIndex = 0;
            setSystemVolume(volumeLevels[currentVolumeIndex]);
            if (!playSound()) break;
            while(currentlyPlayingSoundIndex < soundList.size()){

                currentlyPlayingSoundIndex += 1;
                setSystemVolume(volumeLevels[currentVolumeIndex]);
                if (isStopPlaying()) {
                    testFinished.set(true);
                    return;
                }
                if (!playSound()) {
                    break;
                }

            }

            currentRound += 1;
            currentVolumeIndex += 1;
        }
        userEntry.setPlaylist(this.playlist);
        testFinished.set(true);
        //System.out.println("END OF THE TEST");
    }

    public UserEntry getUser() {
        return this.userEntry;
    }

    public BooleanProperty isTestFinished() {
        return testFinished;
    }

    public void test() {
        soundThread.start();

    }

    public Clip getClip() {
        return this.clip;
    }


    public void markPressButton() {
        soundList.get(currentlyPlayingSoundIndex).markAsHeard();
        LocalTime currentTime = LocalTime.now();

        userEntry.getResults().get(userEntry.getResults().size() - 1).markClick(currentTime.format(timeFormatter));
        //System.out.println("Heard button pressed");
    }

    public void print() {
        for(Sound sound : soundList) {
            System.out.println(sound.getName());
        }
    }

    public void recordResults() throws IOException {
//        System.out.println("MENO: " + user.getUsername());
//        System.out.println("VEK: " + user.getAge());
//        System.out.println("\nVYSLEDKY:");
//        for (ResultEntry entry : user.getResults()) {
//            System.out.println("\nZvuk: " + entry.getSoundName() + "(" + entry.getSoundPath() + "), Dlzka: " + entry.getDuration() + ", Cas zaciatku prehratia: " + entry.getTimeStarted() + ", Hlasitost: " + entry.getVolume());
//            for (String click : entry.getClicks()) {
//                System.out.println("\n  Klik: " + click);
//            }
//        }
//        System.out.println("\n\nCSV:");
//        for (Sound sound : playlist.getSoundtrackList()) {
//            System.out.println("\nZvuk: " + sound.getName() + "(" + sound.getPath() + "), Hlasitost posledneho prehratia: "  + sound.getVolume() + " Reakcia: " + sound.isHeard());
//        }
//
//        for (Sound pause : playlist.getPauseList()) {
//            System.out.println("\nZvuk: " + pause.getName() + "(" + pause.getPath() + "), Hlasitost posledneho prehratia: -  Reakcia: " + pause.isHeard());
//        }


        FileWriter fileWriter = new FileWriter("./vysledky_priebeh_testu.txt", true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write("_______________________________________");


        bufferedWriter.write("\nMENO: " + userEntry.getName());
        bufferedWriter.write("\nPRIEZVISKO: " + userEntry.getSurname());
        bufferedWriter.write("\nVEK: " + userEntry.getAge());
        bufferedWriter.write("\nZACIATOK TESTU - DEN: " + userEntry.getDateStarted());
        bufferedWriter.write("\nZACIATOK TESTU - CAS: " + userEntry.getTimeStarted());
        bufferedWriter.write("\nVYSLEDKY:");
        for (ResultEntry entry : userEntry.getResults()) {
            bufferedWriter.write("\nZvuk: " + entry.getSoundName() + "(" + entry.getSoundPath() + "), Dlzka: " + entry.getDuration() + ", Cas zaciatku prehratia: " + entry.getTimeStarted() + ", Hlasitost: " + entry.getVolume());
            for (String click : entry.getClicks()) {
                bufferedWriter.write("\n  Klik: " + click);
            }
        }

        bufferedWriter.write("\n_______________________________________");
        bufferedWriter.close();


        File fileCSV = new File("./vysledky_tabulky.csv");
        //boolean fileExists = fileCSV.exists();

        FileWriter fileWriter2 = new FileWriter(fileCSV, true);
        BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter2);

        if (fileCSV.length() == 0) {
            bufferedWriter2.write("MENO;PRIEZVISKO;VEK;ZACIATOK TESTOVANIA - DEN;ZACIATOK TESTOVANIA - CAS;ZACIATOK PREHRATIA NAHRAVKY;CAS PRVEHO ROZPOZNANIA;NAHRAVKA;SUBOR;POSLEDNA HLASITOST PREHRAVANIA;REAKCIA;POCET REAKCII;TYP");
        }


        for (ResultEntry entry : userEntry.getResults()) {
            if (entry.getClicks().size() != 0) {
                if (entry.getSoundFilename().contains("pause")) {
                    bufferedWriter2.write("\n" + userEntry.getName() + ";" + userEntry.getSurname() + ";" + userEntry.getAge() + ";" + userEntry.getDateStarted() + ";" + userEntry.getTimeStarted() + ";" + entry.getTimeStarted() + ";" + entry.getClicks().get(0) + ";" + entry.getSoundName() + ";" + entry.getSoundFilename() + ";" + entry.getVolume() + ";ano;" + entry.getClicks().size() + ";pauza");
                } else {
                    bufferedWriter2.write("\n" + userEntry.getName() + ";" + userEntry.getSurname() + ";" + userEntry.getAge() + ";" + userEntry.getDateStarted() + ";" + userEntry.getTimeStarted() + ";" + entry.getTimeStarted() + ";" + entry.getClicks().get(0) + ";" + entry.getSoundName() + ";" + entry.getSoundFilename() + ";" + entry.getVolume() + ";ano;" + entry.getClicks().size() + ";zvuk");
                }

            } else {
                if (entry.getSoundFilename().contains("pause")) {
                    bufferedWriter2.write("\n" + userEntry.getName() + ";" + userEntry.getSurname() + ";" + userEntry.getAge() + ";" + userEntry.getDateStarted() + ";" + userEntry.getTimeStarted() + ";" + entry.getTimeStarted() + ";-;" + entry.getSoundName() + ";" + entry.getSoundFilename() + ";" + entry.getVolume() + ";nie;0;pauza");
                } else {
                    bufferedWriter2.write("\n" + userEntry.getName() + ";" + userEntry.getSurname() + ";" + userEntry.getAge() + ";" + userEntry.getDateStarted() + ";" + userEntry.getTimeStarted() + ";" + entry.getTimeStarted() + ";-;" + entry.getSoundName() + ";" + entry.getSoundFilename() + ";" + entry.getVolume() + ";nie;0;zvuk");
                }

            }

        }


        bufferedWriter2.close();




    }


    public void setSystemVolume(int volume)
    {
        System.out.println(volume);
        if(volume < 0 || volume > 100)
        {
            throw new RuntimeException("Error: " + volume + " is not a valid number. Choose a number between 0 and 100");
        }

        else
        {
            double endVolume = 655.35 * volume;

            Runtime rt = Runtime.getRuntime();

            URL resourceUrl = Test.class.getResource("nircmd.exe");
            if (resourceUrl == null) {
                throw new IllegalArgumentException("Resource file not found: nircmd.exe");
            }
            //String filePath = new File(resourceUrl.getFile()).getPath();


            File filePath = null;
            try {
                filePath = File.createTempFile("nircmd", ".exe");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            filePath.deleteOnExit();


            try (InputStream inputStream = resourceUrl.openStream()) {
                Files.copy(inputStream, filePath.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            //Process pr;
            String[] unmuteCommand = {filePath.getPath(), "mutesysvolume", "0"};
            String[] command = {filePath.getPath(), "setsysvolume", String.valueOf(endVolume)};
            try
            {
                rt.exec(unmuteCommand);
                rt.exec(command);
                //pr = rt.exec("src/main/resources/nircmd.exe" + " setsysvolume " + endVolume);
                //pr = rt.exec("src/nircmd.exe" + " mutesysvolume 0");

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


        }
    }

    public boolean isStopPlaying() {
        return stopPlaying;
    }
}
