package io.example.board.controller;

import io.example.board.utils.docs.DocsRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static io.example.board.utils.HateoasUtils.mapToDecodedLinkWithProfileLink;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author : choi-ys
 * @date : 2021-09-28 오전 3:15
 */
@RestController
@RequestMapping(
        value = "index",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaTypes.HAL_JSON_VALUE
)
public class IndexController {

    @GetMapping
    public RepresentationModel index() {
        var indexRepresentationModel = new RepresentationModel();
        indexRepresentationModel.add(indexLinks());
        indexRepresentationModel.add(memberLinks());
        indexRepresentationModel.add(LoginLinks());
        indexRepresentationModel.add(PostLinks());
        return indexRepresentationModel;
    }

    public static Link profileUrl() {
        return linkTo(methodOn(IndexController.class).index()).withRel("profile");
    }

    public Links indexLinks() {
        return Links.of(mapToDecodedLinkWithProfileLink(linkTo(this.getClass()).toUri(), DocsRelations.INDEX));
    }

    // TODO: 용석(2021-09-29) : 현재 API 요청자의 권한 별로 접근 가능한 Link 정보를 가공, EX) memberLinks(LoginUser loginUser)
    public Links memberLinks() {
        WebMvcLinkBuilder memberLinkBuilder = linkTo(MemberController.class);
        return Links.of(mapToDecodedLinkWithProfileLink(memberLinkBuilder.toUri(), DocsRelations.CREATE_MEMBER));
    }

    // TODO: 용석(2021-09-29) : 현재 API 요청자의 권한 별로 접근 가능한 Link 정보를 가공, EX) LoginLinks(LoginUser loginUser)
    public Links LoginLinks() {
        WebMvcLinkBuilder loginLinkBuilder = linkTo(LoginController.class);
        Links loginLinks = mapToDecodedLinkWithProfileLink(loginLinkBuilder.slash(DocsRelations.LOGIN.relation).toUri(), DocsRelations.LOGIN);
        Links refreshLinks = mapToDecodedLinkWithProfileLink(loginLinkBuilder.slash(DocsRelations.REFRESH.relation).toUri(), DocsRelations.REFRESH);
        return Links.of(loginLinks).and(refreshLinks);
    }

    // TODO: 용석(2021-09-29) : 현재 API 요청자의 권한 별로 접근 가능한 Link 정보를 가공, EX) PostLinks(LoginUser loginUser)
    public Links PostLinks() {
        WebMvcLinkBuilder postLinkBuilder = linkTo(PostController.class);
        URI postIdPathUri = postLinkBuilder.slash("{id}").toUri();
        Links createPostLinks = mapToDecodedLinkWithProfileLink(postLinkBuilder.toUri(), DocsRelations.CREATE_POST);
        Links getAnPostLinks = mapToDecodedLinkWithProfileLink(postIdPathUri, DocsRelations.GET_AN_POST);
        Links updateAnPostLinks = mapToDecodedLinkWithProfileLink(postIdPathUri, DocsRelations.UPDATE_AN_POST);
        Links deleteAnPostLinks = mapToDecodedLinkWithProfileLink(postIdPathUri, DocsRelations.DELETE_AN_POST);
        return Links.of(createPostLinks).and(getAnPostLinks).and(updateAnPostLinks).and(deleteAnPostLinks);
    }
}
