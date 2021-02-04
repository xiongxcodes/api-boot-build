/*
 * Copyright [2019] [恒宇少年 - 于起宇]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.minbox.framework.api.boot.autoconfigure.security;

import static org.minbox.framework.api.boot.autoconfigure.security.ApiBootSecurityProperties.API_BOOT_SECURITY_PREFIX;

import org.minbox.framework.api.boot.autoconfigure.oauth.ApiBootAuthorizationServerRedisConfiguration;
import org.minbox.framework.security.WebSecurityConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import de.codecentric.boot.admin.server.config.AdminServerProperties;

/**
 * ApiBoot integrates SpringSecurity's default automation configuration
 *
 * @author 恒宇少年
 * @see ApiBootWebSecurityMemoryConfiguration
 * @see ApiBootWebSecurityJdbcConfiguration
 */
@Configuration
@ConditionalOnProperty(prefix = API_BOOT_SECURITY_PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties({ApiBootSecurityProperties.class,AdminServerProperties.class})
@ConditionalOnClass(WebSecurityConfiguration.class)
@EnableWebSecurity
@Import({ApiBootWebSecurityMemoryConfiguration.class,ApiBootWebSecurityJdbcConfiguration.class,ApiBootAuthorizationServerRedisConfiguration.class})
public class ApiBootWebSecurityAutoConfiguration  {

}
