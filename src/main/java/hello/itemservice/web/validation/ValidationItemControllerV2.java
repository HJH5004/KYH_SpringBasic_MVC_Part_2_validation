package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {



        //벨리데이션 로직

        // vali 1. 특정 값 검증
        if(!StringUtils.hasText(item.getItemName())) bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
        if(item.getPrice()== null || item.getPrice() < 1000 || item.getPrice() > 1000000) bindingResult.addError(new FieldError("item","price","가격은 1000~ 100000 사이만 ㄱㄴ"));
        if(item.getQuantity() == null || item.getQuantity() >=9999) bindingResult.addError(new FieldError("item", "quantity", "수량은 9999까지만 ㄱㄴ"));


        //vali 2. 복합 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();

            if(resultPrice<10000){
                bindingResult.addError( new ObjectError("item", "가격 * 수랴의 합은 10000원 이상이어야 합니다. 지금 값 = "+ resultPrice));
            }
        }


        //검증 실패하면 바로 return
        if(bindingResult.hasErrors()){
            log.error("errors: {}", bindingResult);
            return "validation/v2/addForm";
        }


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //벨리데이션 로직

        // vali 1. 특정 값 검증
        if(!StringUtils.hasText(item.getItemName())) bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수 입니다."));
        if(item.getPrice()== null || item.getPrice() < 1000 || item.getPrice() > 1000000) bindingResult.addError(new FieldError("item","price", item.getPrice(), false, null, null, "가격은 1000~ 100000 사이만 ㄱㄴ"));
        if(item.getQuantity() == null || item.getQuantity() >=9999) bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 9999까지만 ㄱㄴ"));


        //vali 2. 복합 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();

            if(resultPrice<10000){
//                bindingResult.addError( new ObjectError("item", "가격 * 수랴의 합은 10000원 이상이어야 합니다. 지금 값 = "+ resultPrice));
                bindingResult.addError( new ObjectError("item", null, null, "가격 * 수랴의 합은 10000원 이상이어야 합니다. 지금 값 = "+ resultPrice));
            }
        }


        //검증 실패하면 바로 return
        if(bindingResult.hasErrors()){
            log.error("errors: {}", bindingResult);
            return "validation/v2/addForm";
        }


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());

        // vali 1. 특정 값 검증
        if(!StringUtils.hasText(item.getItemName())) bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName","error.default"}, null, null));
        if(item.getPrice()== null || item.getPrice() < 1000 || item.getPrice() > 1000000) bindingResult.addError(new FieldError("item","price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{100, 1000000},null));
        if(item.getQuantity() == null || item.getQuantity() >=9999) bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));


        //vali 2. 복합 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();

            if(resultPrice<10000){
//                bindingResult.addError( new ObjectError("item", "가격 * 수랴의 합은 10000원 이상이어야 합니다. 지금 값 = "+ resultPrice));
                bindingResult.addError( new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice},null));
            }
        }


        //검증 실패하면 바로 return
        if(bindingResult.hasErrors()){
            log.error("errors: {}", bindingResult);
            return "validation/v2/addForm";
        }


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());

        // vali 1. 특정 값 검증
        if(!StringUtils.hasText(item.getItemName())) bindingResult.rejectValue("itemName", "required");
        if(item.getPrice()== null || item.getPrice() < 1000 || item.getPrice() > 1000000) bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        if(item.getQuantity() == null || item.getQuantity() >=9999) bindingResult.rejectValue("quantity", "max",new Object[]{9999}, null);


        //vali 2. 복합 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();

            if(resultPrice<10000){
//                bindingResult.addError( new ObjectError("item", "가격 * 수랴의 합은 10000원 이상이어야 합니다. 지금 값 = "+ resultPrice));
//                bindingResult.addError( new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice},null));
                bindingResult.reject("totalPriceMin",new Object[]{10000, resultPrice}, null);
            }
        }


        //검증 실패하면 바로 return
        if(bindingResult.hasErrors()){
            log.error("errors: {}", bindingResult);
            return "validation/v2/addForm";
        }


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    //벨리데이션 로직을 validator로 추출
    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());

        //벨리데이터 다 추출됨
        itemValidator.validate(item, bindingResult);

        //검증 실패하면 바로 return
        if(bindingResult.hasErrors()){
            log.error("errors: {}", bindingResult);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

