package com.fr.repositories;

import com.fr.entities.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by Moi on 27-Nov-16.
 */
@Service
public interface UserRepository extends JpaRepository<Users, Long> {

    Users getByEmail(String email);

    Users getByTelephone(String telephone);

    Users getByUsername(String username);

    Users getByConfirmationCode(String code);

    Users getByIdAndDeletedFalse(Long id);

    List<Users> getByUuid(int id);

    @PostFilter("!filterObject.isDeleted() AND filterObject.isConfirmed()")
    @Query("SELECT u from Users u WHERE u.username LIKE CONCAT('%',:prefix,'%') OR u.firstName LIKE CONCAT('%',:prefix,'%') OR u.lastName LIKE CONCAT('%',:prefix,'%') ")
    List<Users> getSearchedUsers(@Param("prefix") String userPrefix, Pageable pageable);

    @PostFilter("!filterObject.isDeleted() AND filterObject.isConfirmed()")
    @Query("SELECT u from Users u WHERE (u.firstName LIKE CONCAT('%',:part1,'%') AND u.lastName LIKE CONCAT('%',:part2,'%')) OR (u.firstName LIKE CONCAT('%',:part2,'%') AND u.lastName LIKE CONCAT('%',:part1,'%')) ")
    List<Users> getSearchedUsersByFirstNameAndLastName(@Param("part1") String part1, @Param("part2") String part2, Pageable pageable);

}