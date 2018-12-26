package calendaracademic.dao;

import calendaracademic.POJO.NormalEventsPOJO;
import calendaracademic.POJO.PrivateEventsPOJO;
import calendaracademic.POJO.PrivateRecurentEventsPOJO;
import calendaracademic.POJO.RecurentEventsPOJO;
import calendaracademic.dto.*;
import calendaracademic.response.Invitations;

import javax.servlet.http.HttpServletRequest;

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
}
