package com.buzzybees.master.config;

import com.buzzybees.master.beehives.Beehive;
import com.buzzybees.master.beehives.BeehiveRepository;
import com.buzzybees.master.controllers.AuthController;
import com.buzzybees.master.controllers.template.DatabaseController;
import com.buzzybees.master.users.UserService;
import com.buzzybees.master.websockets.ClientSocketHandler;
import com.buzzybees.master.websockets.EspSocketHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ClientSocketHandler(), "/websocket/connect").addInterceptors(new ClientHandshakeInterceptor());
        registry.addHandler(new EspSocketHandler(), "/websocket/beehive").addInterceptors(new EspHandshakeInterceptor()).setAllowedOrigins("*");
    }

    public static class ClientHandshakeInterceptor implements HandshakeInterceptor {

        @Override
        public boolean beforeHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, @NotNull Map<String, Object> attributes) throws Exception {

            List<String> cookieHeaders = request.getHeaders().get(HttpHeaders.COOKIE);
            String ssid = getSSID(cookieHeaders);

            if(ssid != null) {
                long userId = UserService.getUserIdByToken(ssid);
                attributes.put("userID", userId);
                return true;
            }

            return false;
        }

        private String getSSID(List<String> cookieHeaders) {
            if(cookieHeaders != null) {
                String[] cookies = cookieHeaders.get(0).split(";");
                for(String cookieString : cookies) {
                    List<HttpCookie> cookie = HttpCookie.parse(cookieString);
                    if(cookie.get(0).getName().equals(AuthController.SSID)) return cookie.get(0).getValue();
                }
            }
            return null;
        }

        @Override
        public void afterHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, Exception exception) {

        }
    }


    public static class EspHandshakeInterceptor implements HandshakeInterceptor {

        @Override
        public boolean beforeHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, @NotNull Map<String, Object> attributes) throws Exception {
            System.out.println(request.getHeaders());
            request.getHeaders().set("Upgrade", "websocket");
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String token = servletRequest.getServletRequest().getParameter("token");

            BeehiveRepository beehiveRepository = DatabaseController.accessRepo(Beehive.class);
            Beehive beehive = beehiveRepository.getBeehiveByToken(token);
            attributes.put("beehive", token);

            return beehive != null;
        }

        @Override
        public void afterHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, Exception exception) {

        }
    }

}