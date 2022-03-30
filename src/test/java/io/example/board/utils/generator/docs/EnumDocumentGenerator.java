package io.example.board.utils.generator.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.board.utils.generator.docs.custom.EnumDocument;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.Map;

import static io.example.board.config.docs.ApiDocumentUtils.createDocument;
import static io.example.board.utils.generator.docs.custom.CustomResponseFieldsSnippet.customResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;

/**
 * @author : choi-ys
 * @date : 2022/03/29 1:02 오후
 */
public class EnumDocumentGenerator {

    private static final String ENUM_ADOC_TEMPLATE_PREFIX = "enum-response";
    private static final String ENUM_ADOC_TABLE_KEY = "title";

    public static RestDocumentationResultHandler generateMemberStatusDocument(MvcResult mvcResult) throws IOException {
        EnumDocument enumDocument = parseEnumDocument(mvcResult);
        return createDocument(
                customResponseFields(
                        ENUM_ADOC_TEMPLATE_PREFIX,
                        beneathPath("memberStatus").withSubsectionId("memberStatus"),
                        attributes(key(ENUM_ADOC_TABLE_KEY).value("memberStatus")),
                        enumConvertFieldDescriptor(enumDocument.getMemberStatus())
                )
        );
    }

    private static FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {
        return enumValues.entrySet().stream()
                .map(valueSet -> fieldWithPath(valueSet.getKey()).description(valueSet.getValue()))
                .toArray(FieldDescriptor[]::new);
    }

    public static EnumDocument parseEnumDocument(MvcResult mvcResult) throws IOException {
        EnumDocument enumDocuments = new ObjectMapper()
                .readValue(
                        mvcResult.getResponse().getContentAsByteArray(),
                        EnumDocument.class
                );
        return enumDocuments;
    }
}
