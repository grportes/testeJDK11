package models.repository.impl;

import infra.exceptions.BusinessException;
import infra.models.JPARepository;
import io.vavr.Tuple2;
import models.domains.Parametro;
import models.repository.ParametroRepository;

import static java.lang.String.format;

public class JPAParametroRepository
    extends JPARepository<Parametro,Long>
    implements ParametroRepository
{

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Tuple2<Long, Long> reservarNNumeros(
        final Long idParametro,
        final int qtReserva
    ) throws BusinessException {

        var parametro = findById(idParametro)
            .orElseThrow( () -> new BusinessException(format("Parametro [%s] não localizado", idParametro) ) );

        var qtLock = getEm()
            .createNamedQuery( "Perfil.updateLock" )
            .setParameter("id", idParametro)
            .setParameter("version", parametro.getVersion() )
            .executeUpdate();

        if ( qtLock != 1 )
            throw new BusinessException(format("Parametro [%s] alterado entre leitura e gravação", idParametro) );

        var soma = parametro.getNumero() + qtReserva;
        var intervalo = new Tuple2<Long, Long>(parametro.getNumero(), soma);

        parametro.setNumero( soma + 1 );
        save( parametro );

        return intervalo;
    }
}
