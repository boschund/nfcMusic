package ch.sky.web.nfc;

public class NdefUltralightTagScanner { //implements TagScannerListener {

//    private static WebApp webAppController;
//
//    public NdefUltralightTagScanner(WebApp webAppController) {
//        this.webAppController = webAppController;
//        NfcListener ndefListener = new NfcListener(webAppController);
//        scanForTags(ndefListener);
//    }
//
//    public static Terminal getAvailableTerminal(String preferredTerminalName) {
//        try {
//            TerminalHandler terminalHandler = new TerminalHandler();
//            terminalHandler.addTerminal(new AcsTerminal());
//            return terminalHandler.getAvailableTerminal(preferredTerminalName);
//        } catch (RuntimeException e){
//            webAppController.showError(e);
//            return null;
//        }
//    }
//
//    private void scanForTags(NfcListener ndefListener) {
//
//        Terminal availableTerminal = null;
//        while (availableTerminal == null){
//            availableTerminal = getAvailableTerminal(null);
//        }
//        NfcAdapter nfcAdapter = new NfcAdapter(availableTerminal, TerminalMode.INITIATOR, this);
//        nfcAdapter.registerTagListener(new Type2NfcTagListener(ndefListener));
//        nfcAdapter.startListening();
//    }
//
//    @Override
//    public void onScanningEnded() {
//        System.out.println("Scanning ended");
//    }
//
//    @Override
//    public void onScanningFailed(Throwable throwable) {
//        webAppController.showError(throwable);
//    }
//
//    @Override
//    public void onTagHandingFailed(Throwable throwable) {
//        webAppController.showError(throwable);
//    }
}
