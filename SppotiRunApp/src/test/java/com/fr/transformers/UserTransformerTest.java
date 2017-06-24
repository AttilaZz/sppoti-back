package com.fr.transformers;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.GenderEnum;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.RoleEntity;
import com.fr.entities.UserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djenanewail on 6/24/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTransformerTest
{
	@Autowired
	private UserTransformer userTransformer;
	
	@Test
	public void testModelToDto() {
		final UserEntity entity = new UserEntity();
		entity.setGender(GenderEnum.MALE);
		entity.setUsername(SppotiUtils.randomString(9));
		entity.setLastName(SppotiUtils.randomString(9));
		entity.setFirstName(SppotiUtils.randomString(9));
		entity.getRoles().add(new RoleEntity());
		entity.setTimeZone(SppotiUtils.randomString(3));
		entity.setConfirmationCode(SppotiUtils.randomString(30));
		entity.setConfirmed(false);
		entity.setTelephone(SppotiUtils.randomString(30));
		
		final UserDTO dto = this.userTransformer.modelToDto(entity);
		
		Assert.assertNotNull(dto);
		Assert.assertEquals(entity.getGender().name(), dto.getGender());
		Assert.assertEquals(entity.getUsername(), dto.getUsername());
		Assert.assertEquals(entity.getLastName(), dto.getLastName());
		Assert.assertEquals(entity.getFirstName(), dto.getFirstName());
		Assert.assertEquals(entity.getRoles().size(), dto.getUserRoles().size());
		Assert.assertEquals(entity.getTimeZone(), dto.getTimeZone());
		Assert.assertEquals(entity.isConfirmed(), dto.isConfirmed());
		Assert.assertEquals(entity.getTelephone(), dto.getPhone());
	}
	
	@Test
	public void testDtoToModel() {
	
	}
	
	@Test
	public void testIterableModelsToDtos() {
		final List<UserEntity> userEntities = new ArrayList<>();
		final UserEntity entity = new UserEntity();
		entity.setUsername(SppotiUtils.randomString(9));
		entity.setLastName(SppotiUtils.randomString(9));
		entity.setFirstName(SppotiUtils.randomString(9));
		entity.setGender(GenderEnum.MALE);
		entity.getRoles().add(new RoleEntity());
		userEntities.add(entity);
		
		final List<UserDTO> userDTOS = this.userTransformer.iterableModelsToDtos(userEntities);
		
		Assert.assertEquals(1, userDTOS.size());
	}
	
	
}
