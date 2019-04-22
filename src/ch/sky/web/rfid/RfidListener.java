package ch.sky.web.rfid;

import ch.sky.web.test.RWLauncher;
import org.nfctools.NfcAdapter;
import org.nfctools.api.NfcTagListener;
import org.nfctools.api.TagScannerListener;
import org.nfctools.examples.TerminalUtils;
import org.nfctools.mf.MfAccess;
import org.nfctools.mf.MfCardListener;
import org.nfctools.mf.MfException;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.block.BlockResolver;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.classic.Key;
import org.nfctools.mf.classic.MemoryLayout;
import org.nfctools.mf.classic.MfClassicReaderWriter;
import org.nfctools.mf.tools.MfClassicCardCleaner;
import org.nfctools.scio.TerminalMode;
import org.nfctools.utils.LoggingUnknownTagListener;

import javax.smartcardio.CardException;
import java.io.IOException;

import static ch.sky.web.rfid.HexUtils.*;

public class RfidListener implements TagScannerListener {

    private static Acr122Device acr122 = null;

    private static final int SECTOR = 1;
    private static final int BLOCK = 1;
    private static final String KEY = "FFFFFFFFFFFF";
    private static final int SXT = 16;

    private RWLauncher launcher;

    public RfidListener(RWLauncher launcher){
        this.launcher = launcher;
        MfCardListener listener = new MfCardListener() {
            @Override
            public void cardDetected(MfCard mfCard, MfReaderWriter mfReaderWriter) {
                try {
                    if(launcher.getMode() == RWLauncher.Mode.READ)
                        launcher.setRes(readCard(mfReaderWriter, mfCard));
                    if(launcher.getMode() == RWLauncher.Mode.WRITE)
                        writeCard(mfReaderWriter, mfCard, launcher.getTest());
                    if(launcher.getMode() == RWLauncher.Mode.CLEAN)
                        cleanCard();
                }
                catch (CardException ce) {
                    ce.printStackTrace();
                }
            }
        };
        // Start listening
        try {
            listen(listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * handling more than 32bit!
     * @throws CardException if fails
     */
    private void writeCard(MfReaderWriter mfReaderWriter, MfCard mfCard, String text) throws CardException {
        // 16 chars in 16 sectors - schlimmstenfalls könnte man pro sector 3 blöcke statt nur einen schreiben
        if (text.length() >= (15*16))
            throw new CardException("::: Textlänge darf 16*16 Zeichen (256) nicht überschreiten!");
        // hacking the text n chars
        char[] hack = text.toCharArray();
        // adding fillings of 16 void chars (if there's enougth of place)
        char[] fillings = new char[]{' ', ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',};
        if (text.length() <= (15*16-16))
            hack = combine(hack, fillings);
        // substrings of 16 chars
        char[] sub = new char[SXT];
        int sector = 1; // sector 0 = MAD  --> zur verschlüsselung
        System.out.println("allData : " + text);
        for (int i = 0; i < hack.length; sector++)
        {
            sub = new char[SXT];
            for (int j = 0; j < SXT ; j++)
            {
                if (i < text.length())
                    sub[j] = hack[i];
                else
                    sub[j] = ' ';
                i++;
            }
            String substring = new String(sub);
            System.out.println("SUB : " + substring);
            writeToCards(mfReaderWriter, mfCard, substring, sector, 1);
        }
//        writeToCards(mfReaderWriter, mfCard, text, 1, 1);
//        // ...
//        writeToCards(mfReaderWriter, mfCard, text, 12, 1);
    }

    private static char[] combine(char[] a, char[] b){
        int length = a.length + b.length;
        char[] result = new char[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    private String readCard(MfReaderWriter mfReaderWriter, MfCard mfCard) throws CardException {
        int sector = 1;
        StringBuilder res = new StringBuilder("");
        String teil = null;
        do {
            teil = RfidListener.dumpMifareClassic1KBlock(mfReaderWriter, mfCard, sector, BLOCK, KEY);
            if (teil == null || teil.equalsIgnoreCase("")) break;
            res.append(teil);
            sector++;
        }
        while (teil != null);
        return res.toString();
//        return dumpMifareClassic1KBlock(mfReaderWriter, mfCard, sector, BLOCK, KEY);
    }

    private void cleanCard() throws CardException {
        MfClassicCardCleaner tagListener = new MfClassicCardCleaner();
        NfcAdapter nfcAdapter = new NfcAdapter(TerminalUtils.getAvailableTerminal(), TerminalMode.INITIATOR, this);
        nfcAdapter.registerTagListener(tagListener);
        nfcAdapter.registerUnknownTagListerner(new LoggingUnknownTagListener());
        nfcAdapter.startListening();
    }

    @Override
    public void onScanningEnded() {
        System.out.println("Scanning ended");
    }

    @Override
    public void onScanningFailed(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onTagHandingFailed(Throwable throwable) {
        throwable.printStackTrace();
    }

    //******************************************************************************
    //* *** ***  TO NOT TOUCH THE FOLLOWING CODE - HERE COMES THE MAGIC !!!!!!!!!!!!
    //******************************************************************************

    /**
     * Listens for cards using the provided listener.
     * @param listener a listener
     */
    private static void listen(MfCardListener listener) throws IOException {
        boolean connected = false;
        while (!connected) {
            try {
                acr122 = new Acr122Device();
                connected = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        acr122.open();
        acr122.listen(listener);
    }

    public void stop(){
        try {
            acr122.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String dumpMifareClassic1KBlock(MfReaderWriter reader, MfCard card, int sectorId, int blockId, String key) throws CardException {
        byte[] keyBytes = hexStringToBytes(key);
        // Reading with key A
        MfAccess access = new MfAccess(card, sectorId, blockId, Key.A, keyBytes);
        String blockData = readMifareClassic1KBlock(reader, access);
        if (blockData != null) {
            // Block read
            String value = hexToAscii(blockData);
            return value;
        }
        // All keys tested, failed to read block
        System.out.println("<Failed to read block>");
        return "";
    }

    private static String readMifareClassic1KBlock(MfReaderWriter reader, MfAccess access) throws CardException {
        String data = null;
        try {
            MfBlock block = reader.readBlock(access)[0];
            data = bytesToHexString(block.getData());
        } catch (IOException ioe) {
            if (ioe.getCause() instanceof CardException) {
                throw (CardException) ioe.getCause();
            }
        }
        return data;
    }

    /**
     * Writes to cards.
     */
    private static void writeToCards(MfReaderWriter writer, MfCard card, String data, int sector, int block) throws CardException {
        String hex = asciiToHex(data);
        writeToMifareClassic1KCard(writer, card, sector, block, KEY, hex); }

    /**
     * Write data to a Mifare Classic 1K card.
     * @param reader the reader
     * @param card the card
     * @param sectorId the sector to be written
     * @param blockId the block to be written
     * @param key the key to be used for writing
     * @param dataString the data hex string to be written
     */
    public static void writeToMifareClassic1KCard(MfReaderWriter reader, MfCard card, int sectorId, int blockId, String key, String dataString)
            throws CardException {

        byte[] keyBytes = hexStringToBytes(key);
        // Reading with key A
        MfAccess access = new MfAccess(card, sectorId, blockId, Key.A, keyBytes);
        String blockData = readMifareClassic1KBlock(reader, access);
        if (blockData == null) {
            // Reading with key B
            access = new MfAccess(card, sectorId, blockId, Key.B, keyBytes);
            blockData = readMifareClassic1KBlock(reader, access);
        }
        System.out.print("Old block data: ");
        if (blockData == null) {
            // Failed to read block
           System.out.println("<Failed to read block>");
        } else {
            // Block read
            System.out.println(blockData + " (Key " + access.getKey() + ": " + key + ")");

            // Writing with same key
            boolean written = false;
            try {
                byte[] data = hexStringToBytes(dataString);
                MfBlock block = BlockResolver.resolveBlock(MemoryLayout.CLASSIC_1K, sectorId, blockId, data);
                written = writeMifareClassic1KBlock(reader, access, block);
            } catch (MfException me) {
                System.out.println(me.getMessage());
            }
            if (written) {
                blockData = readMifareClassic1KBlock(reader, access);
                System.out.print("New block data: ");
                if (blockData == null) {
                    // Failed to read block
                    System.out.println("<Failed to read block>");
                } else {
                    // Block read
                    System.out.println(blockData + " (Key " + access.getKey() + ": " + key + ")");
                }
            }
        }
    }

    /**
     * Writes a Mifare Classic 1K block.
     * @param reader the reader
     * @param access the access
     * @param block the block to be written
     * @return true if the block has been written, false otherwise
     */
    private static boolean writeMifareClassic1KBlock(MfReaderWriter reader, MfAccess access, MfBlock block) throws CardException {
        boolean written = false;
        try {
            reader.writeBlock(access, block);
            written = true;
        } catch (IOException ioe) {
            if (ioe.getCause() instanceof CardException) {
                throw (CardException) ioe.getCause();
            }
        }
        return written;
    }
}
