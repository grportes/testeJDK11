package models;

import com.google.inject.AbstractModule;
import models.repository.ParametroRepository;
import models.repository.PerfilRepository;
import models.repository.impl.JPAParametroRepository;
import models.repository.impl.JPAPerfilRepository;

public class Module extends AbstractModule {

    @Override
    protected void configure() {

        bind( PerfilRepository.class ).to( JPAPerfilRepository.class );
        bind( ParametroRepository.class ).to( JPAParametroRepository.class );
    }
}