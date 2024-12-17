package jp.gihyo.projava.householdbudget;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestController
public class HomeRestController {
    //収入のリスト
    record IncomeItem(String id, String income, String memo,
                      String tag, String date) {}
    private List<IncomeItem> incomeItems = new ArrayList<>();

    //収入追加
    @GetMapping("/restincomeadd")
    String incomeaddItem(@RequestParam("income") String income,
                         @RequestParam("memo") String memo,
                         @RequestParam("tag") String tag,
                         @RequestParam("date") String date) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        IncomeItem incomeitem = new IncomeItem(id, income, memo, tag, date);
        incomeItems.add(incomeitem);

        return "収入を追加しました。";
    }

    //収入一覧表示
    @GetMapping("/restincomelist")
    String incomelistItems() {
        String incomeresult = incomeItems.stream()
                .map(IncomeItem::toString)
                .collect(Collectors.joining(", "));
        return incomeresult;
    }


    //支出のリスト
    record ExpenditureItem(String id, String expenditure, String memo,
                      String tag, String date) {}
    private List<ExpenditureItem> expenditureItems = new ArrayList<>();

    //支出追加
    @GetMapping("/restexadd")
    String exaddItem(@RequestParam("expenditure") String expenditure,
                         @RequestParam("memo") String memo,
                         @RequestParam("tag") String tag,
                         @RequestParam("date") String date) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        ExpenditureItem exitem = new ExpenditureItem(id, expenditure, memo, tag, date);
        expenditureItems.add(exitem);

        return "支出を追加しました。";
    }

    //支出一覧表示
    @GetMapping("/restexlist")
    String exlistItems() {
        String exresult = expenditureItems.stream()
                .map(ExpenditureItem::toString)
                .collect(Collectors.joining(", "));
        return exresult;
    }


    @RequestMapping(value = "/resthello")
    String hello() {
        return """
                Hello.
                It works!
                現在時刻%sはです。
                """.formatted(LocalDateTime.now());
    }
}
