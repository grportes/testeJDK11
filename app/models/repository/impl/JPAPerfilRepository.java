package models.repository.impl;

import infra.model.JPARepository;
import models.domains.Perfil;
import models.repository.PerfilRepository;

import javax.persistence.NoResultException;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public class JPAPerfilRepository
    extends JPARepository<Perfil,Long>
    implements PerfilRepository
{

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> buscarDescricaoPorId( final Long id ) {

        try {
            return ofNullable( getEm()
                .createNamedQuery("Perfil.buscarDescricaoPorId",String.class)
                .setParameter("id",id).getSingleResult()
            );
        } catch ( final NoResultException e ) {
            return empty();
        }
    }
}
