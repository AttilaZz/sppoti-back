package com.fr.repositories;

import com.fr.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by djenanewail on 12/15/16.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
}
