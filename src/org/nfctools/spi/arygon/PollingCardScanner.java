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
package org.nfctools.spi.arygon;

import org.nfctools.mf.MfCardListener;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.card.MfCard;
import static ch.bod.nfcMusic.Logger.*;


import java.io.IOException;

public class PollingCardScanner implements Runnable {



	private ArygonHighLevelReaderWriter nfcReaderWriter;
	private MfCardListener cardListener;
	private MfReaderWriter readerWriter;

	public PollingCardScanner(ArygonHighLevelReaderWriter nfcReaderWriter, MfCardListener cardListener,
			MfReaderWriter readerWriter) {
		this.nfcReaderWriter = nfcReaderWriter;
		this.cardListener = cardListener;
		this.readerWriter = readerWriter;
	}

	@Override
	public void run() {

		MfCard card = null;
		while (!Thread.interrupted()) {
			try {
				if (nfcReaderWriter.hasData()) {
					try {
						card = ((ArygonReaderWriter)readerWriter).readCard();
						cardListener.cardDetected(card, readerWriter);
						readerWriter.setCardIntoHalt(card);
					}
					catch (IOException e) {
						try {
							readerWriter.reselectCard(card);
							readerWriter.setCardIntoHalt(card);
						}
						catch (Exception e1) {
							Thread.sleep(1000);
						}
					}
					((ArygonReaderWriter)readerWriter).scanForCard();
				}
				else {
					Thread.sleep(10);
				}
			}
			catch (IOException e) {
				break;
			}
			catch (InterruptedException e) {
				return;
			}
		}
	}
}
