package ch.bod.nfcMusic;

public class Logger {
    public static void error(Throwable e_e)
    {
        System.out.println("Logger-error: " + e_e);
        e_e.printStackTrace();
        System.out.println("..............................");
    }

    public static void warning(Exception e_e)
    {
        System.out.println("Logger-warning: " + e_e);
    }

    public static void info(String e_e)
    {
        System.out.println("Logger-info: " + e_e);
    }

    public static void debug(String e_e)
    {
        System.out.println("Logger-Debug: " + e_e);
    }


}
