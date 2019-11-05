package infra.controllers;

import infra.functions.ThrowingSupplier;
import infra.jpa.JPAUtil;
import play.mvc.Result;

import javax.inject.Inject;

import static infra.utils.UtilException.toJson;

public class Controller extends play.mvc.Controller {

    @Inject
    private JPAUtil jpaUtil;

    protected Result withTransaction( final ThrowingSupplier<Result> block ) {

        return withTransaction( false, block );
    }

    protected Result withTransaction(
        final boolean readOnly,
        final ThrowingSupplier<Result> block
    ) {
        return jpaUtil.withTransaction( readOnly, () -> {
            try {
                return block.get();
            } catch ( final Throwable e ) {
                jpaUtil.rollback();
                return badRequest( toJson(e) );
            }
        });
    }

}
