package v1;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeekendDiscount implements Discount{
    @Override
    public boolean isApplicable(VisitDate date) {
        DayOfWeek week = date.getDate().getDayOfWeek();
        return week == DayOfWeek.FRIDAY || week == DayOfWeek.SATURDAY;
    }
}
