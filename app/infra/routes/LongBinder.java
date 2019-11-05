package infra.routes;

import play.mvc.PathBindable;
import play.mvc.QueryStringBindable;

import java.util.Map;
import java.util.Optional;

import static java.util.Objects.nonNull;

public class LongBinder implements QueryStringBindable<LongBinder>  {

    private Long value;

    public LongBinder() {

        this.value = null;
    }

    public LongBinder(Long value) {
        this.value = value;
    }

    @Override
    public Optional<LongBinder> bind(
        final String field,
        final Map<String, String[]> data
    ) {


        data.get(field);

        return Optional.empty();
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
