package ch.bod.nfcMusic.nfc;

import org.nfctools.ndef.mime.MimeRecord;

public class MusicRecord extends MimeRecord {

    @Override
    public byte[] getContentAsBytes()
    {
        return getKey().getBytes();
    }
}
