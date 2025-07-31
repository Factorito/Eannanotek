package prototype.example.demo.security;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckQuoteOwner {
    // 필요하다면 추가 속성을 정의할 수 있습니다.
}