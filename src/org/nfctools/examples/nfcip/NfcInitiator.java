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

package org.nfctools.examples.nfcip;

import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.nfcip.NFCIPManager;
import static ch.bod.nfcMusic.Logger.*;


import java.text.DecimalFormat;

public class NfcInitiator implements Runnable {


	private DecimalFormat df = new DecimalFormat("#.###");
	private NFCIPManager nfcipManager;
	private final int RUNS = 5;

	public NfcInitiator(NFCIPManager nfcipManager) {
		this.nfcipManager = nfcipManager;
	}

	@Override
	public void run() {
		try {

			while (true) {
				info("Waiting for connection");
				NFCIPConnection nfcipConnection = nfcipManager.connectAsInitiator();

				info("Connected, sending data...");

				long time = System.currentTimeMillis();
				int totalSend = 0;

				byte[] data = new byte[200];
				for (int k = 0; k < data.length; k++)
					data[k] = (byte)(255 - k);

				for (int runs = 0; runs < RUNS; runs++) {

					log.trace("Start of Run: " + runs);
					nfcipConnection.send(data);
					totalSend += data.length;
					info("Send: " + data.length + " Runs: " + runs);
					byte[] receive = nfcipConnection.receive();
					info("Response: " + receive.length);
					log.trace("End of Run: " + runs);
				}

				nfcipConnection.send(new byte[0]);
				nfcipConnection.receive();

				double timeNeeded = (double)(System.currentTimeMillis() - time) / 1000.0;
				info("DONE in " + timeNeeded + "sec, " + df.format(((double)totalSend / 1024) / timeNeeded)
						+ " kb/s");

				info("Closing connection");
				nfcipConnection.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
