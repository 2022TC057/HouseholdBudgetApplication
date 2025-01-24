package jp.gihyo.projava.householdbudget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HouseGraph {
    @Autowired
    HouseListDao dao;

        public Map<String, Double> getMonthlyIncomeSummary(int month, int year) {
            List<Map<String, Object>> results = dao.findMonthlyIncomeSummary(month, year);
            Map<String, Double> summary = new HashMap<>();
            for (Map<String, Object> result : results) {
                summary.put((String) result.get("tag"), ((Number) result.get("total")).doubleValue());
            }
            return summary;
        }

        public Map<String, Double> getMonthlyExpenditureSummary(int month, int year) {
            List<Map<String, Object>> results = dao.findMonthlyExpenditureSummary(month, year);
            Map<String, Double> summary = new HashMap<>();
            for (Map<String, Object> result : results) {
                summary.put((String) result.get("tag"), ((Number) result.get("total")).doubleValue());
            }
            return summary;
        }
}
