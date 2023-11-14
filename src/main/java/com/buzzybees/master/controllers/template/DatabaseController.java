package com.buzzybees.master.controllers.template;

import org.attoparser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
public abstract class DatabaseController {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * @param entity which repository store
     * @param <R> CrudRepository object
     * @param <E> Entity object
     * @return repository by entity
     */
    @SuppressWarnings("unchecked")
    public <R, I extends CrudRepository<E, I>, E> R getRepo(Class<E> entity) {
        Repositories repositories = new Repositories(applicationContext);
        Optional<Object> repo = repositories.getRepositoryFor(entity);
        return (R) repo.orElse(null);
    }

    @ExceptionHandler(ParseException.class)
    public ApiResponse handleException(Exception e) {
        return new ApiResponse("ERR_WRONG_DATE");
    }
}


