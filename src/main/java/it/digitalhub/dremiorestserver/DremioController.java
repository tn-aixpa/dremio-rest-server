package it.digitalhub.dremiorestserver;

//import jakarta.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.MultiValueMap;
//import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Validated
public class DremioController {

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

    @Autowired
	JdbcTemplate jdbcTemplate;

    @GetMapping("/{table}")
	public Page<DremioRecord> list(
            @PathVariable /*@Pattern(regexp = "\".*\"|[a-zA-Z0-9]+")*/ String table,
            Pageable pageable,
            ServerHttpRequest request
        ) {
        MultiValueMap<String, String> queryParams = request.getQueryParams();

        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();

        String query = "SELECT ";

        query += queryParams.get("select") == null ? "*" : String.join(",", queryParams.get("select"));

        query += " FROM " + table;

        String where = addWhere(queryParams);
        query += where;

        query += addOrderBy(pageable.getSort());

        query += " LIMIT " + limit + " OFFSET " + offset;

        List<Map<String,Object>> map = jdbcTemplate.queryForList(query);
        long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + table + where, Long.class);
        List<DremioRecord> result = new ArrayList<>();
        map.forEach(r -> result.add(new DremioRecord(r)));
        return new PageImpl<>(result, pageable, count);
	}

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
}

/*
 * TODO
 * - aggiungere regex al path param? usare @Param da spring-boot-starter-data-jpa o PathParam (es. "/{name:regex}")?
 * - usare metodi di JdbcTemplate con parametri (es. sostituire queryForList con quello che prende arguments) per prevenire SQL injection (vedere https://github.com/scc-digitalhub/AAC/blob/f00f80d68dc066b93347f06e482a18e6b1d455c0/src/main/java/it/smartcommunitylab/aac/audit/store/AutoJdbcAuditEventStore.java#L36)
 * - creare endpoint per GET (capire come gestire get, es. recuperando la primary key da uno schema)
 * - autenticazione: imitare approccio di PostgREST
 * - domanda: tenere paginazione e sorting con approccio Spring o con approccio PostgREST (https://postgrest.org/en/stable/references/api/tables_views.html#limits-and-pagination) ?
 */
