package com.fr.repositories;

import com.fr.entities.SppoterEntity;
import com.fr.entities.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostFilter;
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
	
	UserEntity getByConfirmationCodeAndDeletedFalse(String code);
	
	UserEntity getByIdAndDeletedFalse(Long id);
	
	Optional<UserEntity> getByUuidAndDeletedFalse(int id);
	
	@PostFilter("filterObject.isConfirmed()")
	@Query("SELECT u from UserEntity u WHERE u.username LIKE CONCAT('%',:prefix,'%') OR u.firstName LIKE CONCAT('%',:prefix,'%') OR u.lastName LIKE CONCAT('%',:prefix,'%') AND u.deleted = false ")
	List<UserEntity> getSearchedUsers(@Param("prefix") String userPrefix, Pageable pageable);
	
	@PostFilter("filterObject.isConfirmed()")
	@Query("SELECT u from UserEntity u WHERE (u.firstName LIKE CONCAT('%',:part1,'%') AND u.lastName LIKE CONCAT('%',:part2,'%')) OR (u.firstName LIKE CONCAT('%',:part2,'%') AND u.lastName LIKE CONCAT('%',:part1,'%')) AND u.deleted = false")
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
}