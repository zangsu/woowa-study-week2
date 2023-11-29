package v1;

import java.util.List;

public class SpecialDiscount implements Discount{
    List<Integer> specialDate = List.of(3, 10, 17, 24, 25, 31);
    @Override
    public boolean isApplicable(VisitDate date) {
        return specialDate.contains(date.getDate().getDayOfMonth());
    }
}
