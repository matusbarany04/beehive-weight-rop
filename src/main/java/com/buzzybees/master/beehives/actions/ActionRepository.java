package com.buzzybees.master.beehives.actions;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ActionRepository extends CrudRepository<Action, Long> {

    @Query("SELECT a FROM Action a WHERE a.author = :author")
    Action[] getAllByAuthor(long author);

    @Query("SELECT a FROM Action a WHERE a.time = :time")
    Action[] getAllByTime(long time);

    @Query("SELECT a FROM Action a WHERE a.author = :author AND a.time = :time")
    Action[] getAllByAuthorAndTime(long author, long time);

    @Query("SELECT a FROM Action a WHERE a.id = :id")
    Action[] getActionById(long id);


    @Query("SELECT a FROM Action a WHERE a.beehive_id = :beehiveId")
    Action[] getActionsByBeehiveId(String beehiveId);

   /* @Modifying
    @Transactional
    @Query("DELETE FROM Action a WHERE a.done = true")
    void removeDoneActions();*/




}