package v3;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class VisitDate {

    private final List<Integer> specialDate = List.of(3, 10, 17, 24, 25, 31);
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

    public boolean isSpecialDay() {
        return specialDate.contains(this.date.getDayOfMonth());
    }
}
