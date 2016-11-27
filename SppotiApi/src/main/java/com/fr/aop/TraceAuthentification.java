/**
 * 
 */
package com.fr.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */

@Aspect
@Component
public class TraceAuthentification {

    private static Logger LOGGER = Logger.getLogger( TraceAuthentification.class );

    // @Around( "execution(*
    // com.dz.security.UserDetailServiceImpl.loadUserByUsername(..))" )
    public Object afficherTrace( final ProceedingJoinPoint joinpoint )
            throws Throwable {
        String nomMethode = joinpoint.getTarget().getClass().getSimpleName() + "."
                + joinpoint.getSignature().getName();
        final Object[] args = joinpoint.getArgs();
        final StringBuffer sb = new StringBuffer();

        sb.append( joinpoint.getSignature().toString() );
        sb.append( " avec les parametres : (" );
        for ( int i = 0; i < args.length; i++ ) {
            sb.append( args[i] );
            if ( i < args.length - 1 ) {
                sb.append( ", " );
            }
        }
        sb.append( ")" );

        LOGGER.info( "Debut methode : " + sb );
        Object obj = null;
        try {
            obj = joinpoint.proceed();
        } finally {
            LOGGER.info( "Fin methode :  " + nomMethode + " retour=" + obj );
        }
        return obj;
    }

}
