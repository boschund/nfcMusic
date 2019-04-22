package ch.sky.web.test;

import ch.sky.web.rfid.RfidListener;

public class RWLauncher {

    RfidListener listener;

    private Mode mode;

    public enum Mode {READ, WRITE, CLEAN};

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getTest()
    {
//        return "TEST-TEST-TEST-T";
        return "-->> TEST-TEST-TEST-TEST-TEST-TEST-TEST-TEST-TEST-TEST-TEST-TEST-TEST-TEST-TEST <<--";
//        return "fELSENFESTfELSENFESTfELSENFESTfELSESFEST";
    }

    public void setRes(String result)
    {
        System.out.println(":::::" + result);
    }

    public void launch()
    {
        listener = new RfidListener(this);
    }

    public void stop()
    {
        listener.stop();
    }
}
