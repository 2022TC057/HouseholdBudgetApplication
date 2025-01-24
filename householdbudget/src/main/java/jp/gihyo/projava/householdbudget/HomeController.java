package jp.gihyo.projava.householdbudget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class HomeController {

    private final HouseListDao dao;
    @Autowired
    HomeController(HouseListDao dao) {
        this.dao = dao;
    }
    @Autowired
    HouseGraph houseGraph;

    //収入一覧表示のフィールド
    record IncomeItem(String id,String income,String date, String tag,String memo) {}
    private List<IncomeItem> incomeItems =new ArrayList<>();

    //収支一覧表示のエンドポイント
    @GetMapping("/list")
    String listItems(Model model) {
        List<IncomeItem> incomeItems = dao.findAll();
        List<ExpenditureItem> expenditureItems = dao.selectAll();
        model.addAttribute("incomeList", incomeItems);
        model.addAttribute("expenditureList", expenditureItems);
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
        dao.add(item);

        return "redirect:/list";
    }


    //収入削除のエンドポイント
    @GetMapping("/delete")
    String deleteItem(@RequestParam("id") String id) {
        dao.delete(id);
        return "redirect:/list";
    }

    //収入変更のエンドポイント
    @GetMapping("/update")
    String updateItem(@RequestParam("id") String id,
                      @RequestParam("income") String income,
                      @RequestParam("date") String date,
                      @RequestParam("tag") String tag,
                      @RequestParam("memo") String memo) {
        IncomeItem incomeItem = new IncomeItem(id, income, date, tag, memo);
        dao.update(incomeItem);
        return "redirect:/list";
    }




    //支出一覧表示のフィールド
    record ExpenditureItem(String id, String expenditure, String date, String tag, String memo) {}
    private List<ExpenditureItem> expenditureItems =new ArrayList<>();

    //支出追加のエンドポイント
    @GetMapping("/exadd")
    String expenditureaddItem(@RequestParam("expenditure") String expenditure,
                              @RequestParam("date") String date,
                              @RequestParam("memo") String memo,
                              @RequestParam("tag") String tag) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        ExpenditureItem expenditureitem =new ExpenditureItem(id, expenditure, date, memo, tag);
        dao.add(expenditureitem);

        return "redirect:/list";
    }

    //支出削除のエンドポイント
    @GetMapping("/exdelete")
    String expendituredeleteItem(@RequestParam("id") String id) {
        dao.exdelete(id);
        return "redirect:/list";
    }

    //支出変更のエンドポイント
    @GetMapping("/exupdate")
    String expenditureupdateItem(@RequestParam("id") String id,
                      @RequestParam("expenditure") String expenditure,
                      @RequestParam("date") String date,
                      @RequestParam("tag") String tag,
                      @RequestParam("memo") String memo) {
        ExpenditureItem expenditureItem = new ExpenditureItem(id, expenditure, date, tag, memo);
        dao.exupdate(expenditureItem);
        return "redirect:/list";
    }

    //グラフ
    @GetMapping("/monthlySummary")
    public String getMonthlySummary(@RequestParam("month") int month, @RequestParam("year") int year, Model model) {
            Map<String, Double> incomeSummary = houseGraph.getMonthlyIncomeSummary(month, year);
            Map<String, Double> expenditureSummary = houseGraph.getMonthlyExpenditureSummary(month, year);
            model.addAttribute("incomeSummary", incomeSummary);
            model.addAttribute("expenditureSummary", expenditureSummary);
            return "monthlySummary";
    }


    @RequestMapping(value="/hello")
    String hello(Model model) {
        model.addAttribute("time", LocalDateTime.now());
        return "hello";
    }


}
