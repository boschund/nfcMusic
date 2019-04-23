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
package org.nfctools.spi.tama;

import org.nfctools.io.ByteArrayReader;
import org.nfctools.io.ByteArrayWriter;
import org.nfctools.nfcip.NFCIPCommunicator;
import org.nfctools.spi.tama.request.GetFirmwareVersionReq;
import org.nfctools.spi.tama.request.GetGeneralStatusReq;
import org.nfctools.spi.tama.request.TamaRequestEncoder;
import org.nfctools.spi.tama.response.GetFirmwareVersionResp;
import org.nfctools.spi.tama.response.GetGeneralStatusResp;
import org.nfctools.spi.tama.response.TamaResponseDecoder;
import org.nfctools.utils.NfcUtils;
import static ch.bod.nfcMusic.Logger.*;


import java.io.IOException;

public abstract class AbstractTamaCommunicator implements NFCIPCommunicator {



	protected TamaResponseDecoder responseResolver = new TamaResponseDecoder();
	protected TamaRequestEncoder requestEncoder = new TamaRequestEncoder();

	protected ByteArrayReader reader;
	protected ByteArrayWriter writer;

	protected AbstractTamaCommunicator(ByteArrayReader reader, ByteArrayWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	public <RESP, REQ> RESP sendMessage(REQ request) throws IOException {
		debug("Sending message type:  " + request.getClass().getSimpleName());

		byte[] response = sendMessageInternal(requestEncoder.encodeMessage(request));
		RESP resp = responseResolver.<RESP> decodeMessage(response);
		debug("Received message type:  " + resp.getClass().getSimpleName());

		return resp;
	}

	private byte[] sendMessageInternal(byte[] message) throws IOException {
		debug("Sending message:  " + NfcUtils.convertBinToASCII(message));
		writer.write(message, 0, message.length);
		byte[] responseBuffer = new byte[1024];
		int responseLength = reader.read(responseBuffer, 0, responseBuffer.length);
		byte[] response = new byte[responseLength];
		System.arraycopy(responseBuffer, 0, response, 0, responseLength);

		debug("Received message: " + NfcUtils.convertBinToASCII(response));
		return response;
	}

	public void showTamaVersionAndStatus() throws IOException {

		GetFirmwareVersionResp getFirmwareVersionResp = sendMessage(new GetFirmwareVersionReq());
		info("Version: " + getFirmwareVersionResp.getVersion() + " Revision: "
				+ getFirmwareVersionResp.getRevision());
		GetGeneralStatusResp getGeneralStatusResp = sendMessage(new GetGeneralStatusReq());
		info(getGeneralStatusResp.toString());
	}

}
