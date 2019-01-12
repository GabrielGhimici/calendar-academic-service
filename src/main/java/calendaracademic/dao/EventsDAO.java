package calendaracademic.dao;

import calendaracademic.POJO.*;
import calendaracademic.dto.*;
import calendaracademic.response.Invitations;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public interface EventsDAO {

    Invitations[] getInvitations(HttpServletRequest request);

    boolean respond(InvitationDTO inv);

    NormalEventsPOJO[] getNormalEvents(HttpServletRequest request);

    PrivateEventsPOJO[] getPrivateEvents(HttpServletRequest request);

    PrivateRecurentEventsPOJO[] getPrivateRecurentEvents(HttpServletRequest request);

    RecurentEventsPOJO[] getRecurentEvents(HttpServletRequest request);

    boolean setNormalEvents(HttpServletRequest request, NormalEventDTO event);

    boolean setPrivateEvents(HttpServletRequest request, PrivateEventDTO event);

    boolean setPrivateRecurentEvents(HttpServletRequest request, PrivateRecurentEventDTO event);

    boolean setRecurentEvents(HttpServletRequest request, RecurentEventDTO event);

    EventsPOJO[] getEventsSerialized(HttpServletRequest request, Date before, Date after);

    EventsPOJO getEvent(HttpServletRequest request, Long id);

    /*boolean updateNormalEvent(NormalEventDTO event, Long id);

    boolean deleteEvent(HttpServletRequest request, Long id);*/
}
