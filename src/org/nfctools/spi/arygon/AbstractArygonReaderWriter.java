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

import org.nfctools.com.InputOutputToken;
import org.nfctools.io.NfcDevice;
import static ch.bod.nfcMusic.Logger.*;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractArygonReaderWriter {



	public enum LED {
		GREEN("06"), RED("02");

		private String id;

		private LED(String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}
	}

	protected NfcDevice nfcDevice;
	protected InputOutputToken inputOutputToken;
	protected OutputStream out;
	protected InputStream in;

	protected AbstractArygonReaderWriter(NfcDevice nfcDevice) {
		this.nfcDevice = nfcDevice;
		if (nfcDevice.getConnectionToken() instanceof InputOutputToken) {
			inputOutputToken = (InputOutputToken)nfcDevice.getConnectionToken();
		}
		else
			throw new IllegalArgumentException("connection token not supported");
	}

	public void open() throws IOException {
		nfcDevice.open();
		out = inputOutputToken.getOutputStream();
		in = inputOutputToken.getInputStream();
	}

	public void close() throws IOException {
		nfcDevice.close();
	}
}
