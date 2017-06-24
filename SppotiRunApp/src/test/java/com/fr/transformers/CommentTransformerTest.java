package com.fr.transformers;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.CommentEntity;
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
public class CommentTransformerTest
{
	@Autowired
	private CommentTransformer commentTransformer;
	
	@Test
	public void testModelToDto() {
		final CommentDTO dto = new CommentDTO();
		dto.setTimeZone(SppotiUtils.randomString(3));
		
		final CommentEntity entity = this.commentTransformer.dtoToModel(dto);
		
		Assert.assertNotNull(entity);
		Assert.assertEquals(dto.getTimeZone(), entity.getTimeZone());
	}
	
}
