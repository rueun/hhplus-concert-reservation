package com.hhplus.concertreservation.apps.concert.presentation.dto.response;

import com.hhplus.concertreservation.apps.concert.domain.model.dto.ConcertSeatsInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetConcertSeatsResponse(
        @Schema(name = "전체 좌석 수")
        int totalSeatCount,
        @Schema(name = "예약 불가능한 좌석 목록")
        List<ConcertSeatResponse> unavailableSeats,
        @Schema(name = "예약 가능한 좌석 목록")
        List<ConcertSeatResponse> availableSeats
) {

    public static GetConcertSeatsResponse of(final ConcertSeatsInfo concertSeatsInfo) {
        return new GetConcertSeatsResponse(
                concertSeatsInfo.getConcertSession().getTotalSeatCount(),
                concertSeatsInfo.getUnavailableSeats().stream()
                        .map(ConcertSeatResponse::of)
                        .toList(),
                concertSeatsInfo.getAvailableSeats().stream()
                        .map(ConcertSeatResponse::of)
                        .toList()
        );
    }
}
