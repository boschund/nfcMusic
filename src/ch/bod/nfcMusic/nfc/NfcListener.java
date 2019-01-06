package ch.bod.nfcMusic.nfc;

import ch.bod.nfcMusic.MusicController;
import ch.bod.nfcMusic.sound.Song;
import org.nfctools.ndef.NdefListener;
import org.nfctools.ndef.NdefOperations;
import org.nfctools.ndef.NdefOperationsListener;
import org.nfctools.ndef.Record;
import org.nfctools.snep.Sneplet;

import java.io.File;
import java.util.Collection;

public class NfcListener implements NdefListener, NdefOperationsListener, Sneplet {

    private MusicController controller;
    public NfcListener(MusicController controller) {
        this.controller = controller;
    }

    @Override
    public void onNdefMessages(Collection<Record> records) {
        for (Record record : records) {
            String path = record.getKey();
            File songFile = new File(path);
            if (songFile.exists() && songFile.isFile()) {
                //Here comes a song
                controller.callNext(new Song(new File(path)));
            }
            else if (songFile.exists() && songFile.isDirectory()) {
                controller.resetPlaylist();
                File[] listOfFiles = songFile.listFiles();
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile())
                        if (listOfFiles[i].getName().endsWith("mp3"))
                            controller.addToPlaylist(new Song(listOfFiles[i]));
                }
                controller.callNext();
            }
            else {
                controller.info("Scanned File : " + path + " is not existent !");
            }
        }
    }

    @Override
    public void onNdefOperations(NdefOperations ndefOperations) {
        if (ndefOperations.isFormatted()) {
            if (ndefOperations.hasNdefMessage() && controller.getMode() == MusicController.MODE.READABLE)
                onNdefMessages(ndefOperations.readNdefMessage());
            else if (ndefOperations.hasNdefMessage() && controller.getMode() == MusicController.MODE.WRITABEL)
                ndefOperations.writeNdefMessage(getRecord());
            else
                controller.info("Empty tag. (Or DEV Mode)");
        } else {
            controller.info("Empty tag. NOT formatted." + ndefOperations.getMaxSize() + " bytes");
            ndefOperations.format();
            controller.info("Device FORMATTED !!!");
        }
    }

    private Record getRecord() {
        MusicRecord mr = new MusicRecord();
        mr.setContentType("java.lang.String");
        mr.setKey(controller.getInput());
        return mr;
    }

    @Override
    public Collection<Record> doGet(Collection<Record> requestRecords) {
        System.out.println("SNEP get");
        onNdefMessages(requestRecords);
        return null;
    }

    @Override
    public void doPut(Collection<Record> requestRecords) {
        System.out.println("SNEP put");
        onNdefMessages(requestRecords);
    }
}
