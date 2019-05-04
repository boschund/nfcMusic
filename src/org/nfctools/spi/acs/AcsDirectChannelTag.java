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
package org.nfctools.spi.acs;

import org.nfctools.NfcException;
import org.nfctools.api.ApduTag;
import org.nfctools.api.Tag;
import org.nfctools.api.TagType;
import org.nfctools.scio.Command;
import org.nfctools.scio.Response;
import org.nfctools.utils.NfcUtils;
import static ch.bod.nfcMusic.Logger.*;


import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

public class AcsDirectChannelTag extends Tag implements ApduTag {


	private Card card;

	public AcsDirectChannelTag(TagType tagType, byte[] generalBytes, Card card) {
		super(tagType, generalBytes);
		this.card = card;
	}

	@Override
	public Response transmit(Command command) {
		try {
			CommandAPDU commandAPDU = null;
			if (command.isDataOnly()) {
				commandAPDU = new CommandAPDU(0xff, 0, 0, 0, command.getData(), command.getOffset(),
						command.getLength());
			}
			else
				throw new NfcException("Only data mode supported");
			byte[] transmitControlResponse = card.transmitControlCommand(Acs.IOCTL_SMARTCARD_ACR122_ESCAPE_COMMAND,
					commandAPDU.getBytes());
			ResponseAPDU responseAPDU = new ResponseAPDU(transmitControlResponse);
			return new Response(responseAPDU.getSW1(), responseAPDU.getSW2(), responseAPDU.getData());
		}
		catch (CardException e) {
			throw new NfcException(e);
		}
	}

}
