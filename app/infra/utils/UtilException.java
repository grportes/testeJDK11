package infra.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import infra.exceptions.BusinessException;
import io.vavr.control.Try;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang.exception.ExceptionUtils.getRootCauseMessage;
import static play.libs.Json.newObject;

public final class UtilException {

    public static JsonNode toJson( final Throwable throwable ) {

        final ObjectNode erro = newObject();

        if ( throwable instanceof BusinessException )
            erro.put("msg", throwable.getMessage() );
        else
            erro.put("msg", getRootCauseMessage(throwable) );

        final Predicate<? super String> pacotes = stack ->
            !stack.contains( ".lambda$")
            && !stack.contains( "withTransaction")
            && !stack.contains( "infra.models.JPARepository")
            && (
                stack.startsWith("infra")
                || stack.startsWith("controllers")
                || stack.startsWith("services")
                || stack.startsWith("models")
                || stack.startsWith("views")
            )
        ;

        erro.put( "stack",
            Arrays.stream( ExceptionUtils.getRootCauseStackTrace(throwable) )
                .map(stack -> stack.replaceAll("\tat ", ""))
                .filter( pacotes )
                .collect( joining("  :  ") )
        );

        var cause = ExceptionUtils.getCause(throwable);
        if ( nonNull(cause) ) {
            if (cause instanceof ConstraintViolationException) {
                if (containsIgnoreCase(((ConstraintViolationException) cause).getConstraintName(), "unique constraint"))
                    erro.put("duplicateKey", true);
            }
        }

        return newObject().set("erro", erro);
    }

}
