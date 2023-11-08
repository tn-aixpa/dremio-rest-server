package it.digitalhub.dremiorestserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DremioService implements InitializingBean {
    Logger logger = LoggerFactory.getLogger(DremioService.class);

    @Value("${dremio.tables}")
    private List<String> tables;

    @Autowired
	JdbcTemplate jdbcTemplate;

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

    public DremioResult find(SelectQuery query) {
        if (!tables.contains(query.getTableName())) {
            throw new IllegalArgumentException(String.format("Table %s is not exposed", query.getTableName()));
        }

        long offset = query.getOffset();
        int limit = query.getLimit();

        String sqlTemplate = "SELECT %s FROM %s %s LIMIT %s OFFSET %s";

        String columns = String.join(",", query.getColumns());
        String orderBy = addOrderBy(query.getSort());

        String sql = String.format(sqlTemplate, columns, query.getTableName(), orderBy, limit, offset);
        List<Map<String,Object>> map = jdbcTemplate.queryForList(sql);

        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + query.getTableName(), Long.class);
        List<DremioRecord> result = new ArrayList<>();
        map.forEach(r -> result.add(new DremioRecord(r)));
        return new DremioResult(result, count);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (tables == null || tables.isEmpty()) {
            throw new IllegalArgumentException("Property dremio.tables is missing or empty");
        }
    }

    // //possibly add +, AND, OR, NOT
    // private static final Map<String, String> operators = Map.ofEntries(
    //     entry("eq", "="),
    //     entry("gt", ">"),
    //     entry("gte", ">="),
    //     entry("lt", "<"),
    //     entry("lte", "<="),
    //     entry("neq", "!="),
    //     entry("like", "LIKE"),
    //     entry("in", "IN"),
    //     entry("between", "BETWEEN")
    // );

    // private String addWhere(MultiValueMap<String, String> queryParams) {
    //     String clauses = "";
    //     for (String key : queryParams.keySet()) {
    //         String clause = queryParams.getFirst(key);
    //         if (clause == null) {
    //             continue;
    //         }
    //         String[] operatorAndValue = clause.split("\\.", 2);
    //         if (operatorAndValue.length < 2) {
    //             continue;
    //         }
    //         String operator = operatorAndValue[0];
    //         String value = operatorAndValue[1];
    //         if (operators.get(operator) != null) {
    //             clauses += key + " " + operators.get(operator) + " '" + value + "' AND ";
    //         }
    //     }
    //     if (!clauses.isEmpty()) {
    //         return " WHERE " + clauses.substring(0, clauses.length() - 5);
    //     }
    //     return "";
    // }
}
