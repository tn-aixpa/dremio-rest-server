package it.digitalhub.dremiorestserver;

//import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

@RestController
@Validated
public class DremioController {

    @Autowired
    private DremioService service;

    @Autowired
    private SelectQueryFactory factory;

    @GetMapping("/{table}")
	public DremioResult list(
            //@PathVariable @Pattern(regexp = "[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*") String table,
            @PathVariable String table,
            Pageable pageable,
            ServletWebRequest request
        ) {
        //MultiValueMap<String, String> queryParams = request.getQueryParams();
        SelectQuery query = factory.create(table, pageable, request);
        return service.find(query);
	}
}
