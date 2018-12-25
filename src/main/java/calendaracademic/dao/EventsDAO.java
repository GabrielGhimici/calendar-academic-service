package calendaracademic.dao;

import calendaracademic.dto.InvitationDTO;
import calendaracademic.response.Invitations;

import javax.servlet.http.HttpServletRequest;

public interface EventsDAO {

    Invitations[] getInvitations(HttpServletRequest request);

    boolean respond(InvitationDTO inv);

}
