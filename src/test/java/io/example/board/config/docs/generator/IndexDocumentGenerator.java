package io.example.board.config.docs.generator;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static io.example.board.config.docs.ApiDocumentUtils.getDocumentRequest;
import static io.example.board.config.docs.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.hateoas.IanaLinkRelations.INDEX;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

/**
 * @author : choi-ys
 * @date : 2021-09-28 오전 3:13
 */
public class IndexDocumentGenerator {

    public static RestDocumentationResultHandler generateIndexDocument() {
        return document("{class-name}/{method-name}",
                getDocumentRequest(),
                getDocumentResponse(),
                links(
                        linkWithRel(INDEX.value()).description("link to api index")
                )
        );
    }
}
