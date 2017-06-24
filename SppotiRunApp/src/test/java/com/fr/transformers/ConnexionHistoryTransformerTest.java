package com.fr.transformers;

import com.fr.commons.dto.ConnexionHistoryDto;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.ConnexionHistoryEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by djenanewail on 6/24/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConnexionHistoryTransformerTest
{
	@Autowired
	private ConnexionHistoryTransformer connexionHistoryTransformer;
	
	@Test
	public void testModelToDto() {
		final ConnexionHistoryEntity entity = new ConnexionHistoryEntity();
		entity.setCity(SppotiUtils.randomString(20));
		entity.setCountryCode(SppotiUtils.randomString(20));
		entity.setCountryName(SppotiUtils.randomString(20));
		entity.setLatitude(SppotiUtils.randomString(20));
		entity.setLongitude(SppotiUtils.randomString(20));
		entity.setMetroCode(SppotiUtils.randomString(20));
		entity.setRegionCode(SppotiUtils.randomString(20));
		entity.setRegionName(SppotiUtils.randomString(20));
		entity.setIp(SppotiUtils.randomString(20));
		entity.setVersion(SppotiUtils.randomInteger(3));
		entity.setUuid(SppotiUtils.randomString(30));
		
		final ConnexionHistoryDto dto = this.connexionHistoryTransformer.modelToDto(entity);
		
		Assert.assertNotNull(dto);
		Assert.assertEquals(entity.getCity(), dto.getCity());
		Assert.assertEquals(entity.getCountryCode(), dto.getCountryCode());
		Assert.assertEquals(entity.getCountryName(), dto.getCountryName());
		Assert.assertEquals(entity.getLatitude(), dto.getLatitude());
		Assert.assertEquals(entity.getLongitude(), dto.getLongitude());
		Assert.assertEquals(entity.getMetroCode(), dto.getMetroCode());
		Assert.assertEquals(entity.getMetroCode(), dto.getMetroCode());
		Assert.assertEquals(entity.getRegionName(), dto.getRegionName());
		Assert.assertEquals(entity.getIp(), dto.getIp());
		Assert.assertEquals(entity.getVersion(), dto.getVersion());
		Assert.assertEquals(entity.getUuid(), dto.getId());
	}
	
	@Test
	public void testDtoToModel() {
		final ConnexionHistoryDto dto = new ConnexionHistoryDto();
		dto.setCity(SppotiUtils.randomString(20));
		dto.setCountryCode(SppotiUtils.randomString(20));
		dto.setCountryName(SppotiUtils.randomString(20));
		dto.setLatitude(SppotiUtils.randomString(20));
		dto.setLongitude(SppotiUtils.randomString(20));
		dto.setMetroCode(SppotiUtils.randomString(20));
		dto.setRegionCode(SppotiUtils.randomString(20));
		dto.setRegionName(SppotiUtils.randomString(20));
		dto.setIp(SppotiUtils.randomString(20));
		dto.setVersion(SppotiUtils.randomInteger(3));
		dto.setUserId(SppotiUtils.randomLong(5));
		
		final ConnexionHistoryEntity entity = this.connexionHistoryTransformer.dtoToModel(dto);
		
		Assert.assertNotNull(entity);
		Assert.assertEquals(dto.getCity(), entity.getCity());
		Assert.assertEquals(dto.getCountryCode(), entity.getCountryCode());
		Assert.assertEquals(dto.getCountryName(), entity.getCountryName());
		Assert.assertEquals(dto.getLatitude(), entity.getLatitude());
		Assert.assertEquals(dto.getLongitude(), entity.getLongitude());
		Assert.assertEquals(dto.getMetroCode(), entity.getMetroCode());
		Assert.assertEquals(dto.getMetroCode(), entity.getMetroCode());
		Assert.assertEquals(dto.getRegionName(), entity.getRegionName());
		Assert.assertEquals(dto.getIp(), entity.getIp());
		Assert.assertEquals(dto.getVersion(), entity.getVersion());
		Assert.assertEquals(dto.getUserId(), entity.getUser().getId());
	}
	
	
}
