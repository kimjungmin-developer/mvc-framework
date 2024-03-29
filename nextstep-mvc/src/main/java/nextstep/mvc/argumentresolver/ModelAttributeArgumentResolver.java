package nextstep.mvc.argumentresolver;

import nextstep.mvc.argumentresolver.support.ServletArgumentConverter;
import nextstep.mvc.argumentresolver.support.TypeParser;
import nextstep.mvc.exception.InstantiationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;

public class ModelAttributeArgumentResolver implements ArgumentResolver {
    @Override
    public boolean canResolve(MethodParameter methodParameter) {
        Class<?> type = methodParameter.getType();
        return methodParameter.hasNoDeclaredAnnotation() &&
                !TypeParser.canParse(type) &&
                !ServletArgumentConverter.supports(type);
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, MethodParameter methodParameter) {
        Class<?> paramType = methodParameter.getType();
        Field[] fields = paramType.getDeclaredFields();
        try {
            Object instance = paramType.getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                field.set(instance, TypeParser.parse(request.getParameter(field.getName()), fieldType));
            }
            return instance;
        } catch (Exception e) {
            throw new InstantiationException();
        }
    }
}