# 디미터 법칙

## 개요

다들 프리코스를 진행하면서 "디미터 법칙" 이라는 것을 한번쯤은 들어보셨을 것이라 생각합니다.

누군가는 디미터 법칙을 "오직 하나의 도트('.')를 사용하라.", 또는 "낯선 자에게 말하지 말라" 와 같이 알고 있기도 합니다.
이번 시간에는 디미터 법칙이란 무엇인지, 왜 디미터 법칙을 준수하는 코드를 지향해야 하는지를 알아보겠습니다.

## 디미터 법칙이란?

디미터 법칙은 다음과 같이 정리할 수 있습니다.

>"클래스 C의 메서드 f는 아래 인스턴스의 메서드만 호출해야 한다"
> - 클래스 C
> - f가 생성한 객체
> - f 메서드에게 전달되는 모든 매개변수
> - C 인스턴스 변수에 저장된 객체

조금 더 간단히 정리하자면 "다른 객체가 어떠한 자료를 갖고 있는지 속사정을 몰라야 한다"고 기억해도 좋습니다.
결국 이를 위해선 우리가 지금까지 고민해 왔던 것 처럼 getter의 사용을 지양하고 메서드를 제공하는 방법으로 구현을 해야 합니다.

이번 시간엔 디미터 법칙을 공부하면서 getter를 지양해야 하는 또 다른 이유에 대해 알아봅시다.

## 코드를 통해 이해하기

우리가 가장 최근에 구현했던 프리코스 미션인 "크리스마스 프로모션"을 예시로 가져와 보겠습니다.
그 중에서도 우리는 혜택을 적용하는 부분을 구현해 보겠습니다.

우리의 관심사는 "고객의 방문 날짜를 이용해 할인이 적용 가능한지를 판별하는 것" 입니다.

### 사용 클래스

방문 날짜를 나타내기 위한 클래스는 다음과 같습니다.

```java
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
```

다음으로 할인을 적용하기 위한 책임은 인터페이스로 만들어 봅시다.
```java
public interface Discount {
    boolean isApplicable(VisitDate date);
}
```

날짜를 입력 받아 할인이 적용 가능한지 판별해 주는 `isApplicable`을 구현하는 것이 우리의 목적입니다.

간단하게 특별 할인과 주말 할인, 두 가지만 구현을 해 보겠습니다.

### Version 1

- 특별 할인
```java
public class SpecialDiscount implements Discount{
    List<Integer> specialDate = List.of(3, 10, 17, 24, 25, 31);
    @Override
    public boolean isApplicable(VisitDate date) {
        return specialDate.contains(date.getDate().getDayOfMonth());
    }
}
```

- 주말 할인
```java
public class WeekendDiscount implements Discount{
    @Override
    public boolean isApplicable(VisitDate date) {
        DayOfWeek week = date.getDate().getDayOfWeek();
        return week == DayOfWeek.FRIDAY || week == DayOfWeek.SATURDAY;
    }
}
```

위의 코드는 디미터 법칙을 준수하지 않는 코드입니다.
이유를 먼저 살펴볼까요?

특별 할인을 위한 클래스 `SpecialDiscount` 내부의 `isApplicable()` 메서드는 다음의 네 가지 인스턴스 객체의 메서드만 사용 가능합니다.
1. `SpecialDiscount`의 메서드
   - 지금은 `SpecialDiscount` 클래스가 구현하고 있는 다른 메서드가 존재하지 않습니다.
2. `isApplicable()`이 생성한 인스턴스
   - `isApplicable()` 메서드 내부에서 새롭게 생성된 인스턴스는 존재하지 않습니다.
3. `isApplicable()`에 전달되는 파라미터 인스턴스
   - `isApplicable`은 `VisitDate date`를 전달 받고 있기 때문에 해당 객체의 메서드를 사용할 수 있습니다.
4. `SpecialDiscount`의 필드 인스턴스
    - 지금은 `SpecialDiscount` 클래스가 `List<Integer> specialDate`를 필드 변수로 가지고 있기에 `specialDate`의 메서드를 사용할 수 있습니다.

그렇다면 `SpecialDiscount.isApplicable()`은 위의 네 가지 인스턴스의 메서드만 사용하고 있다고 볼 수 있을까요?
해당 메서드 내에서 사용된 `getDayOfMonth()`는 위의 네 가지 케이스 중 어느 곳에서 해당하지 않는 `DayOfWeek`의 메서드 입니다.
그렇기 때문에 위의 코드는 디미터 법칙을 준수하지 않고 있는 것입니다.

