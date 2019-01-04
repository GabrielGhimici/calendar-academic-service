package calendaracademic.services;

import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Watchdog implements Runnable {

    private String name;
    private long thread_sleep_time = 5000;

    public Watchdog(String name){
        this.name = name;
    }

    @Override
    public void run() {

        System.out.println(name + ConsoleColours.CYAN + " is running" + ConsoleColours.RESET);

        while(true) {
            try {
                Thread.sleep(thread_sleep_time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
            System.out.println(name + " started FMI schedule: " + ConsoleColours.YELLOW +  timeStamp + ConsoleColours.RESET);
            if(parseFMISchedule())
                System.out.println(name + " succeeded: " + ConsoleColours.BLUE +  timeStamp + ConsoleColours.RESET);
            else
                System.out.println(name + " failed: " + ConsoleColours.RED_BOLD +  timeStamp + ConsoleColours.RESET);
        }
    }

    private boolean parseFMISchedule()
    {
        try {
            File inputFile = new File("orar.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("student");
            System.out.println("----------------------------");
        }catch (Exception e)
        {
            return false;
        }

        return true;
    }

}
