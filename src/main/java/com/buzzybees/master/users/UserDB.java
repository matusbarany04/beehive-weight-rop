package com.buzzybees.master.users;


import org.springframework.data.repository.Repository;

public interface UserDB<T, ID> extends Repository<T, ID> {

    <S extends T> void save(S entity);

}
