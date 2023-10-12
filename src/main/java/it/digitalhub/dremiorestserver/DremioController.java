package it.digitalhub.dremiorestserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DremioController {

    @Autowired
	JdbcTemplate jdbcTemplate;

    @GetMapping("/{table}")
	public Page<DremioRecord> list(@PathVariable String table, Pageable pageable) {
        System.out.println(pageable.getSort());
        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();

        String query = "SELECT * FROM %s";

        if(pageable.getSort().isSorted()) {
            query += addOrderBy(pageable.getSort());
        }

        query += " LIMIT %s OFFSET %s";

        List<Map<String,Object>> map = jdbcTemplate.queryForList(String.format(query, table, limit, offset));
        long count = jdbcTemplate.queryForObject(String.format("SELECT COUNT(*) FROM %s", table), Long.class);
        List<DremioRecord> result = new ArrayList<>();
        map.forEach(r -> result.add(new DremioRecord(r)));
        System.out.println("Result: " + result);
        return new PageImpl<>(result, pageable, count);
	}

    private String addOrderBy(Sort sort) {
        String orderBy = " ORDER BY ";

        for (Sort.Order order : sort) {
            orderBy += order.getProperty() + " " + order.getDirection() + ",";
        }

        return orderBy.substring(0, orderBy.length() - 1);
    }
}
/*
 * TODO
 * - usare metodi di JdbcTemplate con parametri (es. sostituire queryForList con quello che prende arguments) per prevenire SQL injection (vedere https://github.com/scc-digitalhub/AAC/blob/f00f80d68dc066b93347f06e482a18e6b1d455c0/src/main/java/it/smartcommunitylab/aac/audit/store/AutoJdbcAuditEventStore.java#L36)
 * - creare gli endpoint per LIST e GET (capire come gestire get, es. recuperando la primary key da uno schema)
 * - elaborare filtri di ricerca ispirandosi a PostgREST
 * - autenticazione: imitare approccio di PostgREST
 */
