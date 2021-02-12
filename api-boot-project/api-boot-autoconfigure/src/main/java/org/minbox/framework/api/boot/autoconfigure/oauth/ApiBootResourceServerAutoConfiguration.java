/*
 * Copyright [2019] [恒宇少年 - 于起宇]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.minbox.framework.api.boot.autoconfigure.oauth;

import static org.minbox.framework.api.boot.autoconfigure.oauth.ApiBootOauthProperties.API_BOOT_OAUTH_PREFIX;

import org.minbox.framework.api.boot.autoconfigure.security.ApiBootSecurityProperties;
import org.minbox.framework.security.SecurityUser;
import org.minbox.framework.security.handler.DefaultSecurityAccessDeniedHandler;
import org.minbox.framework.security.point.DefaultSecurityAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.ObjectUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * Resource server configuration
 *
 * @author 恒宇少年
 */
@SuppressWarnings("deprecation")
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = API_BOOT_OAUTH_PREFIX, name = "enable-resource", havingValue = "true")
@ConditionalOnClass({ResourceServerConfigurerAdapter.class, SecurityUser.class})
@EnableConfigurationProperties({ApiBootSecurityProperties.class, ApiBootOauthProperties.class})
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Import({ApiBootResourceFeignConfiguration.class})
public class ApiBootResourceServerAutoConfiguration extends ResourceServerConfigurerAdapter {
    //private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private ApiBootSecurityProperties apiBootSecurityProperties;
    private ApiBootOauthProperties apiBootOauthProperties;
    private AccessDeniedHandler accessDeniedHandler;
    private AuthenticationEntryPoint authenticationEntryPoint;
    public ApiBootResourceServerAutoConfiguration(ApiBootSecurityProperties apiBootSecurityProperties, ApiBootOauthProperties apiBootOauthProperties,@Autowired(required = false) AccessDeniedHandler accessDeniedHandler,@Autowired(required = false) AuthenticationEntryPoint authenticationEntryPoint) {
        this.apiBootSecurityProperties = apiBootSecurityProperties;
        this.apiBootOauthProperties = apiBootOauthProperties;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }
    @Bean
    @ConditionalOnProperty(prefix = API_BOOT_OAUTH_PREFIX, name = "jwt.enable", havingValue = "true")
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(apiBootOauthProperties.getJwt().getSignKey());
        return converter;
    }
    @Bean
    @ConditionalOnProperty(prefix = API_BOOT_OAUTH_PREFIX, name = "jwt.enable", havingValue = "true")
    public TokenStore jwtTokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }
    /**
     * Configure resource server auth prefix
     *
     * @param http {@link HttpSecurity}
     * @throws Exception The exception instance
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers( "/actuator/**","/instances/**").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .requestMatchers()
            .antMatchers(apiBootSecurityProperties.getAuthPrefix())
            //跨域
            .and()
            .cors()
            .and()
            .csrf()
            .disable();
    }
    
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
    protected AccessDeniedHandler getAccessDeniedHandler() {
        return ObjectUtils.isEmpty(this.accessDeniedHandler) ? new DefaultSecurityAccessDeniedHandler()
            : this.accessDeniedHandler;
    }

    protected AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return ObjectUtils.isEmpty(this.authenticationEntryPoint) ? new DefaultSecurityAuthenticationEntryPoint()
            : this.authenticationEntryPoint;
    }

    protected boolean disableHttpBasic() {
        return apiBootSecurityProperties.isDisableHttpBasic();
    }

    protected boolean disableCsrf() {
        return apiBootSecurityProperties.isDisableCsrf();
    }
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(apiBootOauthProperties.getResourceId());
        resources.authenticationEntryPoint(getAuthenticationEntryPoint());
        resources.accessDeniedHandler(getAccessDeniedHandler());
    }
}
