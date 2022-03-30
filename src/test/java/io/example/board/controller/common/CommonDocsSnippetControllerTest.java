package io.example.board.controller.common;

import io.example.board.config.test.SpringBootTestConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static io.example.board.utils.generator.docs.EnumDocumentGenerator.generateMemberStatusDocument;
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

    private static final String DOCS_RUL = "/index";

    public CommonDocsSnippetControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("공통 페이징 응답 구조")
    public void commonPaginationResponse() throws Exception {
        // Given
        final String urlTemplate = DOCS_RUL.concat("/pagination");

        // When
        ResultActions resultActions = this.mockMvc.perform(get(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andDo(generateCommonPaginationDocument())
        ;
    }

    @Test
    @DisplayName("Enum 상태 코드")
    public void enumDocs() throws Exception {
        // Given
        final String urlTemplate = DOCS_RUL.concat("/enum");

        // When
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andDo(generateMemberStatusDocument(resultActions.andReturn()))
        ;
    }
}