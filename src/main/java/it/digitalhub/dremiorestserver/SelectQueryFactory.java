package it.digitalhub.dremiorestserver;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.web.context.request.ServletWebRequest;

public class SelectQueryFactory {

    private String processAlias(String column) {
        String[] columnAndAlias = column.split(":");
        if (columnAndAlias.length < 2) {
            //no alias
            return column;
        }

        return columnAndAlias[1] + " " + columnAndAlias[0];
    }

    public SelectQuery create(String table, Pageable pageable, ServletWebRequest request) {
        SelectQuery query = new SelectQuery();
        long offset = pageable.getOffset();
        int limit = pageable.getPageSize();
        Map<String, String[]> queryParams = request.getParameterMap();
        Set<String> columns = new HashSet<>();
        String[] select = queryParams.get("select");

        if (select == null) {
            columns.add("*");
        } else {
            columns.addAll(Arrays.stream(select[0].split(",")).map(this::processAlias).collect(Collectors.toList()));
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
