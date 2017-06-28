package processor;

import com.fr.entities.SppotiEntity;
import org.springframework.batch.item.ItemProcessor;

/**
 * process {@link SppotiEntity}.
 *
 * Created by wdjenane on 28/06/2017.
 */
public class SppotiItemProcessor implements ItemProcessor<SppotiEntity, SppotiEntity> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SppotiEntity process(final SppotiEntity sppotiEntity) throws Exception {
        return null;
    }

}
