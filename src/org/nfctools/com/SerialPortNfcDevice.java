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
package org.nfctools.com;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import org.nfctools.io.NfcDevice;
import static ch.bod.nfcMusic.Logger.*;


import java.io.IOException;
import java.util.Enumeration;
import java.util.TooManyListenersException;

public class SerialPortNfcDevice implements NfcDevice {

	private final static int TIMEOUT_FOR_OPEN = 1000;
	private final static int SERIAL_PORT_BUFFER_SIZE = 1024;



	private SerialPort port = null;
	private AbstractBaudRateNegotiator speedNegotiator;
	private InputOutputToken inputOutputToken = new InputOutputToken();

	private int baudRate;
	private String comPort;

	public SerialPortNfcDevice(AbstractBaudRateNegotiator speedNegotiator) {
		this.speedNegotiator = speedNegotiator;
	}

	@Override
	public InputOutputToken getConnectionToken() {
		return inputOutputToken;
	}

	@Override
	public void close() throws IOException {
		inputOutputToken.close();
		port.close();
		port = null;
	}

	@Override
	public void open() throws IOException {
		if (port == null) {
			try {
				CommPortIdentifier identifier = findCommPortIdentifier(comPort);
				if (identifier == null) {
					throw new RuntimeException("ComPort not found: " + comPort);
				}

				port = (SerialPort)identifier.open(SerialPortNfcDevice.class.getName() + "." + comPort,
						TIMEOUT_FOR_OPEN);

				port.setInputBufferSize(SERIAL_PORT_BUFFER_SIZE);
				//				initSerialPortEventListener();
				tweakPort();
				inputOutputToken.setInputStream(port.getInputStream());
				inputOutputToken.setOutputStream(port.getOutputStream());

				speedNegotiator.negotiateBaudRateOnObject(port, baudRate);
			}
			catch (PortInUseException e) {
				throw new IOException(e);
			}
		}
	}

	private void initSerialPortEventListener() throws IOException {
		try {
			port.addEventListener(new SerialPortEventListenerImpl());
			port.notifyOnDataAvailable(true);
		}
		catch (TooManyListenersException e) {
			throw new IOException(e);
		}
	}

	private void tweakPort() {
		try {
			port.enableReceiveThreshold(1);
			port.enableReceiveTimeout(10);
		}
		catch (UnsupportedCommOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public int getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}

	public String getComPort() {
		return comPort;
	}

	public void setComPort(String comPort) {
		this.comPort = comPort;
	}

	@SuppressWarnings("unchecked")
	private CommPortIdentifier findCommPortIdentifier(String name) {
		Enumeration<CommPortIdentifier> enumeration = CommPortIdentifier.getPortIdentifiers();

		CommPortIdentifier port = null;

		while (enumeration.hasMoreElements()) {
			CommPortIdentifier identifier = enumeration.nextElement();
			if (identifier.getName().equalsIgnoreCase(name)) {
				port = identifier;
			}
		}
		return port;
	}

}
