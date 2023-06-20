package com.cutegyuseok.freetalk.global.config;


import com.cutegyuseok.freetalk.global.jwt.JwtExceptionFilter;
import com.cutegyuseok.freetalk.global.jwt.JwtFilter;
import com.cutegyuseok.freetalk.global.jwt.JwtProperties;
import com.cutegyuseok.freetalk.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {


    private final JwtFilter jwtFilter;
    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;


    private static final String[] PUBLIC_URLS = { //이 URL은 권한 검사안함
            "/auth/**",
            "/categories/**",
            "/posting/**",
            "/community/*",

            /* swagger v3 */
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };
    private static final String[] ADMIN_URLS = { //이 URL은 ADMIN 권한만 허용
            "/admin/**"
    };


    @Bean
    public PasswordEncoder passwordEncoderParser() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .cors()
                .and()
                .authorizeRequests()
                .mvcMatchers(PUBLIC_URLS).permitAll()
                .and()
                .authorizeRequests()
                .mvcMatchers(ADMIN_URLS).hasAnyRole("SUPER", "WRITE")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(JwtExceptionFilter.of(jwtProvider, jwtProperties), UsernamePasswordAuthenticationFilter.class)
                .build()
                ;


    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { //시큐리티 filter 제외, 그러나 OncePerRequestFilter는 시큐리티 필터가 아니라서 로직실행
        return (web) -> web.ignoring().mvcMatchers(PUBLIC_URLS);
    }

    //Cors 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private AccessDeniedHandler accessDeniedHandler() {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        return accessDeniedHandler;
    }

}