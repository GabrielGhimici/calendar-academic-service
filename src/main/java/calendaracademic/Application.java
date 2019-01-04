package calendaracademic;

import calendaracademic.services.Watchdog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
// We need the following in order to detect the filter
// from the security package
@ServletComponentScan(value="calendaracademic.services")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        Watchdog watcher = new Watchdog("Watchdog");
        watcher.run();
    }
}