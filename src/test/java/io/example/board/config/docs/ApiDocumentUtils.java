package io.example.board.config.docs;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.snippet.Snippet;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

/**
 * @author : choi-ys
 * @date : 2021-09-28 오전 3:08
 */
public interface ApiDocumentUtils {

    String BASE_HOST = "board.io";
    String DOCUMENT_IDENTIFIER = "{class-name}/{method-name}";

    static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                modifyUris()
                        .scheme(scheme())
                        .host(host())
                        .removePort(),
                prettyPrint());
    }

    static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(
                modifyUris()
                        .scheme(scheme())
                        .host(host())
                        .removePort(),
                prettyPrint());
    }

    static RestDocumentationResultHandler createDocument(Snippet... snippets) {
        return document(DOCUMENT_IDENTIFIER,
                getDocumentRequest(),
                getDocumentResponse(),
                snippets
        );
    }

    // TODO (2022-03-26) : 실행 환경의 Profile 정보로부터 TC 기반 API Docs 생성 시, Link 정보를 생성에 필요한 Scheme 정보를 반환
    private static String scheme() {
        return "http";
    }

    // TODO (2022-03-26) : 실행 환경의 Profile 정보로부터 TC 기반 API Docs 생성 시, Link 정보 생성에 필요한 Host 정보를 반환
    private static String host() {
        return String.format("dev-api.%s", BASE_HOST);
    }
}