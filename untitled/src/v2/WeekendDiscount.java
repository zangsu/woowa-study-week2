package v2;

import java.time.DayOfWeek;

public class WeekendDiscount implements Discount {
    @Override
    public boolean isApplicable(VisitDate date) {
        return date.isWeekend();
    }
}
