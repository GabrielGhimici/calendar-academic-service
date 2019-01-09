package calendaracademic.dto;

import java.util.Date;
import java.util.Objects;

public class DateDTO {

    private Date beforeDate;
    private Date afterDate;

    public DateDTO(){}

    public Date getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(Date beforeDate) {
        this.beforeDate = beforeDate;
    }

    public Date getAfterDate() {
        return afterDate;
    }

    public void setAfterDate(Date afterDate) {
        this.afterDate = afterDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateDTO dateDTO = (DateDTO) o;
        return Objects.equals(beforeDate, dateDTO.beforeDate) &&
                Objects.equals(afterDate, dateDTO.afterDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beforeDate, afterDate);
    }
}
