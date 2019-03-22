package org.nfctools.examples.hce;

import org.nfctools.examples.TerminalUtils;

import javax.smartcardio.CardTerminal;

public class HceDemo {

	public void run() {
		CardTerminal cardTerminal = TerminalUtils.getAvailableTerminal().getCardTerminal();
		HostCardEmulationTagScanner tagScanner = new HostCardEmulationTagScanner(cardTerminal);
		tagScanner.run();
	}

	public static void main(String[] args) {
		new HceDemo().run();
	}
}
