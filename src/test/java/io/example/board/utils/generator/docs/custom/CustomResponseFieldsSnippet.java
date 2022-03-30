package io.example.board.utils.generator.docs.custom;

import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.payload.AbstractFieldsSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author : choi-ys
 * @date : 2022/03/29 11:38 오전
 */
public class CustomResponseFieldsSnippet extends AbstractFieldsSnippet {

    protected CustomResponseFieldsSnippet(
            String type,
            List<FieldDescriptor> descriptors,
            Map<String, Object> attributes,
            boolean ignoreUndocumentedFields,
            PayloadSubsectionExtractor<?> subsectionExtractor
    ) {
        super(type, descriptors, attributes, ignoreUndocumentedFields, subsectionExtractor);
    }

    public static CustomResponseFieldsSnippet customResponseFields(
            String type,
            PayloadSubsectionExtractor<?> subsectionExtractor,
            Map<String, Object> attributes,
            FieldDescriptor... descriptors
    ) {
        return new CustomResponseFieldsSnippet(
                type,
                Arrays.asList(descriptors),
                attributes,
                true,
                subsectionExtractor
        );
    }

    @Override
    protected MediaType getContentType(Operation operation) {
        return operation.getResponse().getHeaders().getContentType();
    }

    @Override
    protected byte[] getContent(Operation operation) throws IOException {
        return operation.getResponse().getContent();
    }
}
