package com.hhplus.concertreservation.support.api.intercepter;

import com.hhplus.concertreservation.apps.queue.domain.exception.WaitingQueueErrorType;
import com.hhplus.concertreservation.apps.queue.domain.service.WaitingQueueService;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@DisplayName("대기열 토큰 인터셉터 테스트")
@ExtendWith(MockitoExtension.class)
class QueueTokenInterceptorTest {

    @InjectMocks
    private QueueTokenInterceptor queueTokenInterceptor;

    @Mock
    private WaitingQueueService waitingQueueService;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void 유효한_토큰이라면_에러가_발생하지_않는다() throws Exception {
        // given
        String validToken = "valid-token";
        request.addHeader("QUEUE-TOKEN", validToken);
        doNothing().when(waitingQueueService).checkActivatedQueue(validToken);

        // when & then
        assertDoesNotThrow(() ->
                queueTokenInterceptor.preHandle(request, response, new Object())
        );
    }

    @Test
    void 토큰이_없다면_에러가_발생한다() {
        // given
        request.addHeader("QUEUE-TOKEN", "");

        // when & then
        thenThrownBy(() ->
                queueTokenInterceptor.preHandle(request, response, new Object()))
                .isInstanceOf(CoreException.class)
                .hasMessage("대기열 토큰 정보를 찾을 수 없습니다")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(WaitingQueueErrorType.WAITING_QUEUE_HEADER_NOT_FOUND);
    }

    @Test
    void 토큰이_만료되었다면_에러가_발생한다() {
        // given
        String invalidToken = "invalid-token";
        request.addHeader("QUEUE-TOKEN", invalidToken);
        doThrow(new CoreException(WaitingQueueErrorType.WAITING_QUEUE_EXPIRED, "대기열 토큰이 만료되었습니다."))
                .when(waitingQueueService).checkActivatedQueue(invalidToken);

        // when & then
        thenThrownBy(() ->
                queueTokenInterceptor.preHandle(request, response, new Object()))
                .isInstanceOf(CoreException.class)
                .hasMessage("대기열 토큰이 만료되었습니다.")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(WaitingQueueErrorType.WAITING_QUEUE_EXPIRED);
    }
}