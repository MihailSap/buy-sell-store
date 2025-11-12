package ru.project.buySellStore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <b>Конфигурационный класс для Spring Security.</b>
 * <p>
 *     Отвечает за настройку Spring Security: за определение доступных и защищенных маршрутов
 *     и за создание бинов, необходимых для корректной аутентификации и шифрования паролей.
 * </p>
 * @author SapeginMihail
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    /**
     * <b>Настройка фильтров безопасности.</b>
     * <ul>
     *     <li>Отключение защиты CSRF</li>
     *     <li>Открытие доступа к точкам входа /api/auth/login и /api/auth/register</li>
     *     <li>Закрытие доступа к остальным точкам входа</li>
     * </ul>
     * @param http объект для настройки правил безопасности для HTTP запросов
     * @return конфигурация, определяющая правила безопасности
     * @throws Exception ошибка, возникающая при настройке фильтров безопасности
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }

    /**
     * Создание бина для шифрования паролей пользователей.
     * @return экземпляр {@link PasswordEncoder} для шифрования.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Создание бина для работы с аутентификацией
     * @param configuration объект с настройками аутентификации
     * @return экземпляр {@link AuthenticationManager}, полученный из конфигурации
     * @throws Exception ошибка при получении {@link AuthenticationManager} из конфигурации
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
