package io.example.board.controller;

import lombok.val;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.IanaLinkRelations.INDEX;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
        val indexRepresentationModel = new RepresentationModel();
        indexRepresentationModel.add(linkTo(this.getClass()).withRel(INDEX));
        return indexRepresentationModel;
    }
}
