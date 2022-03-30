package io.example.board.controller;

import io.example.board.config.test.SpringBootTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static io.example.board.utils.generator.docs.common.PaginationDocumentGenerator.generateCommonPaginationDocument;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : choi-ys
 * @date : 2022/03/28 6:29 오후
 */
@SpringBootTestConfig
@AutoConfigureRestDocs
@DisplayName("API:Index")
class CommonDocsSnippetControllerTest {

    private final MockMvc mockMvc;

    private static final String DOCS_RUL = "/index/pagination";

    public CommonDocsSnippetControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("공통 페이징 응답 구조")
    public void commonPaginationResponse() throws Exception {
        // When
        ResultActions resultActions = this.mockMvc.perform(get(DOCS_RUL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andDo(generateCommonPaginationDocument())
        ;
    }
}