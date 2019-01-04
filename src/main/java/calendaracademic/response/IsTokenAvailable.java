package calendaracademic.response;

public class IsTokenAvailable {

    private boolean isValid;

    public IsTokenAvailable(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
