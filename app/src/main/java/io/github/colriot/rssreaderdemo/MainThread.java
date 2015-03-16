package io.github.colriot.rssreaderdemo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         15/03/15
 */
@Qualifier @Retention(RetentionPolicy.RUNTIME)
public @interface MainThread {
}
