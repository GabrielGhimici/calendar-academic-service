package calendaracademic.dao;

import calendaracademic.POJO.*;
import calendaracademic.dto.InvitationDTO;
import calendaracademic.model.*;
import calendaracademic.response.Invitations;
import calendaracademic.response.Login;
import calendaracademic.services.JwtService;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Repository
@Transactional
public class EventsDAOI implements EventsDAO{

    @Autowired
    static private SessionFactory sessionFactory;

    @Value("${jwt.auth.header}")
    String authHeader;

    @Autowired
    private JwtService jwtTokenService;

    public EventsDAOI(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Invitations[] getInvitations(HttpServletRequest request)
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);

        String hql = "from User where username = '" + jwtUser.getUserName() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<User> list = (List<User>) query.list();

            if (list != null && !list.isEmpty()) {

                hql = "from Invitation where user = '" + list.get(0).getId() + "'";
                try {
                    query = sessionFactory.getCurrentSession().createQuery(hql);

                    @SuppressWarnings("unchecked")
                    List<Invitation>list2 = (List<Invitation>) query.list();

                    if (list2 != null && !list2.isEmpty()) {

                        Invitations[] vect = new Invitations[list2.size()];

                        for(int cnt = 0; cnt < list2.size(); cnt++)
                        {
                            EventsPOJO event = new EventsPOJO();
                            Invitations inv = new Invitations();
                            inv.setId(list2.get(cnt).getId());

                            short event_type = 0;
                            if(list2.get(cnt).getNormal_event() != null)
                                event_type = 1;
                            else
                            {
                                if(list2.get(cnt).getPrivate_event() != null)
                                    event_type = 2;
                                else
                                {
                                    if(list2.get(cnt).getRecurent_event() != null)
                                        event_type = 3;
                                    else
                                        event_type = 4;
                                }
                            }

                            switch (event_type)
                            {
                                case 1:
                                    hql = "from Normal_Event where event_id = '" + list2.get(cnt).getNormal_event() + "'";
                                    break;
                                case 2:
                                    hql = "from Private_Event where event_id = '" + list2.get(cnt).getPrivate_event() + "'";
                                    break;
                                case 3:
                                    hql = "from Recurent_Event where event_id = '" + list2.get(cnt).getRecurent_event() + "'";
                                    break;
                                case 4:
                                    hql = "from Private_Recurent_Event where event_id = '" + list2.get(cnt).getPrivate_recurent_event() + "'";
                                    break;
                                default:
                                    return null;
                            }

                            try {
                                query = sessionFactory.getCurrentSession().createQuery(hql);

                                switch (event_type)
                                {
                                    case 1:
                                        @SuppressWarnings("unchecked")
                                        List<Normal_Event> listN = (List<Normal_Event>) query.list();

                                        if (listN != null && !listN.isEmpty()) {

                                            event.setEnd_date(listN.get(0).getEnd_date());
                                            event.setEnd_hour(listN.get(0).getEnd_time());
                                            event.setStart_date(listN.get(0).getStart_date());
                                            event.setStart_hour(listN.get(0).getStart_time());
                                            event.setEvent_description(listN.get(0).getDescription());
                                            event.setLocation(listN.get(0).getLocation());
                                            event.setName(listN.get(0).getName());
                                            event.setRecurrent(false);
                                        }

                                        break;
                                    case 2:
                                        @SuppressWarnings("unchecked")
                                        List<Private_Event> listP = (List<Private_Event>) query.list();

                                        if (listP != null && !listP.isEmpty()) {
                                            event.setEnd_date(listP.get(0).getEnd_date());
                                            event.setEnd_hour(listP.get(0).getEnd_time());
                                            event.setStart_date(listP.get(0).getStart_date());
                                            event.setStart_hour(listP.get(0).getStart_time());
                                            event.setEvent_description(listP.get(0).getDescription());
                                            event.setLocation(listP.get(0).getLocation());
                                            event.setName(listP.get(0).getName());
                                            event.setRecurrent(false);

                                            hql = "from User where user_id = '" + listP.get(0).getOwner() + "'";

                                            try {

                                                query = sessionFactory.getCurrentSession().createQuery(hql);

                                                @SuppressWarnings("unchecked")
                                                List<User> list4 = (List<User>) query.list();

                                                if (list4 != null && !list4.isEmpty()) {
                                                    String aux = list4.get(0).getUsername().replace(".", " ");
                                                    aux = aux.substring(0, aux.lastIndexOf("@"));
                                                    event.setOwner(aux);
                                                }

                                            } catch (Exception e) {
                                                return null;
                                            }
                                        }

                                        break;
                                    case 3:
                                        @SuppressWarnings("unchecked")
                                        List<Recurent_Event> listRN = (List<Recurent_Event>) query.list();

                                        if (listRN != null && !listRN.isEmpty()) {
                                            event.setEnd_date(listRN.get(0).getEnd_date());
                                            event.setEnd_hour(listRN.get(0).getEnd_time());
                                            event.setStart_date(listRN.get(0).getStart_date());
                                            event.setStart_hour(listRN.get(0).getStart_time());
                                            event.setEvent_description(listRN.get(0).getDescription());
                                            event.setLocation(listRN.get(0).getLocation());
                                            event.setName(listRN.get(0).getName());
                                            event.setRecurrent(true);
                                            event.setFrequency(listRN.get(0).getFrequency());
                                            event.setRecurring_days(listRN.get(0).getRecurring_days());
                                        }

                                        break;
                                    case 4:
                                        @SuppressWarnings("unchecked")
                                        List<Private_Recurent_Event> listPR = (List<Private_Recurent_Event>) query.list();

                                        if (listPR != null && !listPR.isEmpty()) {
                                            event.setEnd_date(listPR.get(0).getEnd_date());
                                            event.setEnd_hour(listPR.get(0).getEnd_time());
                                            event.setStart_date(listPR.get(0).getStart_date());
                                            event.setStart_hour(listPR.get(0).getStart_time());
                                            event.setEvent_description(listPR.get(0).getDescription());
                                            event.setLocation(listPR.get(0).getLocation());
                                            event.setName(listPR.get(0).getName());
                                            event.setRecurrent(true);
                                            event.setFrequency(listPR.get(0).getFrequency());
                                            event.setRecurring_days(listPR.get(0).getRecurring_days());

                                            hql = "from User where user_id = '" + listPR.get(0).getOwner() + "'";

                                            try {

                                                query = sessionFactory.getCurrentSession().createQuery(hql);

                                                @SuppressWarnings("unchecked")
                                                List<User> list4 = (List<User>) query.list();

                                                if (list4 != null && !list4.isEmpty()) {
                                                    String aux = list4.get(0).getUsername().replace(".", " ");
                                                    aux = aux.substring(0, aux.lastIndexOf("@"));
                                                    event.setOwner(aux);
                                                }

                                            } catch (Exception e) {
                                                return null;
                                            }
                                        }

                                        break;
                                    default:
                                        return null;
                                }

                                inv.setEvent(event);

                            }catch (Exception e)
                            {
                                return null;
                            }

                            vect[cnt] = inv;
                        }

                        return vect;
                    }
                }catch (Exception e)
                {
                    return null;
                }
            }
        }catch (Exception e)
        {
            return null;
        }

        return null;
    }

    public boolean respond(InvitationDTO inv) {
        String hql = "from Invitation where invitation_id = '" + inv.getId() + "'";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        @SuppressWarnings("unchecked")
        List<Invitation> list = (List<Invitation>) query.list();

        if (list != null && !list.isEmpty()) {
            if (inv.isAccepted()) {

                short event_type = 0;
                if (list.get(0).getNormal_event() != null)
                    event_type = 1;
                else {
                    if (list.get(0).getPrivate_event() != null)
                        event_type = 2;
                    else {
                        if (list.get(0).getRecurent_event() != null)
                            event_type = 3;
                        else
                            event_type = 4;
                    }
                }

                switch (event_type) {
                    case 1:
                        hql = "from Normal_Event where event_id = '" + list.get(0).getNormal_event() + "'";
                        break;
                    case 2:
                        hql = "from Private_Event where event_id = '" + list.get(0).getPrivate_event() + "'";
                        break;
                    case 3:
                        hql = "from Recurent_Event where event_id = '" + list.get(0).getRecurent_event() + "'";
                        break;
                    case 4:
                        hql = "from Private_Recurent_Event where event_id = '" + list.get(0).getPrivate_recurent_event() + "'";
                        break;
                    default:
                        return false;
                }

                try {
                    query = sessionFactory.getCurrentSession().createQuery(hql);

                    switch (event_type) {
                        case 1:
                            @SuppressWarnings("unchecked")
                            List<Normal_Event> listN = (List<Normal_Event>) query.list();

                            if (listN != null && !listN.isEmpty()) {

                                Participation p = new Participation();
                                p.setNormal_event(listN.get(0).getId());
                                p.setOwn_end_time(listN.get(0).getEnd_time());
                                p.setOwn_start_time(listN.get(0).getStart_time());
                                p.setPreffered(true);
                                p.setUser(list.get(0).getUser());

                                hql = "select MAX(id) from Participation";
                                query = sessionFactory.getCurrentSession().createQuery(hql);

                                p.setParticipation_id(((Long) query.list().get(0)) + 1);

                                sessionFactory.getCurrentSession().save(p);
                            }

                            break;
                        case 2:
                            @SuppressWarnings("unchecked")
                            List<Private_Event> listP = (List<Private_Event>) query.list();

                            if (listP != null && !listP.isEmpty()) {

                                Participation p = new Participation();
                                p.setNormal_event(listP.get(0).getId());
                                p.setOwn_end_time(listP.get(0).getEnd_time());
                                p.setOwn_start_time(listP.get(0).getStart_time());
                                p.setPreffered(true);
                                p.setUser(list.get(0).getUser());

                                hql = "select MAX(id) from Participation";
                                query = sessionFactory.getCurrentSession().createQuery(hql);

                                p.setParticipation_id(((Long) query.list().get(0)) + 1);

                                sessionFactory.getCurrentSession().save(p);

                            }

                            break;
                        case 3:
                            @SuppressWarnings("unchecked")
                            List<Recurent_Event> listRN = (List<Recurent_Event>) query.list();

                            if (listRN != null && !listRN.isEmpty()) {

                                Participation p = new Participation();
                                p.setNormal_event(listRN.get(0).getId());
                                p.setOwn_end_time(listRN.get(0).getEnd_time());
                                p.setOwn_start_time(listRN.get(0).getStart_time());
                                p.setPreffered(true);
                                p.setUser(list.get(0).getUser());

                                hql = "select MAX(id) from Participation";
                                query = sessionFactory.getCurrentSession().createQuery(hql);

                                p.setParticipation_id(((Long) query.list().get(0)) + 1);

                                sessionFactory.getCurrentSession().save(p);

                            }

                            break;
                        case 4:
                            @SuppressWarnings("unchecked")
                            List<Private_Recurent_Event> listPR = (List<Private_Recurent_Event>) query.list();

                            if (listPR != null && !listPR.isEmpty()) {

                                Participation p = new Participation();
                                p.setNormal_event(listPR.get(0).getId());
                                p.setOwn_end_time(listPR.get(0).getEnd_time());
                                p.setOwn_start_time(listPR.get(0).getStart_time());
                                p.setPreffered(true);
                                p.setUser(list.get(0).getUser());

                                hql = "select MAX(participation_id) from Participation";
                                query = sessionFactory.getCurrentSession().createQuery(hql);

                                p.setParticipation_id((Long) query.list().get(0) + 1);

                                sessionFactory.getCurrentSession().save(p);

                            }

                            break;
                        default:
                            return false;
                    }

                } catch (Exception e) {
                    return false;
                }
            }

            sessionFactory.getCurrentSession().delete(list.get(0));
        }
        else
            return false;

        return true;
    }

    public NormalEventsPOJO[] getNormalEvents(HttpServletRequest request) {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);

        String hql = "from User where username = '" + jwtUser.getUserName() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<User> list = (List<User>) query.list();

            if (list != null && !list.isEmpty()) {

                hql = "from Participation where user = '" + list.get(0).getId() + "' and normal_event is not null";
                query = sessionFactory.getCurrentSession().createQuery(hql);

                @SuppressWarnings("unchecked")
                List<Participation> list2 = (List<Participation>) query.list();

                if (list2 != null && !list2.isEmpty()) {

                    NormalEventsPOJO[] vect = new NormalEventsPOJO[list2.size()];

                    for(int cnt = 0; cnt < list2.size(); cnt++) {

                        hql = "from Normal_Event where event_id = '" + list2.get(cnt).getNormal_event() + "'";
                        query = sessionFactory.getCurrentSession().createQuery(hql);

                        @SuppressWarnings("unchecked")
                        List<Normal_Event> list3 = (List<Normal_Event>) query.list();

                        NormalEventsPOJO e = new NormalEventsPOJO();
                        e.setEnd_date(list3.get(0).getEnd_date());
                        e.setEnd_hour(list2.get(cnt).getOwn_end_time());
                        e.setStart_date(list3.get(0).getStart_date());
                        e.setStart_hour(list2.get(cnt).getOwn_start_time());
                        e.setEvent_description(list3.get(0).getDescription());
                        e.setLocation(list3.get(0).getLocation());
                        e.setName(list3.get(0).getName());

                        vect[cnt] = e;
                    }

                    return vect;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public PrivateEventsPOJO[] getPrivateEvents(HttpServletRequest request)
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);

        String hql = "from User where username = '" + jwtUser.getUserName() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<User> list = (List<User>) query.list();

            if (list != null && !list.isEmpty()) {

                hql = "from Participation where user = '" + list.get(0).getId() + "' and private_event is not null";
                query = sessionFactory.getCurrentSession().createQuery(hql);

                @SuppressWarnings("unchecked")
                List<Participation> list2 = (List<Participation>) query.list();

                if (list2 != null && !list2.isEmpty()) {

                    PrivateEventsPOJO[] vect = new PrivateEventsPOJO[list2.size()];

                    for(int cnt = 0; cnt < list2.size(); cnt++) {

                        hql = "from Private_Event where event_id = '" + list2.get(cnt).getPrivate_event() + "'";
                        query = sessionFactory.getCurrentSession().createQuery(hql);

                        @SuppressWarnings("unchecked")
                        List<Private_Event> list3 = (List<Private_Event>) query.list();

                        PrivateEventsPOJO e = new PrivateEventsPOJO();
                        e.setEnd_date(list3.get(0).getEnd_date());
                        e.setEnd_hour(list2.get(cnt).getOwn_end_time());
                        e.setStart_date(list3.get(0).getStart_date());
                        e.setStart_hour(list2.get(cnt).getOwn_start_time());
                        e.setEvent_description(list3.get(0).getDescription());
                        e.setLocation(list3.get(0).getLocation());
                        e.setName(list3.get(0).getName());

                        hql = "from User where user_id = '" + list3.get(0).getOwner() + "'";
                        query = sessionFactory.getCurrentSession().createQuery(hql);

                        @SuppressWarnings("unchecked")
                        List<User> list4 = (List<User>) query.list();

                        if (list4 != null && !list4.isEmpty()) {
                            String aux = list4.get(0).getUsername().replace(".", " ");
                            aux = aux.substring(0, aux.lastIndexOf("@"));
                            e.setOwner(aux);
                        }

                        vect[cnt] = e;
                    }

                    return vect;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }


    public PrivateRecurentEventsPOJO[] getPrivateRecurentEvents(HttpServletRequest request)
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);

        String hql = "from User where username = '" + jwtUser.getUserName() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<User> list = (List<User>) query.list();

            if (list != null && !list.isEmpty()) {

                hql = "from Participation where user = '" + list.get(0).getId() + "' and private_recurent_event is not null";
                query = sessionFactory.getCurrentSession().createQuery(hql);

                @SuppressWarnings("unchecked")
                List<Participation> list2 = (List<Participation>) query.list();

                if (list2 != null && !list2.isEmpty()) {

                    PrivateRecurentEventsPOJO[] vect = new PrivateRecurentEventsPOJO[list2.size()];

                    for(int cnt = 0; cnt < list2.size(); cnt++) {

                        hql = "from Private_Recurent_Event where event_id = '" + list2.get(cnt).getPrivate_recurent_event() + "'";
                        query = sessionFactory.getCurrentSession().createQuery(hql);

                        @SuppressWarnings("unchecked")
                        List<Private_Recurent_Event> list3 = (List<Private_Recurent_Event>) query.list();

                        PrivateRecurentEventsPOJO e = new PrivateRecurentEventsPOJO();
                        e.setEnd_date(list3.get(0).getEnd_date());
                        e.setEnd_hour(list2.get(cnt).getOwn_end_time());
                        e.setStart_date(list3.get(0).getStart_date());
                        e.setStart_hour(list2.get(cnt).getOwn_start_time());
                        e.setEvent_description(list3.get(0).getDescription());
                        e.setLocation(list3.get(0).getLocation());
                        e.setName(list3.get(0).getName());

                        hql = "from User where user_id = '" + list3.get(0).getOwner() + "'";
                        query = sessionFactory.getCurrentSession().createQuery(hql);

                        @SuppressWarnings("unchecked")
                        List<User> list4 = (List<User>) query.list();

                        if (list4 != null && !list4.isEmpty()) {
                            String aux = list4.get(0).getUsername().replace(".", " ");
                            aux = aux.substring(0, aux.lastIndexOf("@"));
                            e.setOwner(aux);
                        }

                        e.setFrequency(list3.get(0).getFrequency());
                        e.setRecurring_days(list3.get(0).getRecurring_days());

                        vect[cnt] = e;
                    }

                    return vect;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public RecurentEventsPOJO[] getRecurentEvents(HttpServletRequest request)
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);

        String hql = "from User where username = '" + jwtUser.getUserName() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<User> list = (List<User>) query.list();

            if (list != null && !list.isEmpty()) {

                hql = "from Participation where user = '" + list.get(0).getId() + "' and recurent_event is not null";
                query = sessionFactory.getCurrentSession().createQuery(hql);

                @SuppressWarnings("unchecked")
                List<Participation> list2 = (List<Participation>) query.list();

                if (list2 != null && !list2.isEmpty()) {

                    RecurentEventsPOJO[] vect = new RecurentEventsPOJO[list2.size()];

                    for(int cnt = 0; cnt < list2.size(); cnt++) {

                        hql = "from Recurent_Event where event_id = '" + list2.get(cnt).getRecurent_event() + "'";
                        query = sessionFactory.getCurrentSession().createQuery(hql);

                        @SuppressWarnings("unchecked")
                        List<Recurent_Event> list3 = (List<Recurent_Event>) query.list();

                        RecurentEventsPOJO e = new RecurentEventsPOJO();
                        e.setEnd_date(list3.get(0).getEnd_date());
                        e.setEnd_hour(list2.get(cnt).getOwn_end_time());
                        e.setStart_date(list3.get(0).getStart_date());
                        e.setStart_hour(list2.get(cnt).getOwn_start_time());
                        e.setEvent_description(list3.get(0).getDescription());
                        e.setLocation(list3.get(0).getLocation());
                        e.setName(list3.get(0).getName());
                        e.setFrequency(list3.get(0).getFrequency());
                        e.setRecurring_days(list3.get(0).getRecurring_days());

                        vect[cnt] = e;
                    }

                    return vect;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }
}