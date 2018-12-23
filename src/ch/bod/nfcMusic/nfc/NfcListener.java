package ch.bod.nfcMusic.nfc;

import ch.bod.nfcMusic.WebApp;
import org.nfctools.ndef.NdefListener;
import org.nfctools.ndef.NdefOperations;
import org.nfctools.ndef.NdefOperationsListener;
import org.nfctools.ndef.Record;
import org.nfctools.snep.Sneplet;

import java.util.Collection;

public class NfcListener implements NdefListener, NdefOperationsListener, Sneplet {

    private WebApp webAppController;

    public NfcListener(WebApp webAppController) {
        this.webAppController = webAppController;
    }

    @Override
    public void onNdefMessages(Collection<Record> records) {
        for (Record record : records) {
            String song = record.toString();

            //Here comes the song
            webAppController.callSong(song);
        }
    }

    @Override
    public void onNdefOperations(NdefOperations ndefOperations) {
        if (ndefOperations.isFormatted()) {
            if (ndefOperations.hasNdefMessage())
                onNdefMessages(ndefOperations.readNdefMessage());
            else
                System.out.println("Empty formatted tag. Size: " + ndefOperations.getMaxSize() + " bytes");
        } else
            System.out.println("Empty tag. NOT formatted. Size: " + ndefOperations.getMaxSize() + " bytes");
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
