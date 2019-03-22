package ch.sky.web;

public class Util_NfcSession {

    private static WebApp webApp;

    public Util_NfcSession(WebApp webApp) {
        this.webApp = webApp;
    }

    public static WebApp getWebApp() {
        return webApp;
    }

    public static void setWebApp(WebApp webApp) {
        Util_NfcSession.webApp = webApp;
    }
}
