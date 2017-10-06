package com.fr;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * Created by djenanewail on 10/4/17.
 */
public class Initializer extends AbstractHttpSessionApplicationInitializer
{
	public Initializer() {
		super(SessionConfig.class);
	}
}
