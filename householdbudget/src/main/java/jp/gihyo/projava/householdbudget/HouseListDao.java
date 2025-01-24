package jp.gihyo.projava.householdbudget;

import jp.gihyo.projava.householdbudget.HomeController.IncomeItem;
import jp.gihyo.projava.householdbudget.HomeController.ExpenditureItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HouseListDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    HouseListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //収入追加
    public void add(IncomeItem incomeItem) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(incomeItem);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("incomelist");
        insert.execute(param);
    }

    //収入削除
    public int delete(String id) {
        int number = jdbcTemplate.update("DELETE FROM incomelist WHERE id = ?", id);
        return number;
    }


    //収入変更
    public int update(IncomeItem incomeItem) {
        int number = jdbcTemplate.update(
                "UPDATE incomelist SET income = ?, date = ?, tag = ?, memo = ? WHERE id = ?",
                incomeItem.income(),
                incomeItem.date(),
                incomeItem.tag(),
                incomeItem.memo(),
                incomeItem.id());
        return number;
    }

    public List<IncomeItem> findAll() {
        String query = "SELECT * FROM incomelist";
        List<Map<String,Object>> result = jdbcTemplate.queryForList(query);
        List<IncomeItem> incomeItems = result.stream()
                .map((Map<String, Object> row) -> new IncomeItem(
                        row.get("id").toString(),
                        row.get("income").toString(),
                        row.get("date").toString(),
                        row.get("tag").toString(),
                        row.get("memo").toString()))
                .toList();
        return incomeItems;
    }


    //支出追加
    public void add(ExpenditureItem expenditureItem) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(expenditureItem);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("expenditurelist");
        insert.execute(param);
    }

    //支出削除
    public int exdelete(String id) {
            int number = jdbcTemplate.update("DELETE FROM expenditurelist WHERE id = ?", id);
            return number;
    }

    //支出更新
    public int exupdate(ExpenditureItem expenditureItem) {
            int exnumber = jdbcTemplate.update(
                    "UPDATE expenditurelist SET expenditure = ?, date = ?, tag = ?, memo = ? WHERE id = ?",
                    expenditureItem.expenditure(),
                    expenditureItem.date(),
                    expenditureItem.tag(),
                    expenditureItem.memo(),
                    expenditureItem.id());
            return exnumber;
        }

    public List<ExpenditureItem> selectAll() {
        String query = "SELECT * FROM expenditurelist";

        List<Map<String,Object>> result = jdbcTemplate.queryForList(query);
        List<ExpenditureItem> expenditureItems = result.stream()
                .map((Map<String, Object> row) -> new ExpenditureItem(
                        row.get("id").toString(),
                        row.get("expenditure").toString(),
                        row.get("date").toString(),
                        row.get("tag").toString(),
                        row.get("memo").toString()))
                .toList();
        return expenditureItems;
    }

    public List<Map<String, Object>> findMonthlyIncomeSummary(int month, int year) {
        String query = "SELECT tag, SUM(CAST(REPLACE(income, ',', '') AS DECIMAL)) as total FROM incomelist " +
                "WHERE MONTH(date) = ? AND YEAR(date) = ? " + "GROUP BY tag";
        return jdbcTemplate.queryForList(query, month, year);
    }

    public List<Map<String, Object>> findMonthlyExpenditureSummary(int month, int year) {
        String query = "SELECT tag, SUM(CAST(REPLACE(expenditure, ',', '') AS DECIMAL)) as total FROM expenditurelist " +
                "WHERE MONTH(date) = ? AND YEAR(date) = ? " + "GROUP BY tag";
        return jdbcTemplate.queryForList(query, month, year);
    }
}
