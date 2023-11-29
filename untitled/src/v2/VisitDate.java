package v2;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

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

    public boolean isWeekend() {
        DayOfWeek week = date.getDayOfWeek();
        return week == DayOfWeek.FRIDAY || week == DayOfWeek.SATURDAY;
    }

    public boolean isDayOf(List<Integer> specialDate) {
        return specialDate.contains(this.date.getDayOfMonth());
    }
}
