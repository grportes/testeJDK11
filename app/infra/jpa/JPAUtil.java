package infra.jpa;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

public final class JPAUtil {

    private static final ThreadLocal<EntityManager> contexto = new ThreadLocal<>();

    private final JPAApi jpaApi;

    @Inject
    public JPAUtil( final JPAApi jpaApi ) {

        this.jpaApi = jpaApi;
    }

    public <T> T withTransaction(
        final boolean readOnly,
        final Supplier<T> block
    ) {

        return jpaApi.withTransaction("default", readOnly, em -> {
            try {
                contexto.set(em);
                return block.get();
            } finally {
                contexto.remove();
            }
        });
    }

    public <T> T withTransaction( final Supplier<T> block ) {

        return withTransaction( false, block );
    }

    public void rollback() {

        if ( !getTransaction(EntityTransaction::setRollbackOnly) )
            throw new NullPointerException( "Falhou Rollback / EntityManager não foi carregado!!" );
    }

    public void commit() {

        if ( !getTransaction(EntityTransaction::commit) )
            throw new NullPointerException( "Falhou Commit / EntityManager não foi carregado!!" );
    }

    private boolean getTransaction( final Consumer<EntityTransaction> block ) {

        final EntityManager em = contexto.get();
        if ( nonNull(em) ) {
            final EntityTransaction transaction = em.getTransaction();
            if ( nonNull( transaction ) && transaction.isActive() ) {
                block.accept(transaction);
                if ( !transaction.isActive() ) transaction.begin();
                return true;
            }
        }

        return false;
    }

    public void flushAndClear() {

        final EntityManager em = contexto.get();
        if ( nonNull(em) ) {
            em.flush();
            em.clear();
        }
    }

    public void flush() {

        final EntityManager em = contexto.get();
        if ( nonNull(em) ) em.flush();
    }

    public static EntityManager getEm() {

        final EntityManager em = contexto.get();
        if ( nonNull(em) ) return em;

        throw new NullPointerException( "EntityManager não foi carregado! Verifique uso de jpaUtil.withTransaction" );
    }

}