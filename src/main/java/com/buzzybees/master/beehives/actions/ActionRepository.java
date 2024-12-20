package com.buzzybees.master.beehives.actions;

import org.json.JSONObject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface ActionRepository extends CrudRepository<Action, Long> {

    @Query("SELECT a FROM Action a WHERE a.author = :author")
    Action[] getAllByAuthor(long author);

    @Query("SELECT a FROM Action a WHERE a.executionTime = :time")
    Action[] getAllByTime(long time);

    @Query("SELECT a FROM Action a WHERE a.author = :author AND a.executionTime = :time")
    Action[] getAllByAuthorAndTime(long author, long time);

    @Query("SELECT a FROM Action a WHERE a.id = :id")
    Action getActionById(long id);

    @Query("SELECT a FROM Action a WHERE a.beehive = :beehiveId")
    Action[] getActionsByBeehiveId(String beehiveId);

    @Query("SELECT a FROM Action a WHERE a.beehive = :beehiveId AND (a.status = 'PENDING' OR a.status = 'SENT')")
    Action[] getPendingActionsByBeehiveId(String beehiveId);

    @Query("SELECT a FROM Action a WHERE a.beehive = :beehive AND a.type = :actionType AND a.executionTime = :time AND a.status = :status")
    Optional<Action> getExistingActionId(String beehive, ActionType actionType, long time, ActionStatus status);


    default Action saveOrUpdate(Action action) {
        if(action.getType().singleInstance) {
            Optional<Action> actionDB = getExistingActionId(action.getBeehive(), action.getType(), action.getExecutionTime(), action.getStatus());
            if(actionDB.isPresent()) {
                Action oldAction = actionDB.get();
                if(action.getType() == ActionType.CHANGE_BEEHIVE_CONFIG && action.getParams().size() == 0) {
                    delete(oldAction);
                    return oldAction;
                } else {
                    action.setId(oldAction.getId());
                    HashMap<String, Object> params = oldAction.getParams();
                    params.putAll(action.getParams());
                    action.setParamsMap(params);
                }
            }
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