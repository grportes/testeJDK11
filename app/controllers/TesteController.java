package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import infra.controllers.Controller;
import infra.routes.LongBinder;
import models.domains.Perfil;
import play.mvc.Result;
import play.mvc.Results;
import services.TesteService;

import javax.inject.Inject;
import java.util.List;

import static infra.routes.LongBinder.getValue;
import static play.libs.Json.newObject;
import static play.libs.Json.toJson;

public class TesteController extends Controller {

    private final TesteService testeService;

    @Inject
    public TesteController( final TesteService testeService ) {

        this.testeService = testeService;
    }

    public Result testar() {

        return withTransaction( () -> testeService
            .processar(100 )
            .map( tupla -> {
                final ObjectNode json = newObject();
                json.put("total", tupla._2());
                json.putArray("lista").add( toJson(tupla._1()) );
                return json;
            })
            .map( Results::ok )
            .orElse( noContent() )
        );
    }

    public Result buscar(
        final LongBinder idBinder,
        final String query
    ) {

        switch ( query ) {
            case "descricao":
                return withTransaction( () -> testeService
                    .buscarDescricaoPorId( getValue(idBinder) )
                    .map( descricao -> newObject().put( "descricao", descricao ) )
                    .map( Results::ok )
                    .orElse( noContent() )
                );
        }

        return badRequest( "Obrigatório definir '&query='" );
    }

    public Result buscarTodos() {

        return withTransaction( true, () -> {
            List<Perfil> perfils = testeService.buscarTodos();
            return ok(toJson(perfils));
        });
    }

    public Result buscarQtde() {

        return withTransaction( true,  () ->
            ok( newObject().put("total", testeService.buscarTotalPerfils()) )
        );
    }

    public Result excluirTodos() {

        return withTransaction(() -> {
            testeService.excluirTodos();
            return ok();
        });
    }

    public Result atualizarDescricao() {

        return withTransaction(() -> {
            testeService.atualizarTodasDescricoes();
            return ok();
        });
    }

    public Result testarErro() {

        return withTransaction(() -> {

            testeService.simularErros();
            return ok();
        });
    }

}
