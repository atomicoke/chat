package io.github.fzdwx.ws;

import io.github.fzdwx.ws.config.Ws;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 15:15
 */
@Ws("/echo")
public class EchoWs implements WebSocketHandler {

    @Override
    public Mono<Void> handle(final WebSocketSession session) {
        Flux<WebSocketMessage> output = session.receive()
                .map(value -> session.textMessage("Echo " + value));
        return session.send(output);
    }
}