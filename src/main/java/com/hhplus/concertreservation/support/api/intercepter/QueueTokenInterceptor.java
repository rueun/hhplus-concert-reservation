package com.hhplus.concertreservation.support.api.intercepter;

import com.hhplus.concertreservation.queue.domain.exception.WaitingQueueErrorType;
import com.hhplus.concertreservation.queue.domain.service.WaitingQueueService;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class QueueTokenInterceptor implements HandlerInterceptor {

    private final WaitingQueueService waitingQueueService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String token = request.getHeader("QUEUE-TOKEN");
        if (Objects.isNull(token) || token.isEmpty()) {
            throw new CoreException(WaitingQueueErrorType.WAITING_QUEUE_HEADER_NOT_FOUND, "대기열 토큰 정보를 찾을 수 없습니다");
        }

        waitingQueueService.checkActivatedQueue(token);
        return true;
    }
}