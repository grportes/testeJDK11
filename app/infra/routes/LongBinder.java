package infra.routes;

import play.mvc.QueryStringBindable;

import java.util.Map;
import java.util.Optional;

import static infra.utils.UtilNumber.createLong;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static org.apache.commons.lang.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.math.NumberUtils.isNumber;

public class LongBinder implements QueryStringBindable<LongBinder>  {

    private Long value;

    public LongBinder() {

        this.value = null;
    }

    public LongBinder( final Long value ) {

        this.value = value;
    }

    @Override
    public Optional<LongBinder> bind(
        final String field,
        final Map<String, String[]> data
    ) {

        return isNotBlank( field ) && nonNull( data ) && isNotEmpty( data.get(field) ) && isNumber( data.get(field)[0] )
            ? createLong( data.get(field)[0] ).map( LongBinder::new )
            : empty();
    }

    @Override
    public String unbind(String key) {

        return null;
    }

    @Override
    public String javascriptUnbind() {

        return null;
    }

    public static Long getValue( final LongBinder binder ) {

        return nonNull( binder ) ? binder.value : null;
    }

}
