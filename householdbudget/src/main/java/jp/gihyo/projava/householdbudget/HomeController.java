package jp.gihyo.projava.householdbudget;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    //収入一覧表示のフィールド
    record IncomeItem(String id,String income,String date, String tag,String memo) {}
    private List<IncomeItem> incomeItems =new ArrayList<>();

    //収入一覧表示のエンドポイント
    @GetMapping("/list")
    String listItems(Model model) {
        model.addAttribute("incomeList", incomeItems);
        return "home";
    }

    //収入追加のエンドポイント
    @GetMapping("/add")
    String addItem(@RequestParam("income") String income,
                   @RequestParam("date") String date,
                   @RequestParam("tag") String tag,
                   @RequestParam("memo") String memo) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        IncomeItem item =new IncomeItem(id, income, date, tag, memo);
        incomeItems.add(item);

        return "redirect:/list";
    }



    //支出一覧表示のフィールド
    record ExpenditureItem(String id,String expenditure,String date, String tag,String memo) {}
    private List<ExpenditureItem> expenditureItems =new ArrayList<>();

    //支出一覧表示のエンドポイント
    @GetMapping("/exlist")
    String expenditurelistItems(Model model) {
        model.addAttribute("expenditureList", expenditureItems);
        return "home";
    }

    //支出追加のエンドポイント
    @GetMapping("/exadd")
    String expenditureaddItem(@RequestParam("expenditure") String expenditure,
                              @RequestParam("date") String date,
                              @RequestParam("memo") String memo,
                              @RequestParam("tag") String tag) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        ExpenditureItem expenditureitem =new ExpenditureItem(id, expenditure, date, memo, tag);
        expenditureItems.add(expenditureitem);

        return "redirect:/exlist";
    }

    @RequestMapping(value="/hello")
    String hello(Model model) {
        model.addAttribute("time", LocalDateTime.now());
        return "hello";
    }


}
