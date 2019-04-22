package ch.sky.web.test;

public class MifareTester{

    MifareTester() {
    }

    public static void main(String[] args) {
        MifareTester tester = new MifareTester();
//        tester.write();
        tester.read();
//        tester.cleanCard();
    }

    private void read() {
        RWLauncher launcher = new RWLauncher();
        launcher.setMode(RWLauncher.Mode.READ);
        launcher.launch();
//        launcher.stop();
    }

    private void write() {
        RWLauncher launcher = new RWLauncher();
        launcher.setMode(RWLauncher.Mode.WRITE);
        launcher.launch();
//        launcher.stop();
    }

    private void cleanCard(){
        RWLauncher launcher = new RWLauncher();
        launcher.setMode(RWLauncher.Mode.CLEAN);
        launcher.launch();
    }


}

