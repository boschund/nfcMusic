package ch.sky.web.nfc;

public class NfcListener { //} implements NdefListener, NdefOperationsListener, Sneplet {

//    private WebApp webAppController;
//
//    public NfcListener(WebApp webAppController) {
//        this.webAppController = webAppController;
//    }
//
//    @Override
//    public void onNdefMessages(Collection<Record> records) {
//        for (Record record : records) {
//            String string = record.toString();
//            String[] split = string.split("Text: ");
//            String[] split2 = split[1].split(",");
//            String value = split2[0];
//
//            //Here comes the value
//            webAppController.callURL(value);
//        }
//    }
//
//    @Override
//    public void onNdefOperations(NdefOperations ndefOperations) {
//        if (ndefOperations.isFormatted()) {
//            if (ndefOperations.hasNdefMessage())
//                onNdefMessages(ndefOperations.readNdefMessage());
//            else
//                System.out.println("Empty formatted tag. Size: " + ndefOperations.getMaxSize() + " bytes");
//        } else
//            System.out.println("Empty tag. NOT formatted. Size: " + ndefOperations.getMaxSize() + " bytes");
//    }
//
//    @Override
//    public Collection<Record> doGet(Collection<Record> requestRecords) {
//        System.out.println("SNEP get");
//        onNdefMessages(requestRecords);
//        return null;
//    }
//
//    @Override
//    public void doPut(Collection<Record> requestRecords) {
//        System.out.println("SNEP put");
//        onNdefMessages(requestRecords);
//    }
}
