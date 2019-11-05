package models.repository;

import infra.models.Repository;
import models.domains.Perfil;

import java.util.Optional;

public interface PerfilRepository extends Repository<Perfil, Long> {

    Optional<String> buscarDescricaoPorId( final Long id );
}
