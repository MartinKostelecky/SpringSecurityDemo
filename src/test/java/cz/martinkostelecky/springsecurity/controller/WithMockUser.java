package cz.martinkostelecky.springsecurity.controller;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


//meta-annotation (annotates other annotation) that comes with several retention policies.
//with RUNTIME retention policy other annotation is retained during runtime and can be accessed in our program during runtime
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserSecurityContextFactory.class)
public @interface WithMockUser {

    //an annotation element declaration with default value
    long userId() default 1;

    String[] authorities() default "USER";
}
