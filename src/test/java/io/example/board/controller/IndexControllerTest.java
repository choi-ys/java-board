package io.example.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.board.config.test.SpringBootTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static io.example.board.utils.generator.docs.IndexDocumentGenerator.generateIndexDocument;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author : choi-ys
 * @date : 2021-09-28 오전 3:17
 */
@SpringBootTestConfig
@AutoConfigureRestDocs
@DisplayName("API:Index")
class IndexControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private final String INDEX_URL = "/index";

    public IndexControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @DisplayName("[200:GET]API 목차 조회")
    public void index() throws Exception {
        // When
        ResultActions resultActions = this.mockMvc.perform(get(INDEX_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE))
                .andExpect(jsonPath("_links.index").exists())
                .andDo(generateIndexDocument())
        ;
    }
}