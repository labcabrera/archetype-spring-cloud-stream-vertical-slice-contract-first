package org.labcabrera.sample.archetype.shared.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

	@Bean
	public OpenAPI customOpenAPI(
		@Value("${springdoc.info.title:Application API}") String title,
		@Value("${springdoc.info.description:API description}") String description,
		@Value("${springdoc.info.version:0.0.0}") String version,
		@Value("${springdoc.info.contact.name:}") String contactName,
		@Value("${springdoc.info.contact.email:}") String contactEmail,
		@Value("${springdoc.oAuthFlow.authorizationUrl:}") String authorizationUrl,
		@Value("${springdoc.oAuthFlow.tokenUrl:}") String tokenUrl,
		@Value("#{'${springdoc.servers:}'.split(',')}") List<String> serverUrls) {

		Info info = new Info()
			.title(title)
			.description(description)
			.version(version);
		if (contactName != null && !contactName.isBlank()) {
			Contact contact = new Contact();
			contact.setName(contactName);
			if (contactEmail != null && !contactEmail.isBlank()) {
				contact.setEmail(contactEmail);
			}
			info.setContact(contact);
		}
		OpenAPI openAPI = new OpenAPI().info(info);

		// Add servers from configuration (comma separated list in property `springdoc.servers`)
		if (serverUrls != null) {
			for (String s : serverUrls) {
				if (s != null) {
					String url = s.trim();
					if (!url.isEmpty()) {
						openAPI.addServersItem(new Server().url(url));
					}
				}
			}
		}
		Components components = new Components();

		// Bearer JWT security scheme
		components.addSecuritySchemes("bearer-jwt", new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.description("Enter JWT bearer token as: Bearer <token>"));

		// OAuth2 with OIDC security scheme
		components.addSecuritySchemes(("oidc"), new SecurityScheme()
			.type(SecurityScheme.Type.OAUTH2)
			.description("OAuth2 con OIDC contra IAM")
			.flows(new OAuthFlows()
				.authorizationCode(new OAuthFlow()
					.authorizationUrl(authorizationUrl)
					.tokenUrl(tokenUrl)
					.scopes(new Scopes()
						.addString("openid", "OpenID scope")
						.addString("profile", "User profile scope")
						.addString("api", "API access scope")))));

		openAPI.addSecurityItem(new SecurityRequirement().addList("oidc"));
		openAPI.components(components);
		return openAPI;
	}
}