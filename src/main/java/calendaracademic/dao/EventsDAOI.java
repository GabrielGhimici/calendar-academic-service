package calendaracademic.dao;

import calendaracademic.POJO.EventsPOJO;
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

                                        event.setEnd_date(listN.get(0).getEnd_date());
                                        event.setEnd_hour(listN.get(0).getEnd_time());
                                        event.setStart_date(listN.get(0).getStart_date());
                                        event.setStart_hour(listN.get(0).getStart_time());
                                        event.setEvent_description(listN.get(0).getDescription());
                                        event.setLocation(listN.get(0).getLocation());
                                        event.setName(listN.get(0).getName());
                                        event.setRecurrent(false);

                                        break;
                                    case 2:
                                        @SuppressWarnings("unchecked")
                                        List<Private_Event> listP = (List<Private_Event>) query.list();

                                        event.setEnd_date(listP.get(0).getEnd_date());
                                        event.setEnd_hour(listP.get(0).getEnd_time());
                                        event.setStart_date(listP.get(0).getStart_date());
                                        event.setStart_hour(listP.get(0).getStart_time());
                                        event.setEvent_description(listP.get(0).getDescription());
                                        event.setLocation(listP.get(0).getLocation());
                                        event.setName(listP.get(0).getName());
                                        event.setRecurrent(false);

                                        hql = "from User where user_id = '" + listP.get(0).getOwner() + "'";

                                        try{

                                            query = sessionFactory.getCurrentSession().createQuery(hql);

                                            @SuppressWarnings("unchecked")
                                            List<User> list4 = (List<User>) query.list();

                                            String aux = list4.get(0).getUsername().replace("."," ");
                                            aux = aux.substring(0,aux.lastIndexOf("@"));
                                            event.setOwner(aux);

                                        }catch (Exception e)
                                        {
                                            return null;
                                        }

                                        break;
                                    case 3:
                                        @SuppressWarnings("unchecked")
                                        List<Recurent_Event> listRN = (List<Recurent_Event>) query.list();

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

                                        break;
                                    case 4:
                                        @SuppressWarnings("unchecked")
                                        List<Private_Recurent_Event> listPR = (List<Private_Recurent_Event>) query.list();

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

                                        try{

                                            query = sessionFactory.getCurrentSession().createQuery(hql);

                                            @SuppressWarnings("unchecked")
                                            List<User> list4 = (List<User>) query.list();

                                            String aux = list4.get(0).getUsername().replace("."," ");
                                            aux = aux.substring(0,aux.lastIndexOf("@"));
                                            event.setOwner(aux);

                                        }catch (Exception e)
                                        {
                                            return null;
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
}
