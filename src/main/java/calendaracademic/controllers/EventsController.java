package calendaracademic.controllers;

import calendaracademic.dao.EventsDAO;
import calendaracademic.dto.InvitationDTO;
import calendaracademic.response.Invitations;
import calendaracademic.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PutMapping(value = "/service/respond")
    public ResponseEntity<?> showInvitations (@RequestBody InvitationDTO inv)
    {
        if(EventsDAO.respond(inv)) {
            return ResponseEntity.ok("OK");
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
