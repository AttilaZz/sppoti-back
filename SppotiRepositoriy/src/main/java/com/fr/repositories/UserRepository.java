package com.fr.repositories;

import com.fr.entities.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

    Users getById(Long id);

    List<Users> getByUsernameContaining(String userPrefix, Pageable pageable);
}
