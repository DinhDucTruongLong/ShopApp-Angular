package com.project.shopapp.filter;

import com.project.shopapp.components.JwtTokenUtil;
import com.project.shopapp.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    // ĐÃ SỬA: Cần sử dụng cú pháp $ và đổi tên biến cấu hình cho chuẩn
    @Value("${api.prefix}")
    private String apiPrefix;

    // ĐÃ SỬA: Cần có FINAL để @RequiredArgsConstructor tiêm (inject)
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1. Kiểm tra điều kiện bỏ qua Token (Bypass)
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");

            // 2. Chỉ xử lý nếu header có tồn tại và đúng định dạng
            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                final String token = authHeader.substring(7); // Loại bỏ 7 ký tự "Bearer "
                final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);

                // 3. Nếu trích xuất được phone number VÀ chưa có Authentication trong Context
                if (phoneNumber != null
                        && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // Nếu userDetailsService là NULL, lỗi sẽ xảy ra ở đây
                    User userDetails =(User) userDetailsService.loadUserByUsername(phoneNumber);

                    // 4. Validate Token
                    if (jwtTokenUtil.validateToken(token, userDetails)) {
                        // Token hợp lệ, thiết lập Authentication
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }

            // 5. Cho yêu cầu tiếp tục chuỗi Filter (QUAN TRỌNG: Chỉ gọi MỘT LẦN)
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // Lỗi NullPointerException (do thiếu injection) sẽ bị bắt ở đây và trả về 401
            // System.err.println("Authentication error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid token or access denied.");
        }
    }

    private boolean isBypassToken(@NotNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/products", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories", apiPrefix), "GET"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"), // Sửa endpoint
                Pair.of(String.format("%s/users/login", apiPrefix), "POST")    // Sửa endpoint
        );
        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        for (Pair<String, String> bypassToken : bypassTokens) {
            if (requestPath.contains(bypassToken.getLeft()) &&
                    requestMethod.equals(bypassToken.getRight())) {
                return true;
            }
        }
        return false;
    }
}