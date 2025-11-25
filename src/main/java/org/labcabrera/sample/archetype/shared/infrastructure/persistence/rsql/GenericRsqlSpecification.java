package org.labcabrera.sample.archetype.shared.infrastructure.persistence.rsql;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
@SuppressWarnings("null")
public class GenericRsqlSpecification<T> implements Specification<T> {

    private transient String property;
    private transient ComparisonOperator operator;
    private transient List<String> arguments;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path<?> path = getPath(root, property);
        List<Object> args = castArguments(path);
        Object argument = args.get(0);
        switch (RsqlSearchOperation.getSimpleOperator(operator)) {

        case EQUAL: {
            if (argument instanceof String) {
                return builder.equal(path, argument);
            }
            else if (argument == null) {
                return builder.isNull(path);
            }
            break;
        }
        case NOT_EQUAL: {
            if (argument instanceof String) {
                return builder.notEqual(path, argument);
            }
            else if (argument == null) {
                return builder.isNotNull(path);
            }
            break;
        }
        case GREATER_THAN: {
            return builder.greaterThan(path.as(String.class), argument.toString());
        }
        case GREATER_THAN_OR_EQUAL: {
            return builder.greaterThanOrEqualTo(path.as(String.class), argument.toString());
        }
        case LESS_THAN: {
            return builder.lessThan(path.as(String.class), argument.toString());
        }
        case LESS_THAN_OR_EQUAL: {
            return builder.lessThanOrEqualTo(path.as(String.class), argument.toString());
        }
        case IN:
            return path.in(args);
        case NOT_IN:
            return builder.not(path.in(args));
        case LIKE:
            var str = argument.toString();
            var value = str.indexOf('%') < 0 ? String.format("%%%s%%", str) : str;
            return builder.like(path.as(String.class), value);
        }

        return null;
    }

    private List<Object> castArguments(final Path<?> path) {
        Class<? extends Object> type = path.getJavaType();
        return arguments.stream().map(arg -> {
            if (type.equals(Integer.class)) {
                return Integer.parseInt(arg);
            }
            else if (type.equals(Long.class)) {
                return Long.parseLong(arg);
            }
            else {
                return arg;
            }
        }).collect(Collectors.toList());
    }

    private Path<?> getPath(Root<T> root, String property) {
        if (!property.contains(".")) {
            return root.get(property);
        }
        String[] parts = property.split("\\.");
        Path<?> path = root.get(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            path = path.get(parts[i]);
        }
        return path;
    }

}
