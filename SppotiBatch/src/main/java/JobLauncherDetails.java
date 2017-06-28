import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Batch quartz  scheduler.
 *
 * Created by wdjenane on 28/06/2017.
 */
public class JobLauncherDetails extends QuartzJobBean {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeInternal(final JobExecutionContext jobExecutionContext) {

    }
}
