package it.digitalhub.dremiorestserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class DremioService {
    Logger logger = LoggerFactory.getLogger(DremioService.class);

    @Autowired
	JdbcTemplate jdbcTemplate;

    //TODO add +, AND, OR, NOT
    private static final Map<String, String> operators = Map.ofEntries(
        entry("eq", "="),
        entry("gt", ">"),
        entry("gte", ">="),
        entry("lt", "<"),
        entry("lte", "<="),
        entry("neq", "!="),
        entry("like", "LIKE"),
        entry("in", "IN"),
        entry("between", "BETWEEN")
    );

    private String addWhere(MultiValueMap<String, String> queryParams) {
        String clauses = "";
        for (String key : queryParams.keySet()) {
            String clause = queryParams.getFirst(key);
            if (clause == null) {
                continue;
            }
            String[] operatorAndValue = clause.split("\\.", 2);
            if (operatorAndValue.length < 2) {
                continue;
            }
            String operator = operatorAndValue[0];
            String value = operatorAndValue[1];
            if (operators.get(operator) != null) {
                clauses += key + " " + operators.get(operator) + " '" + value + "' AND ";
            }
        }
        if (!clauses.isEmpty()) {
            return " WHERE " + clauses.substring(0, clauses.length() - 5);
        }
        return "";
    }

    private String addOrderBy(Sort sort) {
        if(!sort.isSorted()) {
            return "";
        }

        String orderBy = " ORDER BY ";

        for (Sort.Order order : sort) {
            orderBy += order.getProperty() + " " + order.getDirection() + ",";
        }

        return orderBy.substring(0, orderBy.length() - 1);
    }

    public DremioResult find(SelectQuery query, MultiValueMap<String, String> queryParams) {
        long offset = query.getOffset();
        int limit = query.getLimit();

        String sql = "SELECT ";

        sql += String.join(",", query.getColumns());

        sql += " FROM " + query.getTableName();

        String where = addWhere(queryParams);
        sql += where;

        sql += addOrderBy(query.getSort());

        sql += " LIMIT " + limit + " OFFSET " + offset;

        List<Map<String,Object>> map = jdbcTemplate.queryForList(sql);
        long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + query.getTableName() + where, Long.class);
        List<DremioRecord> result = new ArrayList<>();
        map.forEach(r -> result.add(new DremioRecord(r)));
        return new DremioResult(result, count);
    }
}
