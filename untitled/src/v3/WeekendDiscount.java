package v3;

public class WeekendDiscount implements Discount {
    @Override
    public boolean isApplicable(VisitDate date) {
        return date.isWeekend();
    }
}
