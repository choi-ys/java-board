package io.example.board.controller;

import io.example.board.repository.rdb.common.PageResponse;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @author : choi-ys
 * @date : 2022/03/28 6:25 오후
 */
@RestController
@RequestMapping(
        value = "index",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaTypes.HAL_JSON_VALUE
)
public class CommonDocsSnippetController {

    @GetMapping("pagination")
    public ResponseEntity commonPaginationResponse() {
        return ResponseEntity.ok(PageResponse.mapTo(
                new PageImpl(
                        new ArrayList(),
                        PageRequest.of(1, 5),
                        0
                )
        ));
    }
}
