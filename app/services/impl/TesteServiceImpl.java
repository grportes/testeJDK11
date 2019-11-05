package services.impl;

import infra.exceptions.BusinessException;
import infra.jpa.JPAUtil;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import models.domains.Perfil;
import models.repository.ParametroRepository;
import models.repository.PerfilRepository;
import services.TesteService;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public class TesteServiceImpl implements TesteService {

    private final PerfilRepository perfilRepository;
    private final ParametroRepository parametroRepository;
    private final JPAUtil jpaUtil;

    @Inject
    public TesteServiceImpl(
        final PerfilRepository perfilRepository,
        final ParametroRepository parametroRepository,
        final JPAUtil jpaUtil
    ) {

        this.perfilRepository = perfilRepository;
        this.parametroRepository = parametroRepository;
        this.jpaUtil = jpaUtil;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Tuple2<List<Perfil>,Integer>> processar( final Integer limite ) throws BusinessException {

        var sequencial = Try.of(() ->
            supplyAsync(() -> jpaUtil.withTransaction(() ->
                    Try.of( () -> parametroRepository.reservarNNumeros(11L, 20) )
                )).get())
            .getOrElseThrow(() -> new BusinessException("Falhou reservar n numeros"));

        if ( sequencial.isSuccess() ) {
            var tupla = sequencial.get();
            System.out.printf("\n%s - %s\n", tupla._1(), tupla._2() );
        }

        IntStream.of(1,2,3,4,5).forEach(value -> {
            Perfil perfil = new Perfil();
            perfil.setDescricao( format("teste - %s - %s", value, new Random(100).nextLong()) );
            perfilRepository.save(perfil);
        });

        final var lista = perfilRepository.findAll();
        if ( lista.size() > limite )
            throw new BusinessException("VAmos parar !!");

        return of( new Tuple2<>(lista, lista.size()) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void atualizarTodasDescricoes() {

        perfilRepository.findAll().forEach(perfil -> {
            var descricao = perfil.getDescricao();
            if ( isNotBlank(descricao) ) {
                perfil.setDescricao( format("%s-%s", perfil.getId(), descricao) );
                perfilRepository.save( perfil );
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void excluirTodos() {

        perfilRepository.deleteAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Perfil> buscarTodos() {

        return perfilRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long buscarTotalPerfils() {

        return perfilRepository.findCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> buscarDescricaoPorId( final Long id ) {

        return perfilRepository.buscarDescricaoPorId( id );
    }


}