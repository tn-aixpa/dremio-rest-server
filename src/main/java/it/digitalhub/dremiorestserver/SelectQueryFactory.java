package it.digitalhub.dremiorestserver;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

//TODO implementare come estensione di FactoryBean?

public class SelectQueryFactory {

    private String processAlias(String column) {
        String[] columnAndAlias = column.split(":");
        if (columnAndAlias.length < 2) {
            //no alias
            return column;
        }

        return columnAndAlias[1] + " " + columnAndAlias[0];
    }

    public SelectQuery create(String table, Pageable pageable, ServerHttpRequest request) {
        SelectQuery query = new SelectQuery();
        long offset = pageable.getOffset();
        int limit = pageable.getPageSize();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        Set<String> columns = new HashSet<>();
        String select = queryParams.getFirst("select");

        if (select == null) {
            columns.add("*");
        } else {
            columns.addAll(Arrays.stream(select.split(",")).map(this::processAlias).collect(Collectors.toList()));
        }

        query.setTableName(table);
        query.setOffset(offset);
        query.setLimit(limit);
        query.setSort(pageable.getSort());
        query.setColumns(columns);

        SelectQueryValidator.validate(query);

        return query;
    }
}
