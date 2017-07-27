package com.fr.business;

import com.fr.commons.utils.SppotiUtils;
import com.fr.config.MyTestsConfiguration;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by djenanewail on 7/27/17.
 */

@RunWith(SpringRunner.class)
@Import(MyTestsConfiguration.class)
public class PasswordEncodingTest
{
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	public void testPasswordEncoding() {
		final String password = StringEscapeUtils.escapeJava("piratusse++");
		final String password64 = SppotiUtils.encodeTo64(password);
		
		Assert.assertEquals(password, SppotiUtils.decode64ToString(password64));
		
		final String encodedPassword = this.passwordEncoder.encode(password64);
		
		Assert.assertTrue(this.passwordEncoder.matches(password64, encodedPassword));
	}
}
