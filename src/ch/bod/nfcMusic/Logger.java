package ch.bod.nfcMusic;

public class Logger {
    public static void error(Throwable ò_ó)
    {
        System.out.println("Logger-error: " + ò_ó);
        ò_ó.printStackTrace();
        System.out.println("..............................");
    }

    public static void warning(Exception ò_ó)
    {
        System.out.println("Logger-warning: " + ò_ó);
    }

    public static void info(String ò_ó)
    {
        System.out.println("Logger-info: " + ò_ó);
    }

    public static void debug(String ò_ó)
    {
        System.out.println("Logger-Debug: " + ò_ó);
    }


}
