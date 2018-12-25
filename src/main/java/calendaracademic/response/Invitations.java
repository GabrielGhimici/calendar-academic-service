package calendaracademic.response;

import calendaracademic.POJO.EventsPOJO;

public class Invitations {

    private long id;

    private EventsPOJO event;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EventsPOJO getEvent() {
        return event;
    }

    public void setEvent(EventsPOJO event) {
        this.event = event;
    }
}
