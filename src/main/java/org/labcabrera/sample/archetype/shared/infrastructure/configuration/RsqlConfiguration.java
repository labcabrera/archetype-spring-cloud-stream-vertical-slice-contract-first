package org.labcabrera.sample.archetype.shared.infrastructure.configuration;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

@Configuration
public class RsqlConfiguration {

    @Bean
    public RSQLParser rsqlParserBuilder() {
        Set<ComparisonOperator> operators = RSQLOperators.defaultOperators();
        operators.add(new ComparisonOperator("=re=", false));
        return new RSQLParser(operators);
    }

}
