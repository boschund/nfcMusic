/**
 * Copyright 2011 Adrian Stabiszewski, as@org.nfctools.org
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


public class NfcTarget implements Runnable {


	private NFCIPManager nfcipManager;

	public NfcTarget(NFCIPManager nfcipManager) {
		this.nfcipManager = nfcipManager;
	}

	@Override
	public void run() {

		try {
			while (true) {

				info("Waiting for connection");
				NFCIPConnection nfcipConnection = nfcipManager.connectAsTarget();

				info("Connected, waiting for data...");

				byte[] data = null;
				int runs = 0;
				do {
					data = nfcipConnection.receive();
					info("Received: " + data.length + " Runs: " + runs);
					nfcipConnection.send(data);
					runs++;
				} while (data != null && data.length > 0);

				info("DONE!!!");

				info("Closing connection");
				nfcipConnection.close();
				Thread.sleep(500);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

}
