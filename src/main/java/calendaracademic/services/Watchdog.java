package calendaracademic.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Watchdog implements Runnable {

    String name;

    public Watchdog(String name){
        this.name = name;
    }

    @Override
    public void run() {

        System.out.println(name + " is running");

        while(true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
            System.out.println(name + " is watching: " + timeStamp);
        }
    }

}
