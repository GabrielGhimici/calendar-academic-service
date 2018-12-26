package calendaracademic.dto;

import java.util.Objects;

public class InvitationDTO {

    private long id;

    private boolean response;


    public InvitationDTO(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAccepted() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvitationDTO that = (InvitationDTO) o;
        return id == that.id &&
                response == that.response;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, response);
    }
}
