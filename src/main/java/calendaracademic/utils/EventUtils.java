package calendaracademic.utils;

import javafx.util.Pair;

public class EventUtils {

    public final static int NORMAL_EVENT = 1;
    public final static int RECURRENT_EVENT = 2;
    public final static int PRIVATE_EVENT = 3;
    public final static int PRIVATE_RECURRENT_EVENT = 4;

    //event_type can be normal event, private event, private recurrent event or recurrent event
    public static Long generateExternalEventId(Long id, int type)
    {
        String auxiliary = "";

        switch (type) {
            case NORMAL_EVENT:
            case RECURRENT_EVENT:
            case PRIVATE_EVENT:
            case PRIVATE_RECURRENT_EVENT:
                auxiliary += String.valueOf(type);
                break;

            default: return null;
        }

        auxiliary += id.toString();

        return new Long(Long.valueOf(auxiliary));
    }

    public static Pair getInternalEventId(Long externalId)
    {
        int type;
        Long internalId;

        String auxiliary = externalId.toString();

        switch (Character.getNumericValue(auxiliary.charAt(0))) {
            case NORMAL_EVENT:
                type = NORMAL_EVENT;
                break;
            case RECURRENT_EVENT:
                type = RECURRENT_EVENT;
                break;
            case PRIVATE_EVENT:
                type = PRIVATE_EVENT;
                break;
            case PRIVATE_RECURRENT_EVENT:
                type = PRIVATE_RECURRENT_EVENT;
                break;

            default: return null;
        }

        auxiliary = auxiliary.substring(1,auxiliary.length());
        internalId = Long.valueOf(auxiliary);

        return new Pair(type,internalId);
    }

}
