package ch.bod.nfcMusic.sky.test;

import javax.smartcardio.*;
import java.math.BigInteger;
import java.util.List;

public class tagscan {

    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1, data));
    }

    public static void main(String[] args) {

        while (true) {

            try {
                // Display the list of terminals
                TerminalFactory factory = TerminalFactory.getDefault();
                List<CardTerminal> terminals = factory.terminals().list();
                System.out.println("Terminals: " + terminals);

                // Use the first terminal
                CardTerminal terminal = terminals.get(0);
                // Connect wit hthe card
                if (terminal.isCardPresent()) {
                    Card card = terminal.connect("*");
                    System.out.println("Card: " + card);
                    CardChannel channel = card.getBasicChannel();

                    // Send test command
                    CommandAPDU commandAPDU = new CommandAPDU(new byte[]{(byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00});
                    commandAPDU = new CommandAPDU(new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0x00,
                            (byte) 0x00, (byte) 0x04, (byte) 0xD4, (byte) 0x4A,
                            (byte) 0x01, (byte) 0x00});
                    ResponseAPDU response = channel.transmit(commandAPDU);
                    System.out.println("Response: " + response.toString());

                    if (response.getSW1() == 0x63 && response.getSW2() == 0x00) System.out.println("Failed");

                    System.out.println("UID: " + bin2hex(response.getData()));
                    // Disconnect the card
                    card.disconnect(false);
                }

            } catch (Exception e) {

                System.out.println("Ouch: " + e.toString());

            }
        }
    }
}