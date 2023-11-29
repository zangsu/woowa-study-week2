package v3;

public class Usage {
    public static void main(String[] args) {
        VisitDate date = VisitDate.from(25);

        Discount weekendDiscount = new WeekendDiscount();
        Discount specialDiscount = new SpecialDiscount();
        System.out.println(weekendDiscount.isApplicable(date));
        System.out.println(specialDiscount.isApplicable(date));
    }
}
