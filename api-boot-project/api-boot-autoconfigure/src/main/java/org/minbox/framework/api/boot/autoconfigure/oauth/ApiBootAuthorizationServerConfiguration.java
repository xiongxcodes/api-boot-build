 package org.minbox.framework.api.boot.autoconfigure.oauth;

import static org.minbox.framework.api.boot.autoconfigure.oauth.ApiBootOauthProperties.API_BOOT_OAUTH_PREFIX;

import java.util.List;

import org.minbox.framework.oauth.AuthorizationServerConfiguration;
import org.minbox.framework.oauth.grant.OAuth2TokenGranter;
import org.minbox.framework.oauth.response.AuthorizationDeniedResponse;
import org.minbox.framework.oauth.response.DefaultAuthorizationDeniedResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@SuppressWarnings("deprecation")
public class ApiBootAuthorizationServerConfiguration  extends AuthorizationServerConfiguration {

     protected ApiBootOauthProperties apiBootOauthProperties;

     public ApiBootAuthorizationServerConfiguration(ObjectProvider<List<OAuth2TokenGranter>> objectProvider, ApiBootOauthProperties apiBootOauthProperties) {
         super(objectProvider);
         this.apiBootOauthProperties = apiBootOauthProperties;
     }

     /**
      * Configure jwt {@link AccessTokenConverter}
      * <p>
      * If the value of the configuration "api.boot.oauth.jwt.enable" is "true"
      * Use {@link JwtAccessTokenConverter}
      *
      * @return {@link JwtAccessTokenConverter} instance
      */
     @Bean
     @ConditionalOnProperty(prefix = API_BOOT_OAUTH_PREFIX, name = "jwt.enable", havingValue = "true")
     public AccessTokenConverter jwtAccessTokenConverter() {
         JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
         converter.setSigningKey(apiBootOauthProperties.getJwt().getSignKey());
         return converter;
     }

     /**
      * Configure default {@link AccessTokenConverter}
      * <p>
      * If the value of the configuration "api.boot.oauth.jwt.enable" is "false" or missing
      * Use {@link DefaultAccessTokenConverter}
      *
      * @return {@link DefaultAccessTokenConverter} instance
      */
     @Bean
     @ConditionalOnProperty(prefix = API_BOOT_OAUTH_PREFIX, name = "jwt.enable", havingValue = "false", matchIfMissing = true)
     public AccessTokenConverter defaultAccessTokenConverter() {
         return new DefaultAccessTokenConverter();
     }

     /**
      * Configure custom serialization authentication error format
      *
      * @return The {@link DefaultAuthorizationDeniedResponse} instance
      * @see DefaultAuthorizationDeniedResponse
      */
     @Bean
     @ConditionalOnMissingBean
     public AuthorizationDeniedResponse authorizationDeniedResponse() {
         return new DefaultAuthorizationDeniedResponse();
     }
     /**
      * Configure secret encryption in the same way as ApiBoot Security
      *
      * @param security AuthorizationServerSecurityConfigurer
      * @throws Exception 异常信息
      */
     @Override
     public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
         /**
         security
                 //.passwordEncoder(passwordEncoder())
         .passwordEncoder(new PasswordEncoder() {

            @Override
            public String encode(CharSequence rawPassword) {
                // TODO Auto-generated method stub
                 return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                // TODO Auto-generated method stub
                 return true;
            }
             
         })
                 // Configure open/oauth/token_key access address
                 .tokenKeyAccess("permitAll()")
                 // Configure Open /oauth/check_token Access Address
                 // Access must be accessible after login privileges
                 .checkTokenAccess("isAuthenticated()")
                 .allowFormAuthenticationForClients();**/
         //security.allowFormAuthenticationForClients().tokenKeyAccess("permitAll()")
         //.checkTokenAccess("isAuthenticated()");
         super.configure(security);
     }

     /**
      * Configure {@link WebResponseExceptionTranslator}
      *
      * @param authorizationDeniedResponse The {@link AuthorizationDeniedResponse} instance
      * @return {@link WebResponseExceptionTranslator} instance
      * @see AuthorizationServerConfiguration#configure(org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer)
      * @see DefaultWebResponseExceptionTranslator
      */
     /**
     @Bean
     @ConditionalOnBean(AuthorizationDeniedResponse.class)
     @ConditionalOnMissingBean
     public WebResponseExceptionTranslator webResponseExceptionTranslator(AuthorizationDeniedResponse authorizationDeniedResponse) {
         return new DefaultWebResponseExceptionTranslator(authorizationDeniedResponse);
     }**/
 }

