/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Marc de Verdelhan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ch.sky.web.rfid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nfctools.mf.MfCardListener;
import org.nfctools.spi.acs.Acr122ReaderWriter;
import org.nfctools.spi.acs.AcsTerminal;
import org.nfctools.utils.CardTerminalUtils;

import javax.smartcardio.CardTerminal;
import java.io.IOException;

/**
 * An ACR122 device.
 */
public class Acr122Device extends AcsTerminal {
    private static final Logger log = LogManager.getLogger(RfidListener.class.getName());


    /** The ACR122 reader/writer */
    private Acr122ReaderWriter readerWriter;
    
    /**
     * Constructor.
     */
    public Acr122Device() {
        CardTerminal terminal = CardTerminalUtils.getTerminalByName("ACR122");
        setCardTerminal(terminal);
        readerWriter = new Acr122ReaderWriter(this);
    }

    @Override
    public void open() throws IOException {
        log.info("Opening device");
        super.open();
    }

    /**
     * Start listening for cards using the provided listener.
     * @param listener a listener
     */
    public void listen(MfCardListener listener) throws IOException {
        log.info("Listening for cards...");
        readerWriter.setCardListener(listener);
    }
    
    @Override
    public void close() throws IOException {
        log.info("Closing device");
        readerWriter.removeCardListener();
        super.close();
    }
}