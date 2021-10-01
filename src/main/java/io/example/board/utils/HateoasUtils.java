package io.example.board.utils;

import io.example.board.utils.docs.DocsRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;

/**
 * @author : choi-ys
 * @date : 2021-09-29 오전 2:38
 */
public class HateoasUtils {
    private static String baseLinkBuilder(URI uri) {
        return uri.getScheme().concat("://").concat(uri.getHost()).concat(":" + uri.getPort());
    }

    private static String linkBuilder(URI uri) {
        try {
            return baseLinkBuilder(uri).concat(URLDecoder.decode(uri.getPath(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Link mapToDecodedLink(URI uri, DocsRelations docsRelations) {
        return new Link(linkBuilder(uri), docsRelations.relation);
    }

    public static Link profileLinkBuilder(URI uri, DocsRelations docsRelations) {
        return Link.of(baseLinkBuilder(uri).concat(docsRelations.path()), docsRelations.profileName());
    }

    public static Links mapToDecodedLinkWithProfileLink(URI uri, DocsRelations docsRelations) {
        return Links.of(mapToDecodedLink(uri, docsRelations), profileLinkBuilder(uri, docsRelations));
    }
}
