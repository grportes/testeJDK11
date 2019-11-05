package services;

import com.google.inject.AbstractModule;
import services.impl.TesteServiceImpl;

public class Module extends AbstractModule {

    @Override
    protected void configure() {

        bind( TesteService.class ).to( TesteServiceImpl.class );
    }
}
