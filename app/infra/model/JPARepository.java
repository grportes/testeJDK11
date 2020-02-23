package infra.model;

import infra.exceptions.BusinessException;
import infra.jpa.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public abstract class JPARepository<T extends Model, ID> implements Repository<T,ID> {

    private Class<T> modelClass;

    @SuppressWarnings("unchecked")
    public JPARepository() {

        final ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        this.modelClass = (Class) pt.getActualTypeArguments()[0];
    }

    protected EntityManager getEm() {

        return JPAUtil.getEm();
    }

    @Override
    public List<T> findAll() {

        return getEm().createQuery( format( "from %s", modelClass.getSimpleName() ), modelClass ).getResultList();
    }

    /**
     * JPA : persist
     */
    @Override
    public void save(T model) {

        try {
            beforeSave( model );
            getEm().persist( model );
            postSave( model );
        } catch ( BusinessException e ) {
            throw new PersistenceException( e );
        }
    }

    /**
     * JPA: antes de aplicar persist
     *
     * @param model
     * @throws BusinessException
     */
    protected void beforeSave( T model ) throws BusinessException {

    }

    /**
     * JPA: após de aplicar persist
     *
     * @param model
     * @throws BusinessException
     */
    protected void postSave( T model ) throws BusinessException {

    }

    @Override
    public void deleteAll() {

        final String query = format("delete from %s", modelClass.getSimpleName());
        getEm().createQuery(query).executeUpdate();
    }

    @Override
    public Optional<T> findById( final ID id ) {

        return ofNullable( getEm().find( modelClass, id ) );
    }

    @Override
    public long findCount() {

        final String query = format( "select count(1) from %s", modelClass.getSimpleName() );
        return getEm().createQuery(query, Long.class).getSingleResult();
    }


}