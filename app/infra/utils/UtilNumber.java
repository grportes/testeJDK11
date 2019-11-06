package infra.utils;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public final class UtilNumber {

    public static Optional<Long> createLong( final String str ) {

        try {
            return of( Long.valueOf( str ) );
        } catch ( final Throwable e ) {
            return empty();
        }
    }

    public static Long createLong(
        final String str,
        final Long defaultValue
    ) {

        try {
            return Long.valueOf( str );
        } catch ( final Throwable e ) {
            return defaultValue;
        }
    }
}
