package v2;

import java.util.List;

public class SpecialDiscount implements Discount {
    List<Integer> specialDate = List.of(3, 10, 17, 24, 25, 31);
    @Override
    public boolean isApplicable(VisitDate date) {
        return date.isDayOf(specialDate);
    }
}
