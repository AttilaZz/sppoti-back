package com.fr.repositories;

import com.fr.entities.SppoterEntity;
import com.fr.entities.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by Moi on 27-Nov-16.
 */
@Service
public interface UserRepository extends JpaRepository<UserEntity, Long>
{
	
	UserEntity getByEmailAndDeletedFalse(String email);
	
	UserEntity getByTelephoneAndDeletedFalse(String telephone);
	
	UserEntity getByUsernameAndDeletedFalse(String username);
	
	UserEntity getByConfirmationCodeAndDeletedFalseAndConfirmedFalse(String code);
	
	UserEntity getByIdAndDeletedFalse(Long id);
	
	Optional<UserEntity> getByUuidAndDeletedFalse(int id);
	
	@Query("SELECT u from UserEntity u WHERE u.username LIKE CONCAT('%',:prefix,'%') " +
			"OR u.firstName LIKE CONCAT('%',:prefix,'%') " + "OR u.lastName LIKE CONCAT('%',:prefix,'%') " +
			"AND u.deleted = false AND u.confirmed = true")
	List<UserEntity> getSearchedUsers(@Param("prefix") String userPrefix, Pageable pageable);
	
	@Query("SELECT u from UserEntity u WHERE (u.firstName LIKE CONCAT('%',:part1,'%') " +
			"AND u.lastName LIKE CONCAT('%',:part2,'%')) " + "OR (u.firstName LIKE CONCAT('%',:part2,'%') " +
			"AND u.lastName LIKE CONCAT('%',:part1,'%')) " + "AND u.deleted = false AND u.confirmed = true")
	List<UserEntity> getSearchedUsersByFirstNameAndLastName(@Param("part1") String part1, @Param("part2") String part2,
															Pageable pageable);
	
	UserEntity getByRecoverCodeAndDeletedFalse(String code);
	
	/**
	 * Find all sppoter allowed to join sppoti.
	 *
	 * @param prefix
	 * 		sppoter prefix name.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of {@link SppoterEntity}
	 */
	@Query("SELECT s FROM UserEntity s WHERE (s.username LIKE CONCAT('%', :prefix ,'%')" +
			"OR s.firstName LIKE CONCAT('%', :prefix, '%')" + "OR s.lastName LIKE CONCAT('%', :prefix, '%'))" +
			"AND s.id NOT IN (:existingSppoter)")
	List<UserEntity> findAllAllowedSppoter(@Param("prefix") String prefix,
										   @Param("existingSppoter") List existingSppoter, Pageable pageable);
	
	/**
	 * Find user by id if account is confirmed and not deleted.
	 *
	 * @param userId
	 * 		user id.
	 *
	 * @return user account data.
	 */
	Optional<UserEntity> getByUuidAndConfirmedTrueAndDeletedFalse(int userId);
	
	/**
	 * Find user in login process by email.
	 */
	UserEntity findByEmail(String username);
	
	/**
	 * Find user in login process by phone number.
	 */
	UserEntity findByTelephone(String username);
	
	/**
	 * Find user in login process by username.
	 */
	UserEntity findByUsername(String username);
}