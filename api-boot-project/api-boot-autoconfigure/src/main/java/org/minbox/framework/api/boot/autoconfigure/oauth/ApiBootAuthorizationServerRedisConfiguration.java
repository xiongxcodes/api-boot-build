/*
 * Copyright [2019] [恒宇少年 - 于起宇]
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package org.minbox.framework.api.boot.autoconfigure.oauth;

import static org.minbox.framework.api.boot.autoconfigure.oauth.ApiBootOauthProperties.API_BOOT_OAUTH_PREFIX;

import java.util.List;

import javax.sql.DataSource;

import org.minbox.framework.oauth.AuthorizationServerConfiguration;
import org.minbox.framework.oauth.grant.OAuth2TokenGranter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * Redis authorization server
 *
 * @author 恒宇少年
 */
@EnableConfigurationProperties(ApiBootOauthProperties.class)
@ConditionalOnBean({RedisConnectionFactory.class,DataSource.class})
@ConditionalOnClass({AuthorizationServerConfiguration.class})
@ConditionalOnProperty(prefix = API_BOOT_OAUTH_PREFIX, name = "away", havingValue = "redis")
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class ApiBootAuthorizationServerRedisConfiguration extends ApiBootAuthorizationServerConfiguration {
    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(ApiBootAuthorizationServerRedisConfiguration.class);
    /**
     * redis connection factory
     */
    private RedisConnectionFactory redisConnectionFactory;
    private DataSource dataSource;
    /**
     * constructor instance redis connection factory
     *
     * @param objectProvider         ApiBoot Token Granter
     * @param apiBootOauthProperties ApiBoot Oauth Properties
     * @param redisConnectionFactory Redis Connection Factory
     * @param dataSource             data Source
     */
    public ApiBootAuthorizationServerRedisConfiguration(ObjectProvider<List<OAuth2TokenGranter>> objectProvider, ApiBootOauthProperties apiBootOauthProperties, RedisConnectionFactory redisConnectionFactory, DataSource dataSource) {
        super(objectProvider, apiBootOauthProperties);
        this.redisConnectionFactory = redisConnectionFactory;
        this.dataSource = dataSource;
        logger.info("ApiBoot Oauth2 initialize using redis.");
    }

    /**
     * configuration clients
     *
     * @param clients client details service configuration
     * @throws Exception exception
     */
    @SuppressWarnings("deprecation")
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /**
        InMemoryClientDetailsServiceBuilder inMemoryClientDetailsServiceBuilder = clients.inMemory();
        apiBootOauthProperties.getClients().stream().forEach(client -> inMemoryClientDetailsServiceBuilder.withClient(client.getClientId())
            .secret(passwordEncoder().encode(client.getClientSecret()))
            .authorizedGrantTypes(client.getGrantTypes())
            .scopes(client.getScopes())
            .resourceIds(client.getResourceId())
            .accessTokenValiditySeconds(client.getAccessTokenValiditySeconds())
            .refreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds()));**/
        clients.jdbc(dataSource);
    }

    /**
     * Configure Redis {@link TokenStore}
     *
     * @return {@link RedisTokenStore} instance
     */
    @Bean
    @Primary
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }
}
