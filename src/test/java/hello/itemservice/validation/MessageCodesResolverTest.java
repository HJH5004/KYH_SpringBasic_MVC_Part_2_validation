package hello.itemservice.validation;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();


    @Test
    void messageCodesResolverObject() {
        String[] messageCode = codesResolver.resolveMessageCodes("required", "item");
        for (String code : messageCode) {
            System.out.println(code);
        }

    }
}
