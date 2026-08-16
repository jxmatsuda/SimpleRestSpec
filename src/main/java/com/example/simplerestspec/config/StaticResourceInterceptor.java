package com.example.simplerestspec.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class StaticResourceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        
        log.info("Checking Authorization header for static resource access: {}", request.getRequestURI());

        if (isValidAuthorization(authHeader)) {
            return true; // チェックOK、リクエストを続行
        }

        log.warn("Unauthorized access attempt to static resource: {}", request.getRequestURI());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Missing or invalid Authorization header");
        return false; // チェックNG、リクエストを中断
    }

    /**
     * Authorizationヘッダの検証ロジックを実装するメソッドの骨組み
     */
    private boolean isValidAuthorization(String authHeader) {
        // TODO: ここに実際の認証チェックロジックを実装してください
        // 例: トークンの形式チェック、有効期限の確認、DB照合など
        
        if (authHeader == null || authHeader.isEmpty()) {
            // 現時点では authorization がない場合でも true を返す。
            return true;
        }

        // 現時点では、ヘッダが存在すれば true を返す骨組みとして実装
        return true; 
    }
}