package v1;

import java.time.LocalDate;

public class VisitDate {
    private final LocalDate date;

    private VisitDate(LocalDate date) {
        this.date = date;
    }

    public static VisitDate from(int date){
        return new VisitDate(LocalDate.of(2023, 12, date));
    }

    public LocalDate getDate() {
        return date;
    }
}
