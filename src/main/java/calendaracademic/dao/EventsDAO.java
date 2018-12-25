package calendaracademic.dao;

import calendaracademic.response.Invitations;

import javax.servlet.http.HttpServletRequest;

public interface EventsDAO {

    Invitations[] getInvitations(HttpServletRequest request);

}
