/**
 * Copyright 2011-2012 Adrian Stabiszewski, as@org.nfctools.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nfctools.examples.ndefreader;

import org.nfctools.examples.TerminalUtils;
import org.nfctools.io.NfcDevice;
import org.nfctools.mf.MfCardListener;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.ndef.MfNdefReader;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.Record;
import org.nfctools.spi.acs.Acr122ReaderWriter;
import static ch.bod.nfcMusic.Logger.*;

import java.io.IOException;
import java.util.List;

/**
 * NdefReaderDemo reads an NDEF message from a Mifare Classic card and shows it in the log.
 * 
 */
public class NdefReaderDemo implements MfCardListener {

	private NfcDevice nfcReaderWriter = TerminalUtils.getAvailableTerminal();
	private MfReaderWriter readerWriter = new Acr122ReaderWriter(nfcReaderWriter);
	private NdefMessageDecoder decoder = NdefContext.getNdefMessageDecoder();

	@Override
	public void cardDetected(MfCard card, MfReaderWriter readerWriter) throws IOException {
		MfNdefReader ndefReader = new MfNdefReader(readerWriter, decoder);

		try {
			List<Record> records = ndefReader.readNdefMessage(card);
			for (Record record : records) {
				info(record.toString());
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void runNdefReader() {

		try {
			nfcReaderWriter.open();
			info("Listening...");
			readerWriter.setCardListener(this);
			info("Done");
			System.in.read();
			nfcReaderWriter.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		try {
			NdefReaderDemo demo = new NdefReaderDemo();
			demo.runNdefReader();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
