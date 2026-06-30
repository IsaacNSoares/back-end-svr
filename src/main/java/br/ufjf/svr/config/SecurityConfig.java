package br.ufjf.svr.config;

import br.ufjf.svr.security.JwtAuthFilter;
import br.ufjf.svr.security.JwtService;
import br.ufjf.svr.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder; // Injetado automaticamente do PasswordConfig

    @Bean
    public JwtAuthFilter jwtFilter() {
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Passando o usuarioService diretamente no construtor da classe!
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/carrinhos/**").authenticated()
                        .requestMatchers("/api/v1/clientes/**").permitAll()
                        .requestMatchers("/api/v1/colaboradores/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/v1/cores-produto/**").permitAll()
                        .requestMatchers("/api/v1/enderecos/**").authenticated()
                        .requestMatchers("/api/v1/fornecedores/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/v1/itens-carrinho/**").authenticated()
                        .requestMatchers("/api/v1/itens-pedido/**").authenticated()
                        .requestMatchers("/api/v1/lojas/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/v1/metodos-pagamento/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/v1/pedidos/**").authenticated()
                        .requestMatchers("/api/v1/produtos/**").permitAll()
                        .requestMatchers("/api/v1/produtos-loja/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/v1/tamanhos-produto/**").permitAll()
                        .requestMatchers("/api/v1/usuarios/**").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/v2/api-docs",
                "/v3/api-docs/**",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/webjars/**"
        );
    }
}