이번엔 이렇게 디미터 법칙을 준수하지 않은 코드가 어떤 문제를 가지는지 살펴봅시다.

1. 코드의 응집력이 낮아진다.
`WeekendDiscount`에서는 `getDayOfWeek()`를 사용하면서 방문 날짜의 요일이 주말에 해당하는지 확인하고 있습니다.
그런데, 특정 날짜의 요일을 확인하는 것은 날짜와 매우 연관성이 높은 책임인데요.
이 때문에 요일을 확인하는 주체는 `WeekendDiscount`가 아닌 `VisitDate`의 책임에 더 가깝다고 생각할 수 있습니다.
`WeekendDiscount`는 "어떤 날짜가 주말에 해당하는지"에 관심이 있는 것이 아니라, 단지 "방문 날짜가 주말인지"만을 알고싶을 뿐입니다.

2. 요구사항 변경에 따라 변경되는 클래스를 찾기 힘들다.
만약 특별 할인을 받을 수 있는 날짜가 변경된다고 가정합시다. 
우리는 어느 클래스를 변경해 요구사항 변화에 대처해야 할까요?
분명 프로모션에 사용되는 날짜의 정보를 가지고 있는 클래스는 `VisitDate`입니다. 하지만, 우리는 할인의 책임을 가지고 있는 `SpecialDiscount`를 수정해야 해요.

### Version 2

이번에는 위의 코드를 디미터 법칙을 지키도록 리팩토링을 해 봅시다.
각 메서드에서 위에서 언급한 네 개의 메서드를 제외하곤 모두 지워 주는 것입니다.

변경 후의 코드를 살펴보면 모두 디미터 법칙을 준수하고 있는 것을 알수 있습니다.
이 변경을 통해 새롭게 알게된 장점도 있는데요.

`WeekendDiscount`는 이제 어떤 요일이 주말인지 신경쓸 필요가 없어졌습니다. 때문에 날짜와 관련된 의존성이었던 `DayOfWeek`를 더이상 `import`할 필요가 없어졌습니다.

### Version 3

저는 할인 책임을 가지고 있는 `SpecialDiscount`가 날짜와 관련된 정보를 가지고 있는 것이 어색하다고 판단하여 한번 더 리팩토링을 진행했습니다. 
물론 이 부분은 "할인이 적용되는 날짜 역시 할인의 관심사에 포함된다." 라고 생각해도 문제는 없을 것 같습니다.

## 디미터 법칙을 준수하는 이유

이제 우리는 디미터 법칙을 준수하는 이유를 직접 눈으로 확인해 보았습니다.
디미터 법칙을 준수한 코드는 다음의 장점을 가지게 됩니다.

1. 각각의 객체에 대한 캡슐화가 보장된다.
디미터 법칙은 자신의 객체가 내부적으로 어떤 필드를 가지고 있는지 숨기기 때문에 객체들의 캡슐화를 보장해 주고, 이는 곧 객체들간의 결합도를 낮춰 주게 된다.

2. 책임을 명확하게 분리할 수 있다.
getter의 사용을 지양하는 이유와 마찬가지로, 각 객체들이 내부 구현을 숨기는 대신 기능 제공을 위한 메서드를 제공하기 때문에 각 클래스의 책임을 분리하는데 큰 도움이 된다.
그리고 이는 코드의 응집력을 높여준다.

## 마치며

이 디미터 법칙 역시 모든 순간에 "무조건" 적용되는 법칙은 아님에 주의해야 합니다.
만약 우리가 사용하는 객체가 자료구조라면, 디미터 법칙을 적용하는 것이 매우 어려운데요.
이는 자료구조 자체가 "책임을 가지기 위한 객체" 보다는 "데이터를 저장하기 위한 객체"의 성격이 강하기 때문입니다.

즉, 디미터 법칙 역시 사용하는 이유와 적용시 누릴 수 있는 장점을 확실하게 이해한 후 상황에 맞게 적용하는 것이 중요합니다.

또, 무조건 여러 개의 `.`을 사용하는 것 자체가 디미터 법칙을 위반하는 것이 아님을 기억하면 좋습니다.
우리의 목적은 "객체의 캡슐화를 보장"하는 것에 있을 뿐입니다.
그 예시로, `Stream` 체인은 여러 개의 `.`을 사용하더라도 각각이 `Stream`을 반환하기에 캡슐화를 보장받습니다.