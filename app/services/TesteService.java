package services;

import infra.exceptions.BusinessException;
import io.vavr.Tuple2;
import models.domains.Perfil;

import java.util.List;
import java.util.Optional;

public interface TesteService {

    Optional<Tuple2<List<Perfil>,Integer>> processar( final Integer limite ) throws BusinessException;

    void atualizarTodasDescricoes();

    void excluirTodos();

    List<Perfil> buscarTodos();

    long buscarTotalPerfils();

    Optional<String> buscarDescricaoPorId( final Long id );

    void simularErros();
}
