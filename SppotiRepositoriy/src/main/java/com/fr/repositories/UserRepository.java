package com.fr.repositories;

import com.fr.entities.UserEntity;
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
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity getByEmail(String email);

    UserEntity getByTelephone(String telephone);

    UserEntity getByUsername(String username);

    UserEntity getByConfirmationCode(String code);

    UserEntity getByIdAndDeletedFalse(Long id);

    List<UserEntity> getByUuid(int id);

    @PostFilter("!filterObject.isDeleted() AND filterObject.isConfirmed()")
    @Query("SELECT u from UserEntity u WHERE u.username LIKE CONCAT('%',:prefix,'%') OR u.firstName LIKE CONCAT('%',:prefix,'%') OR u.lastName LIKE CONCAT('%',:prefix,'%') ")
    List<UserEntity> getSearchedUsers(@Param("prefix") String userPrefix, Pageable pageable);

    @PostFilter("!filterObject.isDeleted() AND filterObject.isConfirmed()")
    @Query("SELECT u from UserEntity u WHERE (u.firstName LIKE CONCAT('%',:part1,'%') AND u.lastName LIKE CONCAT('%',:part2,'%')) OR (u.firstName LIKE CONCAT('%',:part2,'%') AND u.lastName LIKE CONCAT('%',:part1,'%')) ")
    List<UserEntity> getSearchedUsersByFirstNameAndLastName(@Param("part1") String part1, @Param("part2") String part2, Pageable pageable);

}