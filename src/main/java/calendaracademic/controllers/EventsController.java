package calendaracademic.controllers;

import calendaracademic.POJO.*;
import calendaracademic.dao.EventsDAO;
import calendaracademic.dto.*;
import calendaracademic.response.Invitations;
import calendaracademic.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/service/events")
    public ResponseEntity<?> showEvents (HttpServletRequest request)
    {
        NormalEventsPOJO[] joNE = EventsDAO.getNormalEvents(request);
        PrivateEventsPOJO[] joPE = EventsDAO.getPrivateEvents(request);
        PrivateRecurentEventsPOJO[] joPRE = EventsDAO.getPrivateRecurentEvents(request);
        RecurentEventsPOJO[] joRE = EventsDAO.getRecurentEvents(request);

        Map maping = new HashMap<String,String>();
        maping.put("NormalEvents",  joNE);
        maping.put("PrivateEvents",  joPE);
        maping.put("PrivateRecurentEvents",  joPRE);
        maping.put("RecurentEvents",  joRE);

        return ResponseEntity.ok(maping);
    }


    @PostMapping(value = "/service/serializedEvents")
    public ResponseEntity<?> showEventsSerialized (HttpServletRequest request, @RequestBody DateDTO interval)
    {
        EventsPOJO[] jo = EventsDAO.getEventsSerialized(request,interval.getBeforeDate(),interval.getAfterDate());

        Map maping = new HashMap<String,String>();
        maping.put("events",  jo);

        return ResponseEntity.ok(maping);
    }

    @PostMapping(value = "/service/certainEvent")
    public ResponseEntity<?> showEvent (HttpServletRequest request, @RequestBody EventDTO event)
    {
        EventsPOJO jo = EventsDAO.getEvent(request, event.getId());

        Map maping = new HashMap<String,String>();
        maping.put("event",  jo);

        return ResponseEntity.ok(maping);
    }

    /*@PutMapping(value = "/service/certainEvent")
    public ResponseEntity<?> updateEvent (HttpServletRequest request, @RequestBody EventsPOJO event)
    {
        if(EventsDAO.updateEvent(request, event)) {
            return ResponseEntity.ok("OK");
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }*/

    @PutMapping(value = "/service/createNormalEvent")
    public ResponseEntity<?> setNormalEvent (HttpServletRequest request, @RequestBody NormalEventDTO event)
    {
        if(EventsDAO.setNormalEvents(request, event)) {
            return ResponseEntity.ok("OK");
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/service/createPrivateEvent")
    public ResponseEntity<?> setPrivateEvent (HttpServletRequest request, @RequestBody PrivateEventDTO event)
    {
        if(EventsDAO.setPrivateEvents(request, event)) {
            return ResponseEntity.ok("OK");
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/service/createPrivateRecurentEvent")
    public ResponseEntity<?> setPrivateRecurentEvent (HttpServletRequest request, @RequestBody PrivateRecurentEventDTO event)
    {
        if(EventsDAO.setPrivateRecurentEvents(request, event)) {
            return ResponseEntity.ok("OK");
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/service/createRecurentEvent")
    public ResponseEntity<?> setRecurentEvent (HttpServletRequest request, @RequestBody RecurentEventDTO event)
    {

        if(EventsDAO.setRecurentEvents(request, event)) {
            return ResponseEntity.ok("OK");
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


}
