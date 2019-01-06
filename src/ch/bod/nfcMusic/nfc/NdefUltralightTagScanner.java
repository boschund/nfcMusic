package ch.bod.nfcMusic.nfc;

import ch.bod.nfcMusic.MusicController;
import org.nfctools.NfcAdapter;
import org.nfctools.api.TagScannerListener;
import org.nfctools.mf.ul.Type2NfcTagListener;
import org.nfctools.scio.Terminal;
import org.nfctools.scio.TerminalHandler;
import org.nfctools.scio.TerminalMode;
import org.nfctools.spi.acs.AcsTerminal;

public class NdefUltralightTagScanner implements TagScannerListener {

    private static MusicController controller;

    public NdefUltralightTagScanner(MusicController controller) {
        this.controller = controller;
        NfcListener ndefListener = new NfcListener(controller);
        scanForTags(ndefListener);
    }

    public static Terminal getAvailableTerminal(String preferredTerminalName) {
        try {
            TerminalHandler terminalHandler = new TerminalHandler();
            terminalHandler.addTerminal(new AcsTerminal());
            return terminalHandler.getAvailableTerminal(preferredTerminalName);
        } catch (RuntimeException e){
            controller.error(e);
            return null;
        }
    }

    private void scanForTags(NfcListener ndefListener) {

        Terminal availableTerminal = null;
        while (availableTerminal == null){
            availableTerminal = getAvailableTerminal(null);
        }
        NfcAdapter nfcAdapter = new NfcAdapter(availableTerminal, TerminalMode.INITIATOR, this);
        nfcAdapter.registerTagListener(new Type2NfcTagListener(ndefListener));
        nfcAdapter.startListening();
    }

    @Override
    public void onScanningEnded() {
        System.out.println("Scanning ended");
    }

    @Override
    public void onScanningFailed(Throwable throwable) {
        controller.error(throwable);
    }

    @Override
    public void onTagHandingFailed(Throwable throwable) {
        controller.error(throwable);
    }
}
