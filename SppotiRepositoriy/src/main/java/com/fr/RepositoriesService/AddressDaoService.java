package com.fr.RepositoriesService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.entities.Address;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */
@Service
public interface AddressDaoService extends GenericDaoService<Address, Integer> {

	/**
	 * @param id
	 * @return
	 */
	List<Address> getLastPostAddress(Long id);


}
