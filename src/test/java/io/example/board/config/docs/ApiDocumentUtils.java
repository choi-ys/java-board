package io.example.board.config.docs;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

/**
 * @author : choi-ys
 * @date : 2021-09-28 오전 3:08
 */
public interface ApiDocumentUtils {

    final String HTTP = "http";
    final String HTTPS = "https";
    final String DEV_HOST = "dev-api.board.io";
    final String STG_HOST = "stg-api.board.io";
    final String PRD_HOST = "prd-api.board.io";

    static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                modifyUris()
                        .scheme(HTTP)
                        .host(DEV_HOST)
                        .removePort(),
                prettyPrint());
    }

    static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(
                modifyUris()
                        .scheme(HTTP)
                        .host(DEV_HOST)
                        .removePort(),
                prettyPrint());
    }
}