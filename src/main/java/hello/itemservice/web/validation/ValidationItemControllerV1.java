package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class ValidationItemControllerV1 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {


        // 검증 오류 결과 보관

        Map<String, String> errors = new HashMap<>();


        //벨리데이션 로직

        // vali 1. 특정 값 검증
        if(!StringUtils.hasText(item.getItemName())) errors.put("itemName", "상품 이름은 필수 입니다.");
        if(item.getPrice()== null || item.getPrice() < 1000 || item.getPrice() > 1000000) errors.put("price", "가격은 1000 ~ 1000000 사이로만 됩니다.");
        if(item.getQuantity() == null || item.getQuantity() >=9999) errors.put("quantity", "수량은 999 까지만 됩니다.");


        //vali 2. 복합 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();

            if(resultPrice<10000){
                errors.put("globalError", "가격 * 수랴의 합은 10000원 이상이어야 합니다. 지금 값 = "+ resultPrice);
            }
        }


        //검증 실패하면 바로 return
        if(!errors.isEmpty()){
            log.error("errors: {}", errors);
            model.addAttribute("errors", errors);
            return "validation/v1/addForm";
        }


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v1/items/{itemId}";
    }

}

