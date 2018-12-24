package calendaracademic.dao;

import calendaracademic.response.Details;

import javax.servlet.http.HttpServletRequest;

public interface LoginDAO {

    boolean get(String email, String pass);

    boolean setPassword (HttpServletRequest request, String pass);

    String logout(HttpServletRequest request);

    Details getDetails (HttpServletRequest request);
}
