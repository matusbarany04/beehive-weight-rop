package com.buzzybees.master.users;

import com.buzzybees.master.tables.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.id = :id")
    User getUserById(Long id);

    @Query("SELECT u.email FROM User u WHERE u.id = :userId")
    String getEmail(long userId);

    //throws error when duplicate emails are in db
    @Query("SELECT u FROM User u WHERE u.email LIKE :email")
    User getUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email LIKE :email AND u.password LIKE :passwdHash")
    User checkUser(String email, String passwdHash);

    @Modifying
    @Query("UPDATE User u SET u.dashboardData = :dashboardData WHERE u.id = :userId")
    void saveDashboard(Long userId, String dashboardData);

    @Modifying
    @Query("UPDATE User u SET u.verified = true WHERE u.id = :id")
    void verifyUser(long id);

}
