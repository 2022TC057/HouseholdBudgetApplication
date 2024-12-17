package jp.gihyo.projava.householdbudget;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    record IncomeItem(String id,String income,String date, String tag,String memo) {}
    private List<IncomeItem> incomeItems =new ArrayList<>();

    @GetMapping(value="/incomelist")
    String listItems(Model model) {
        model.addAttribute("incomeList", incomeItems);
        return "home";
    }

    @RequestMapping(value="/hello")
    String hello(Model model) {
        model.addAttribute("time", LocalDateTime.now());
        return "hello";
    }
}
