package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Item.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Item item = (Item) o;
        
        // vali 1. 특정 값 검증
        if(!StringUtils.hasText(item.getItemName())) errors.rejectValue("itemName", "required");
        if(item.getPrice()== null || item.getPrice() < 1000 || item.getPrice() > 1000000) errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        if(item.getQuantity() == null || item.getQuantity() >=9999) errors.rejectValue("quantity", "max",new Object[]{9999}, null);


        //vali 2. 복합 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();

            if(resultPrice<10000){
//                errors.addError( new ObjectError("item", "가격 * 수랴의 합은 10000원 이상이어야 합니다. 지금 값 = "+ resultPrice));
//                errors.addError( new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice},null));
                errors.reject("totalPriceMin",new Object[]{10000, resultPrice}, null);
            }
        }

    }
}
