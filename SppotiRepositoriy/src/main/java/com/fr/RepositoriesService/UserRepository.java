package com.fr.RepositoriesService;

import com.fr.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Moi on 27-Nov-16.
 */
@Service
public interface UserRepository extends JpaRepository<Users, Integer> {

    Users getByEmail(String email);

    Users getByTelephone(String telephone);

    Users getByUsername(String username);
}
