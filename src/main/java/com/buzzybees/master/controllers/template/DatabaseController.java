package com.buzzybees.master.controllers.template;

import com.buzzybees.master.beehives.actions.Action;
import org.attoparser.ParseException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
public abstract class DatabaseController implements ApplicationContextAware {

    @Autowired
    private ApplicationContext applicationContext;

    private static Repositories repositories;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        repositories = new Repositories(applicationContext);
    }

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

    /**
     * @param entity which repository store
     * @param <R> CrudRepository object
     * @param <E> Entity object
     * @return repository by entity in static context
     */
    @SuppressWarnings("unchecked")
    public static <R, I extends CrudRepository<E, I>, E> R accessRepo(Class<E> entity) {
        Optional<Object> repo = repositories.getRepositoryFor(entity);
        return (R) repo.orElse(null);
    }

    @ExceptionHandler(ParseException.class)
    public ApiResponse handleException(Exception e) {
        return new ApiResponse("ERR_WRONG_DATE");
    }
}


