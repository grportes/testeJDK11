package infra.models;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository <T extends Serializable, ID> {

    List<T> findAll();

    void save( final T model );

    void deleteAll();

    Optional<T> findById( final ID id ) ;

    long findCount();
}