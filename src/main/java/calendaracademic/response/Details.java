package calendaracademic.response;

public class Details {

    private String username;

    private String email;

    private String affiliation;

    //true if now is the first login
    private boolean firstLogin;

    private short rights;

    public Details(String email, String affiliation, boolean firstLogin, short rights) {
        String aux = email.replace("."," ");
        aux = aux.substring(0,aux.lastIndexOf("@"));
        this.username = aux;
        this.email = email;
        this.affiliation = affiliation;
        this.firstLogin = firstLogin;
        this.rights = rights;
    }

    public Details(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String email) {
        String aux = email.replace("."," ");
        aux = aux.substring(0,aux.lastIndexOf("@"));
        this.username = aux;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public short getRights() {
        return rights;
    }

    public void setRights(short rights) {
        this.rights = rights;
    }
}
