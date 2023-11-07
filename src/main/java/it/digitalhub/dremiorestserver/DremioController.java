package it.digitalhub.dremiorestserver;

import jakarta.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Pageable;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class DremioController {

    @Autowired
    private DremioService service;

    @Autowired
    private SelectQueryFactory factory;

    @GetMapping("/{table}")
	public DremioResult list(
            @PathVariable @Pattern(regexp = "[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*") String table,
            Pageable pageable,
            ServerHttpRequest request
        ) {
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        SelectQuery query = factory.create(table, pageable, request);
        return service.find(query, queryParams);
	}
}

/*
 * TODO
 * - usare metodi di JdbcTemplate con parametri (es. sostituire queryForList con quello che prende arguments) per prevenire SQL injection (vedere https://github.com/scc-digitalhub/AAC/blob/f00f80d68dc066b93347f06e482a18e6b1d455c0/src/main/java/it/smartcommunitylab/aac/audit/store/AutoJdbcAuditEventStore.java#L36)
 * - autenticazione: imitare approccio di PostgREST
 * - nel servizio, non usare concatenazione ma un template
 */
