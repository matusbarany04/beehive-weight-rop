package com.buzzybees.master.beehives.actions;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ActionRepository extends CrudRepository<Action, Long> {

    @Query("SELECT a FROM Action a WHERE a.author = :author")
    Action[] getAllByAuthor(long author);

    @Query("SELECT a FROM Action a WHERE a.execution_time = :time")
    Action[] getAllByTime(long time);

    @Query("SELECT a FROM Action a WHERE a.author = :author AND a.execution_time = :time")
    Action[] getAllByAuthorAndTime(long author, long time);

    @Query("SELECT a FROM Action a WHERE a.id = :id")
    Action getActionById(long id);


    @Query("SELECT a FROM Action a WHERE a.beehive_id = :beehiveId")
    Action[] getActionsByBeehiveId(String beehiveId);

    @Query("SELECT a.id FROM Action a WHERE a.beehive_id = :beehive AND a.type = :actionType AND a.execution_time = :time")
    Optional<Long> getExistingActionId(String beehive, ActionType actionType, long time);

    default Action saveOrUpdate(Action action) {
        if(action.getType().singleInstance) {
            Optional<Long> existingId = getExistingActionId(action.getBeehive(), action.getType(), action.getExecutionTime());
            existingId.ifPresent(action::setId);
        }

        return save(action);
    }

    default void saveOrUpdateAll(List<Action> actions) {
        for(Action action : actions) saveOrUpdate(action);
    }



   /* @Modifying
    @Transactional
    @Query("DELETE FROM Action a WHERE a.done = true")
    void removeDoneActions();*/




}