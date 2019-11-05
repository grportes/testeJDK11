package models.repository;

import infra.exceptions.BusinessException;
import infra.models.Repository;
import io.vavr.Tuple2;
import models.domains.Parametro;

public interface ParametroRepository extends Repository<Parametro,Long> {

    Tuple2<Long, Long> reservarNNumeros(
        final Long idParametro,
        final int qtde
    ) throws BusinessException;
}
