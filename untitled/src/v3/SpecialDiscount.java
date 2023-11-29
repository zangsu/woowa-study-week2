package v3;

public class SpecialDiscount implements Discount {
    @Override
    public boolean isApplicable(VisitDate date) {
        return date.isSpecialDay();
    }
}
