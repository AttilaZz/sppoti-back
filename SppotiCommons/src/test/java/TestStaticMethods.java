import com.fr.commons.utils.SppotiUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZoneId;
import java.util.Date;

/**
 * Created by djenanewail on 6/2/17.
 */

@RunWith(SpringRunner.class)
public class TestStaticMethods
{
	@Test
	public void testTimeZone() {
		
		final Date date = new Date();
		System.out.println("Before: " + date);
		
		final Date correctDate = SppotiUtils.dateWithTimeZone(date, "+06");
		
		System.out.println("After: " + correctDate);
		
		Assert.assertNotNull(correctDate);
		Assert.assertNotEquals(date, correctDate);
	}
	
	@Test
	public void testDateOfBornGenerator() {
		final int age = 25;
		
		final Date dateOfBorn = SppotiUtils.getDateOfBorn(age);
		
		Assert.assertEquals(1992, dateOfBorn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear());
	}
}
