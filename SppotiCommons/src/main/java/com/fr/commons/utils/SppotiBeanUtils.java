package com.fr.commons.utils;

import java.math.BigDecimal;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Add class description.
 * <p>
 * Created by wdjenane on 04/04/2017.
 */
public class SppotiBeanUtils {

    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SppotiBeanUtils.class);

    /**
     * Retourne une copie du bean.
     * @param dest Objet de destination des données à mapper
     * @param orig Objet contenant les données à mapper
     */
    public static void copyProperties(final Object dest, final Object orig)
    {
        try {
            final ConvertUtilsBean convertUtilsBean = BeanUtilsBean.getInstance().getConvertUtils();
            convertUtilsBean.register(new StringConverter(null), String.class);
            convertUtilsBean.register(new LongConverter(null), Long.class);
            convertUtilsBean.register(new BooleanConverter(null), Boolean.class);
            convertUtilsBean.register(new IntegerConverter(null), Integer.class);
            convertUtilsBean.register(new BigDecimalConverter(null), BigDecimal.class);
            BeanUtils.copyProperties(dest, orig);
        }
        catch (final Exception exception) {
            LOGGER.error(exception.getMessage(), exception);
            throw new UnsupportedOperationException("Impossible de copier les deux beans",
                    exception);
        }
    }

    /**
     * Retourne une copie du bean de class B.
     * @param clazz Classe de l'objet à instancier
     * @param orig Objet contenant les données à mapper
     * @param <B> La classe de l'objet à instancier
     * @return objet de class B
     */
    public static <B> B copyProperties(final Class<B> clazz, final Object orig)
    {
        try {
            final B o = clazz.newInstance();
            final ConvertUtilsBean convertUtilsBean = BeanUtilsBean.getInstance().getConvertUtils();
            convertUtilsBean.register(new LongConverter(null), Long.class);
            convertUtilsBean.register(new BooleanConverter(null), Boolean.class);
            convertUtilsBean.register(new IntegerConverter(null), Integer.class);
            convertUtilsBean.register(new BigDecimalConverter(null), BigDecimal.class);
            BeanUtils.copyProperties(o, orig);
            return o;
        }
        catch (final Exception exception) {
            LOGGER.error(exception.getMessage(), exception);
            throw new UnsupportedOperationException("Impossible de copier les deux beans",
                    exception);
        }
    }

}
