package infra.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import infra.exceptions.BusinessException;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.Arrays;
import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang.exception.ExceptionUtils.getRootCauseMessage;
import static play.libs.Json.newObject;

public final class UtilException {

    public static JsonNode toJson( final Throwable throwable ) {

        final ObjectNode erro = newObject();

        if ( throwable instanceof BusinessException )
            erro.put("msg", throwable.getMessage() );
        else
            erro.put("msg", getRootCauseMessage(throwable) );

        Predicate<? super String> pacotes = stack ->
            stack.startsWith("infra")
            || stack.startsWith("controllers")
            || stack.startsWith("services")
            || stack.startsWith("models")
            || stack.startsWith("views")
        ;

        erro.put( "stack",
            Arrays.stream( ExceptionUtils.getRootCauseStackTrace(throwable) )
                .map(stack -> stack.replaceAll("\tat ", ""))
                .filter(pacotes)
                .collect(joining("  :  "))
        );

        return newObject().set("erro", erro);
    }

}
