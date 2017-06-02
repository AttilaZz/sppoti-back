import com.fr.commons.utils.SppotiUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by djenanewail on 6/2/17.
 */
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
}
