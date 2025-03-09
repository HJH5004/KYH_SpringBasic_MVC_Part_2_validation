package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
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
        Assertions.assertThat(messageCode).containsExactly("required.item", "item");
    }

    @Test
    void messageCodesResolverField() {
        String[] messageCode = codesResolver.resolveMessageCodes("required", "item","itemName", String.class);
        for (String code : messageCode) {
            System.out.println(code);
        }



        Assertions.assertThat(messageCode).containsExactly("required.item.itemName", "itemName", "item");
    }
}
