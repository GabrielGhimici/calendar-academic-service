package calendaracademic.controllers;

import calendaracademic.dao.EventsDAO;
import calendaracademic.response.Invitations;
import calendaracademic.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class EventsController {

    @Autowired
    private EventsDAO EventsDAO;

    @Autowired
    private JwtService jwtService;

    public EventsController(){}

    @GetMapping(value = "/service/invitations")
    public ResponseEntity<?> showInvitations (HttpServletRequest request)
    {
        Invitations[] jo = EventsDAO.getInvitations(request);

        Map maping = new HashMap<String,String>();
        maping.put("invitations",  jo);

        return ResponseEntity.ok(maping);
    }
}
