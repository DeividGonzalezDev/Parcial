package com.example.keycloakadmin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;


@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

  private final JwtAuthConverterProperties properties;

  @Autowired
  public JwtAuthConverter(JwtAuthConverterProperties properties) {
    this.properties = properties;
  }

  /**
   * Retrieves the principal claim name from the provided JWT.
   *
   * @param  jwt  the JWT from which to extract the principal claim name
   * @return      the principal claim name, or the default claim name if not specified in the properties
   */
  private String getPrincipalClaimName(Jwt jwt) {
    String claimName = JwtClaimNames.SUB;
    if (properties.getPrincipalAttribute() != null) {
      claimName = properties.getPrincipalAttribute();
    }
    return jwt.getClaim(claimName);
  }

  /**
   * Extracts the resource roles from the provided JWT.
   *
   * @param  jwt  the JWT from which to extract the resource roles
   * @return      a collection of granted authorities representing the resource roles
   */
  private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
    try {
      Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
      Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(properties.getResourceId());
      Collection<String> resourceRoles = (Collection<String>) resource.get("roles");

      List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();

      resourceRoles.forEach(
          role -> grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role)));

      return grantedAuthorities;

    } catch (Exception e) {

      e.printStackTrace();
      return Collections.emptyList();
      
    }
  }

  /**
   * Converts a JWT into a JwtAuthenticationToken, which contains the JWT itself,
   * a set of granted authorities, and the principal claim name. The granted authorities
   * are obtained by combining the authorities from the JwtGrantedAuthoritiesConverter
   * and the resource roles extracted from the JWT.
   *
   * @param  jwt  the JWT to convert
   * @return      the JwtAuthenticationToken representing the converted JWT
   */
  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {

    Collection<GrantedAuthority> authorities = Stream.concat(
        jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
        extractResourceRoles(jwt).stream()).collect(Collectors.toSet());

    return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
  }

}
