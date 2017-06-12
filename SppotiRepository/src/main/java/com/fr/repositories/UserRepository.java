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
	
	UserEntity getByEmailAndDeletedFalseAndConfirmedTrue(String email);
	
	UserEntity getByTelephoneAndDeletedFalseAndConfirmedTrue(String telephone);
	
	UserEntity getByUsernameAndDeletedFalseAndConfirmedTrue(String username);
	
	UserEntity getByConfirmationCodeAndDeletedFalseAndConfirmedFalse(String code);
	
	UserEntity getByIdAndDeletedFalseAndConfirmedTrue(Long id);
	
	Optional<UserEntity> getByUuidAndDeletedFalseAndConfirmedTrue(int id);
	
	@Query("SELECT u from UserEntity u WHERE u.deleted = false AND u.confirmed = true " +
			"AND (u.username LIKE CONCAT('%',:prefix,'%') " + "OR u.firstName LIKE CONCAT('%',:prefix,'%') " +
			"OR u.lastName LIKE CONCAT('%',:prefix,'%'))")
	List<UserEntity> getSearchedUsers(@Param("prefix") String userPrefix, Pageable pageable);
	
	@Query("SELECT u from UserEntity u WHERE u.deleted = false AND u.confirmed = true " +
			"AND (u.firstName LIKE CONCAT('%',:part1,'%') " + "AND u.lastName LIKE CONCAT('%',:part2,'%')) " +
			"OR (u.firstName LIKE CONCAT('%',:part2,'%') " + "AND u.lastName LIKE CONCAT('%',:part1,'%')) ")
	List<UserEntity> getSearchedUsersByFirstNameAndLastName(@Param("part1") String part1, @Param("part2") String part2,
															Pageable pageable);
	
	UserEntity getByRecoverCodeAndDeletedFalseAndConfirmedTrue(String code);
	
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
			"AND s.deleted = FALSE AND s.confirmed = TRUE AND s.id NOT IN (:existingSppoter)")
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
	Optional<UserEntity> getByUuidAndConfirmedTrueAndDeletedFalseAndConfirmedTrue(int userId);
	
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

	/**
	 * Find all existing users accounts by email.
	 * Otherwise confirmed or not.
	 *
	 * @param email user email.
	 * @return User account.
	 */
	UserEntity getByEmailAndDeletedFalse(String email);
}