package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;


    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }


    //Validatior 삽입을 통한 벨리데이션 어노테이션 삽입
//    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // Global 오류의 경우 이렇게 따로 처리한다. Field 값의 공란의 경우 스프링을 사용하지만 이건 그냥 빼서 하는게 편하다
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();

            if(resultPrice<10000){
//                errors.addError( new ObjectError("item", "가격 * 수랴의 합은 10000원 이상이어야 합니다. 지금 값 = "+ resultPrice));
//                errors.addError( new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice},null));
                bindingResult.reject("totalPriceMin",new Object[]{10000, resultPrice}, null);
            }
        }

        //검증 실패하면 바로 return
        if(bindingResult.hasErrors()){
            log.error("errors: {}", bindingResult);
            return "validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }


    //Validatior 삽입을 통한 벨리데이션 어노테이션 삽입
    @PostMapping("/add")
    public String addItemV2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // Global 오류의 경우 이렇게 따로 처리한다. Field 값의 공란의 경우 스프링을 사용하지만 이건 그냥 빼서 하는게 편하다
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();

            if(resultPrice<10000){
//                errors.addError( new ObjectError("item", "가격 * 수랴의 합은 10000원 이상이어야 합니다. 지금 값 = "+ resultPrice));
//                errors.addError( new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice},null));
                bindingResult.reject("totalPriceMin",new Object[]{10000, resultPrice}, null);
            }
        }

        //검증 실패하면 바로 return
        if(bindingResult.hasErrors()){
            log.error("errors: {}", bindingResult);
            return "validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/editForm";
    }

//    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {



        // Global 오류의 경우 이렇게 따로 처리한다. Field 값의 공란의 경우 스프링을 사용하지만 이건 그냥 빼서 하는게 편하다
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();

            if(resultPrice<10000){
//                errors.addError( new ObjectError("item", "가격 * 수랴의 합은 10000원 이상이어야 합니다. 지금 값 = "+ resultPrice));
//                errors.addError( new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice},null));
                bindingResult.reject("totalPriceMin",new Object[]{10000, resultPrice}, null);
            }
        }

        //검증 실패하면 바로 return
        if(bindingResult.hasErrors()){
            log.error("errors: {}", bindingResult);
            return "validation/v3/editForm";
        }



        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {



        // Global 오류의 경우 이렇게 따로 처리한다. Field 값의 공란의 경우 스프링을 사용하지만 이건 그냥 빼서 하는게 편하다
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();

            if(resultPrice<10000){
//                errors.addError( new ObjectError("item", "가격 * 수랴의 합은 10000원 이상이어야 합니다. 지금 값 = "+ resultPrice));
//                errors.addError( new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice},null));
                bindingResult.reject("totalPriceMin",new Object[]{10000, resultPrice}, null);
            }
        }

        //검증 실패하면 바로 return
        if(bindingResult.hasErrors()){
            log.error("errors: {}", bindingResult);
            return "validation/v3/editForm";
        }



        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

}

