package calendaracademic.dao;

import calendaracademic.POJO.*;
import calendaracademic.dto.*;
import calendaracademic.model.*;
import calendaracademic.response.Invitations;
import calendaracademic.response.Login;
import calendaracademic.services.JwtService;
import calendaracademic.utils.EventUtils;
import javafx.util.Pair;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static calendaracademic.utils.EventUtils.getInternalEventId;

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

                                            event.setId(EventUtils.generateExternalEventId(listN.get(0).getId(),
                                                    EventUtils.NORMAL_EVENT));
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
                                            event.setId(EventUtils.generateExternalEventId(listP.get(0).getId(),
                                                    EventUtils.PRIVATE_EVENT));
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
                                            event.setId(EventUtils.generateExternalEventId(listRN.get(0).getId(),
                                                    EventUtils.RECURRENT_EVENT));
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
                                            event.setId(EventUtils.generateExternalEventId(listPR.get(0).getId(),
                                                    EventUtils.PRIVATE_RECURRENT_EVENT));
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

    public boolean setNormalEvents(HttpServletRequest request, NormalEventDTO event)
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);

        Normal_Event e = new Normal_Event();
        e.setDescription(event.getEvent_description());
        e.setEnd_date(event.getEnd_date());
        e.setEnd_time(event.getEnd_hour());
        e.setStart_date(event.getStart_date());
        e.setStart_time(event.getStart_hour());
        e.setLocation(event.getLocation());
        e.setName(event.getName());

        try {
            String hql = "select MAX(id) from Normal_Event";
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            e.setId(((Long) query.list().get(0)) + 1);

            sessionFactory.getCurrentSession().save(e);

            Participation p = new Participation();
            p.setNormal_event(e.getId());
            p.setOwn_end_time(event.getEnd_hour());
            p.setOwn_start_time(event.getStart_hour());
            p.setPreffered(true);

            hql = "from User where username = '" + jwtUser.getUserName() + "'";
            query = sessionFactory.getCurrentSession().createQuery(hql);

            p.setUser(((User) query.list().get(0)).getId());

            hql = "select MAX(participation_id) from Participation";
            query = sessionFactory.getCurrentSession().createQuery(hql);

            p.setParticipation_id((Long) query.list().get(0) + 1);

            sessionFactory.getCurrentSession().save(p);


        }catch (Exception ex)
        {
            return false;
        }

        return true;
    }

    public boolean setPrivateEvents(HttpServletRequest request, PrivateEventDTO event)
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);

        Private_Event e = new Private_Event();
        e.setDescription(event.getEvent_description());
        e.setEnd_date(event.getEnd_date());
        e.setEnd_time(event.getEnd_hour());
        e.setStart_date(event.getStart_date());
        e.setStart_time(event.getStart_hour());
        e.setLocation(event.getLocation());
        e.setName(event.getName());

        try {
            String hql = "select MAX(id) from Private_Event";
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            e.setId(((Long) query.list().get(0)) + 1);

            hql = "from User where username = '" + jwtUser.getUserName() + "'";
            query = sessionFactory.getCurrentSession().createQuery(hql);

            e.setOwner(((User) query.list().get(0)).getId());

            sessionFactory.getCurrentSession().save(e);

            Participation p = new Participation();
            p.setPrivate_event(e.getId());
            p.setOwn_end_time(event.getEnd_hour());
            p.setOwn_start_time(event.getStart_hour());
            p.setPreffered(true);

            p.setUser(((User) query.list().get(0)).getId());

            hql = "select MAX(participation_id) from Participation";
            query = sessionFactory.getCurrentSession().createQuery(hql);

            p.setParticipation_id((Long) query.list().get(0) + 1);

            sessionFactory.getCurrentSession().save(p);


        }catch (Exception ex)
        {
            return false;
        }

        return true;
    }

    public boolean setPrivateRecurentEvents(HttpServletRequest request, PrivateRecurentEventDTO event)
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);

        Private_Recurent_Event e = new Private_Recurent_Event();
        e.setDescription(event.getEvent_description());
        e.setEnd_date(event.getEnd_date());
        e.setEnd_time(event.getEnd_hour());
        e.setStart_date(event.getStart_date());
        e.setStart_time(event.getStart_hour());
        e.setLocation(event.getLocation());
        e.setName(event.getName());
        e.setFrequency(event.getFrequency());
        e.setRecurring_days(event.getRecurring_days().toLowerCase());

        try {
            String hql = "select MAX(id) from Private_Recurent_Event";
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            e.setId(((Long) query.list().get(0)) + 1);

            hql = "from User where username = '" + jwtUser.getUserName() + "'";
            query = sessionFactory.getCurrentSession().createQuery(hql);

            e.setOwner(((User) query.list().get(0)).getId());

            sessionFactory.getCurrentSession().save(e);

            Participation p = new Participation();
            p.setPrivate_recurent_event(e.getId());
            p.setOwn_end_time(event.getEnd_hour());
            p.setOwn_start_time(event.getStart_hour());
            p.setPreffered(true);

            p.setUser(((User) query.list().get(0)).getId());

            hql = "select MAX(participation_id) from Participation";
            query = sessionFactory.getCurrentSession().createQuery(hql);

            p.setParticipation_id((Long) query.list().get(0) + 1);

            sessionFactory.getCurrentSession().save(p);


        }catch (Exception ex)
        {
            return false;
        }

        return true;
    }

    public boolean setRecurentEvents(HttpServletRequest request, RecurentEventDTO event)
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);

        Recurent_Event e = new Recurent_Event();
        e.setDescription(event.getEvent_description());
        e.setEnd_date(event.getEnd_date());
        e.setEnd_time(event.getEnd_hour());
        e.setStart_date(event.getStart_date());
        e.setStart_time(event.getStart_hour());
        e.setLocation(event.getLocation());
        e.setName(event.getName());
        e.setFrequency(event.getFrequency());
        e.setRecurring_days(event.getRecurring_days().toLowerCase());

        try {
            String hql = "select MAX(id) from Recurent_Event";
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            e.setId(((Long) query.list().get(0)) + 1);

            sessionFactory.getCurrentSession().save(e);

            Participation p = new Participation();
            p.setRecurent_event(e.getId());
            p.setOwn_end_time(event.getEnd_hour());
            p.setOwn_start_time(event.getStart_hour());
            p.setPreffered(true);

            hql = "from User where username = '" + jwtUser.getUserName() + "'";
            query = sessionFactory.getCurrentSession().createQuery(hql);

            p.setUser(((User) query.list().get(0)).getId());

            hql = "select MAX(participation_id) from Participation";
            query = sessionFactory.getCurrentSession().createQuery(hql);

            p.setParticipation_id((Long) query.list().get(0) + 1);

            sessionFactory.getCurrentSession().save(p);


        }catch (Exception ex)
        {
            return false;
        }

        return true;
    }


    //get events serialized
    public EventsPOJO[] getEventsSerialized(HttpServletRequest request, Date before, Date after)
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);
        ArrayList<EventsPOJO> vect = new ArrayList<>();

        String hql = "from User where username = '" + jwtUser.getUserName() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<User> list = (List<User>) query.list();

            if (list != null && !list.isEmpty()) {

                hql = "select e from Participation p, Normal_Event e where p.user = '" + list.get(0).getId() + "'" +
                        " and p.normal_event=e.id and ((e.start_date between '" + after +
                        "' and '" + before + "') or (e.end_date between '" + after + "' and '" + before + "'))";
                try {
                    query = sessionFactory.getCurrentSession().createQuery(hql);

                    List<Normal_Event> listN = (List<Normal_Event>) query.list();

                    if (listN != null && !listN.isEmpty()) {
                        for (int cnt = 0; cnt < listN.size(); cnt++) {
                            EventsPOJO event = new EventsPOJO();
                            event.setId(EventUtils.generateExternalEventId(listN.get(cnt).getId(),
                                    EventUtils.NORMAL_EVENT));
                            event.setEnd_date(listN.get(cnt).getEnd_date());
                            event.setEnd_hour(listN.get(cnt).getEnd_time());
                            event.setStart_date(listN.get(cnt).getStart_date());
                            event.setStart_hour(listN.get(cnt).getStart_time());
                            event.setEvent_description(listN.get(cnt).getDescription());
                            event.setLocation(listN.get(cnt).getLocation());
                            event.setName(listN.get(cnt).getName());
                            event.setRecurrent(false);
                            vect.add(event);
                        }
                    }
                } catch (Exception e) {
                    return null;
                }

                hql = "select e from Participation p, Private_Event e where p.user = '" + list.get(0).getId() + "'" +
                        "and p.private_event=e.id and ((e.start_date between '" + after +
                        "' and '" + before + "') or (e.end_date between '" + after + "' and '" + before + "'))";
                try {
                    query = sessionFactory.getCurrentSession().createQuery(hql);

                    @SuppressWarnings("unchecked")
                    List<Private_Event> listP = (List<Private_Event>) query.list();

                    if (listP != null && !listP.isEmpty()) {
                        for (int cnt = 0; cnt < listP.size(); cnt++) {
                            EventsPOJO event = new EventsPOJO();
                            event.setId(EventUtils.generateExternalEventId(listP.get(cnt).getId(),
                                    EventUtils.PRIVATE_EVENT));
                            event.setEnd_date(listP.get(cnt).getEnd_date());
                            event.setEnd_hour(listP.get(cnt).getEnd_time());
                            event.setStart_date(listP.get(cnt).getStart_date());
                            event.setStart_hour(listP.get(cnt).getStart_time());
                            event.setEvent_description(listP.get(cnt).getDescription());
                            event.setLocation(listP.get(cnt).getLocation());
                            event.setName(listP.get(cnt).getName());
                            event.setRecurrent(false);

                            hql = "from User where user_id = '" + listP.get(cnt).getOwner() + "'";

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
                            vect.add(event);
                        }
                    }
                } catch (Exception e) {
                    return null;
                }

                hql = "select e from Participation p, Recurent_Event e where p.user = '" + list.get(0).getId() + "'" +
                        "and p.recurent_event=e.id and ((e.start_date between '" + after +
                        "' and '" + before + "') or (e.end_date between '" + after + "' and '" + before + "'))";
                try {
                    query = sessionFactory.getCurrentSession().createQuery(hql);
                    @SuppressWarnings("unchecked")
                    List<Recurent_Event> listRN = (List<Recurent_Event>) query.list();

                    if (listRN != null && !listRN.isEmpty()) {
                        for (int cnt = 0; cnt < listRN.size(); cnt++) {
                            EventsPOJO event = new EventsPOJO();
                            event.setId(EventUtils.generateExternalEventId(listRN.get(cnt).getId(),
                                    EventUtils.RECURRENT_EVENT));
                            event.setEnd_date(listRN.get(cnt).getEnd_date());
                            event.setEnd_hour(listRN.get(cnt).getEnd_time());
                            event.setStart_date(listRN.get(cnt).getStart_date());
                            event.setStart_hour(listRN.get(cnt).getStart_time());
                            event.setEvent_description(listRN.get(cnt).getDescription());
                            event.setLocation(listRN.get(cnt).getLocation());
                            event.setName(listRN.get(cnt).getName());
                            event.setRecurrent(true);
                            event.setFrequency(listRN.get(cnt).getFrequency());
                            event.setRecurring_days(listRN.get(cnt).getRecurring_days());

                            vect.add(event);
                        }
                    }
                } catch (Exception e) {
                    return null;
                }

                hql = "select e from Participation p, Private_Recurent_Event e where p.user = '" + list.get(0).getId() + "'" +
                        "and p.private_recurent_event=e.id and ((e.start_date between '" + after +
                        "' and '" + before + "') or (e.end_date between '" + after + "' and '" + before + "'))";
                try {
                    query = sessionFactory.getCurrentSession().createQuery(hql);

                    @SuppressWarnings("unchecked")
                    List<Private_Recurent_Event> listPR = (List<Private_Recurent_Event>) query.list();

                    if (listPR != null && !listPR.isEmpty()) {
                        for (int cnt = 0; cnt < listPR.size(); cnt++) {
                            EventsPOJO event = new EventsPOJO();
                            event.setId(EventUtils.generateExternalEventId(listPR.get(cnt).getId(),
                                    EventUtils.PRIVATE_RECURRENT_EVENT));
                            event.setEnd_date(listPR.get(cnt).getEnd_date());
                            event.setEnd_hour(listPR.get(cnt).getEnd_time());
                            event.setStart_date(listPR.get(cnt).getStart_date());
                            event.setStart_hour(listPR.get(cnt).getStart_time());
                            event.setEvent_description(listPR.get(cnt).getDescription());
                            event.setLocation(listPR.get(cnt).getLocation());
                            event.setName(listPR.get(cnt).getName());
                            event.setRecurrent(true);
                            event.setFrequency(listPR.get(cnt).getFrequency());
                            event.setRecurring_days(listPR.get(cnt).getRecurring_days());

                            hql = "from User where user_id = '" + listPR.get(cnt).getOwner() + "'";

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

                            vect.add(event);
                        }
                    }
                } catch (Exception e) {
                    return null;
                }

                return vect.toArray( new EventsPOJO[vect.size()]);
            }
        } catch (Exception e)
        {
            return null;
        }

        return null;
    }

    public EventsPOJO getEvent(HttpServletRequest request, Long id)
    {
        EventsPOJO event = new EventsPOJO();
        String hql = "";

        Pair<Integer,Long> pair = EventUtils.getInternalEventId(id);

        Integer event_type = pair.getKey();

        switch (event_type.intValue())
        {
            case 1:
                hql = "from Normal_Event where event_id = '" + pair.getValue() + "'";
                break;
            case 2:
                hql = "from Recurent_Event where event_id = '" + pair.getValue() + "'";
                break;
            case 3:
                hql = "from Private_Event where event_id = '" + pair.getValue() + "'";
                break;
            case 4:
                hql = "from Private_Recurent_Event where event_id = '" + pair.getValue() + "'";
                break;
            default:
                return null;
        }

        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            switch (event_type.intValue())
            {
                case 1:
                    @SuppressWarnings("unchecked")
                    List<Normal_Event> listN = (List<Normal_Event>) query.list();

                    if (listN != null && !listN.isEmpty()) {
                        event.setId(EventUtils.generateExternalEventId(listN.get(0).getId(),
                                EventUtils.NORMAL_EVENT));
                        event.setEnd_date(listN.get(0).getEnd_date());
                        event.setEnd_hour(listN.get(0).getEnd_time());
                        event.setStart_date(listN.get(0).getStart_date());
                        event.setStart_hour(listN.get(0).getStart_time());
                        event.setEvent_description(listN.get(0).getDescription());
                        event.setLocation(listN.get(0).getLocation());
                        event.setName(listN.get(0).getName());
                        event.setRecurrent(false);

                        return event;
                    }

                    break;
                case 2:
                    @SuppressWarnings("unchecked")
                    List<Recurent_Event> listRN = (List<Recurent_Event>) query.list();hql = "from Recurent_Event where event_id = '" + pair.getValue() + "'";

                    if (listRN != null && !listRN.isEmpty()) {

                        event.setId(EventUtils.generateExternalEventId(listRN.get(0).getId(),
                                EventUtils.RECURRENT_EVENT));
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

                        return event;
                    }

                    break;
                case 3:
                    List<Private_Event> listP = (List<Private_Event>) query.list();

                    if (listP != null && !listP.isEmpty()) {
                        event.setId(EventUtils.generateExternalEventId(listP.get(0).getId(),
                                EventUtils.PRIVATE_EVENT));
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
                        return event;
                    }

                    break;
                case 4:
                    @SuppressWarnings("unchecked")
                    List<Private_Recurent_Event> listPR = (List<Private_Recurent_Event>) query.list();hql = "from Private_Recurent_Event where event_id = '" + pair.getValue() + "'";

                    if (listPR != null && !listPR.isEmpty()) {
                        event.setId(EventUtils.generateExternalEventId(listPR.get(0).getId(),
                                EventUtils.PRIVATE_RECURRENT_EVENT));
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

                        return event;
                    }

                    break;
                default:
                    return null;
            }
        }catch (Exception e)
        {
            return null;
        }

        return null;
    }

    //get events serialized
    public EventsPOJO[] getPrefferedEventsSerialized(HttpServletRequest request, Date before, Date after)
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);
        ArrayList<EventsPOJO> vect = new ArrayList<>();

        String hql = "from User where username = '" + jwtUser.getUserName() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<User> list = (List<User>) query.list();

            if (list != null && !list.isEmpty()) {

                hql = "select e from Participation p, Normal_Event e where p.user = '" + list.get(0).getId() + "'" +
                        " and p.normal_event=e.id and p.preffered = 'true' and ((e.start_date between '" + after +
                        "' and '" + before + "') or (e.end_date between '" + after + "' and '" + before + "'))";
                try {
                    query = sessionFactory.getCurrentSession().createQuery(hql);

                    List<Normal_Event> listN = (List<Normal_Event>) query.list();

                    if (listN != null && !listN.isEmpty()) {
                        for (int cnt = 0; cnt < listN.size(); cnt++) {
                            EventsPOJO event = new EventsPOJO();
                            event.setId(EventUtils.generateExternalEventId(listN.get(cnt).getId(),
                                    EventUtils.NORMAL_EVENT));
                            event.setEnd_date(listN.get(cnt).getEnd_date());
                            event.setEnd_hour(listN.get(cnt).getEnd_time());
                            event.setStart_date(listN.get(cnt).getStart_date());
                            event.setStart_hour(listN.get(cnt).getStart_time());
                            event.setEvent_description(listN.get(cnt).getDescription());
                            event.setLocation(listN.get(cnt).getLocation());
                            event.setName(listN.get(cnt).getName());
                            event.setRecurrent(false);

                            hql = "from Participation p where p.user = '" + list.get(0).getId() + "'" +
                                    " and p.normal_event='" + listN.get(cnt).getId() + "'";
                            try {
                                query = sessionFactory.getCurrentSession().createQuery(hql);

                                List<Participation> listParticipation = (List<Participation>) query.list();

                                if (listParticipation != null && !listParticipation.isEmpty()){
                                    event.setStart_hour(listParticipation.get(0).getOwn_start_time());
                                    event.setEnd_hour(listParticipation.get(0).getOwn_end_time());
                                }
                            }catch (Exception e)
                            {
                                return null;
                            }

                            vect.add(event);
                        }
                    }
                } catch (Exception e) {
                    return null;
                }

                hql = "select e from Participation p, Private_Event e where p.user = '" + list.get(0).getId() + "'" +
                        "and p.private_event=e.id and p.preffered = 'true' and ((e.start_date between '" + after +
                        "' and '" + before + "') or (e.end_date between '" + after + "' and '" + before + "'))";
                try {
                    query = sessionFactory.getCurrentSession().createQuery(hql);

                    @SuppressWarnings("unchecked")
                    List<Private_Event> listP = (List<Private_Event>) query.list();

                    if (listP != null && !listP.isEmpty()) {
                        for (int cnt = 0; cnt < listP.size(); cnt++) {
                            EventsPOJO event = new EventsPOJO();
                            event.setId(EventUtils.generateExternalEventId(listP.get(cnt).getId(),
                                    EventUtils.PRIVATE_EVENT));
                            event.setEnd_date(listP.get(cnt).getEnd_date());
                            event.setEnd_hour(listP.get(cnt).getEnd_time());
                            event.setStart_date(listP.get(cnt).getStart_date());
                            event.setStart_hour(listP.get(cnt).getStart_time());
                            event.setEvent_description(listP.get(cnt).getDescription());
                            event.setLocation(listP.get(cnt).getLocation());
                            event.setName(listP.get(cnt).getName());
                            event.setRecurrent(false);

                            hql = "from User where user_id = '" + listP.get(cnt).getOwner() + "'";

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

                            hql = "from Participation p where p.user = '" + list.get(0).getId() + "'" +
                                    " and p.private_event='" + listP.get(cnt).getId() + "'";
                            try {
                                query = sessionFactory.getCurrentSession().createQuery(hql);

                                List<Participation> listParticipation = (List<Participation>) query.list();

                                if (listParticipation != null && !listParticipation.isEmpty()){
                                    event.setStart_hour(listParticipation.get(0).getOwn_start_time());
                                    event.setEnd_hour(listParticipation.get(0).getOwn_end_time());
                                }
                            }catch (Exception e)
                            {
                                return null;
                            }

                            vect.add(event);
                        }
                    }
                } catch (Exception e) {
                    return null;
                }

                hql = "select e from Participation p, Recurent_Event e where p.user = '" + list.get(0).getId() + "'" +
                        "and p.recurent_event=e.id and p.preffered = 'true' and ((e.start_date between '" + after +
                        "' and '" + before + "') or (e.end_date between '" + after + "' and '" + before + "'))";
                try {
                    query = sessionFactory.getCurrentSession().createQuery(hql);
                    @SuppressWarnings("unchecked")
                    List<Recurent_Event> listRN = (List<Recurent_Event>) query.list();

                    if (listRN != null && !listRN.isEmpty()) {
                        for (int cnt = 0; cnt < listRN.size(); cnt++) {
                            EventsPOJO event = new EventsPOJO();
                            event.setId(EventUtils.generateExternalEventId(listRN.get(cnt).getId(),
                                    EventUtils.RECURRENT_EVENT));
                            event.setEnd_date(listRN.get(cnt).getEnd_date());
                            event.setEnd_hour(listRN.get(cnt).getEnd_time());
                            event.setStart_date(listRN.get(cnt).getStart_date());
                            event.setStart_hour(listRN.get(cnt).getStart_time());
                            event.setEvent_description(listRN.get(cnt).getDescription());
                            event.setLocation(listRN.get(cnt).getLocation());
                            event.setName(listRN.get(cnt).getName());
                            event.setRecurrent(true);
                            event.setFrequency(listRN.get(cnt).getFrequency());
                            event.setRecurring_days(listRN.get(cnt).getRecurring_days());

                            hql = "from Participation p where p.user = '" + list.get(0).getId() + "'" +
                                    " and p.recurent_event='" + listRN.get(cnt).getId() + "'";
                            try {
                                query = sessionFactory.getCurrentSession().createQuery(hql);

                                List<Participation> listParticipation = (List<Participation>) query.list();

                                if (listParticipation != null && !listParticipation.isEmpty()){
                                    event.setStart_hour(listParticipation.get(0).getOwn_start_time());
                                    event.setEnd_hour(listParticipation.get(0).getOwn_end_time());
                                }
                            }catch (Exception e)
                            {
                                return null;
                            }

                            vect.add(event);
                        }
                    }
                } catch (Exception e) {
                    return null;
                }

                hql = "select e from Participation p, Private_Recurent_Event e where p.user = '" + list.get(0).getId() + "'" +
                        "and p.private_recurent_event=e.id and p.preffered = 'true' and ((e.start_date between '" + after +
                        "' and '" + before + "') or (e.end_date between '" + after + "' and '" + before + "'))";
                try {
                    query = sessionFactory.getCurrentSession().createQuery(hql);

                    @SuppressWarnings("unchecked")
                    List<Private_Recurent_Event> listPR = (List<Private_Recurent_Event>) query.list();

                    if (listPR != null && !listPR.isEmpty()) {
                        for (int cnt = 0; cnt < listPR.size(); cnt++) {
                            EventsPOJO event = new EventsPOJO();
                            event.setId(EventUtils.generateExternalEventId(listPR.get(cnt).getId(),
                                    EventUtils.PRIVATE_RECURRENT_EVENT));
                            event.setEnd_date(listPR.get(cnt).getEnd_date());
                            event.setEnd_hour(listPR.get(cnt).getEnd_time());
                            event.setStart_date(listPR.get(cnt).getStart_date());
                            event.setStart_hour(listPR.get(cnt).getStart_time());
                            event.setEvent_description(listPR.get(cnt).getDescription());
                            event.setLocation(listPR.get(cnt).getLocation());
                            event.setName(listPR.get(cnt).getName());
                            event.setRecurrent(true);
                            event.setFrequency(listPR.get(cnt).getFrequency());
                            event.setRecurring_days(listPR.get(cnt).getRecurring_days());

                            hql = "from User where user_id = '" + listPR.get(cnt).getOwner() + "'";

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

                            hql = "from Participation p where p.user = '" + list.get(0).getId() + "'" +
                                    " and p.private_recurent_event='" + listPR.get(cnt).getId() + "'";
                            try {
                                query = sessionFactory.getCurrentSession().createQuery(hql);

                                List<Participation> listParticipation = (List<Participation>) query.list();

                                if (listParticipation != null && !listParticipation.isEmpty()){
                                    event.setStart_hour(listParticipation.get(0).getOwn_start_time());
                                    event.setEnd_hour(listParticipation.get(0).getOwn_end_time());
                                }
                            }catch (Exception e)
                            {
                                return null;
                            }

                            vect.add(event);
                        }
                    }
                } catch (Exception e) {
                    return null;
                }

                return vect.toArray( new EventsPOJO[vect.size()]);
            }
        } catch (Exception e)
        {
            return null;
        }

        return null;
    }

   /* public boolean updateNormalEvent(NormalEventDTO event, Long id)
    {
        String hql = "from Normal_Event where event_id = '" + id + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<Normal_Event> listN = (List<Normal_Event>) query.list();

            if (listN != null && !listN.isEmpty()) {
                event.set
            }
        }
    }

    boolean deleteEvent(HttpServletRequest request, Long id);
*/
}
