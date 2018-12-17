package calendaracademic.dao;

import calendaracademic.model.Password;
import calendaracademic.model.User;
import calendaracademic.response.Login;
import calendaracademic.services.JwtService;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;

@Repository
@Transactional
public class LoginDAOI implements LoginDAO {

    @Autowired
    static private SessionFactory sessionFactory;

    @Value("${jwt.auth.header}")
    String authHeader;

    @Autowired
    private JwtService jwtTokenService;

    public LoginDAOI(){ }

    public LoginDAOI(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean get(String username, String pass)
    {
        Password p = null;
        String hql = "from Password where hash='";
        hql += pass + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<Password> list = (List<Password>) query.list();

            if (list != null && !list.isEmpty()) {
                p = list.get(0);
            }
        }
        catch(Exception e) {
            return false;
        }

        if(p != null) {

            hql = "from User where username='" + username + "' and password= '" + p.getId() + "'";
            try {
                Query query = sessionFactory.getCurrentSession().createQuery(hql);

                @SuppressWarnings("unchecked")
                List<User> list = (List<User>) query.list();

                if (list != null && !list.isEmpty())
                    return true;
            } catch (Exception e) {
                return false;
            }
        }

        return false;

    }

    @Override
    public boolean setPassword (HttpServletRequest request, String pass)
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);
        User u = null;


        String hql = "from User where username='";
        hql += jwtUser.getUserName() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<User> list = (List<User>) query.list();

            if (list != null && !list.isEmpty()) {
                u = list.get(0);
                hql = "update User set first_login='true' where user_id='" + u.getId() + "'";
                query = sessionFactory.getCurrentSession().createQuery(hql);
                query.executeUpdate();

                hql = "update Password set pass_hashed='" + pass +"' where user_id='" + u.getPass() + "'";
                query = sessionFactory.getCurrentSession().createQuery(hql);
                query.executeUpdate();
            }
        }
        catch(Exception e) {
            return false;
        }

        return true;

    }

    @Override
    public String logout(HttpServletRequest request) {

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);

        return authHeaderVal;
    }
}
