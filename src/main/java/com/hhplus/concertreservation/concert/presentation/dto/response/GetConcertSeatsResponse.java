package com.hhplus.concertreservation.concert.presentation.dto.response;

import com.hhplus.concertreservation.concert.domain.model.dto.ConcertSeatsInfo;

import java.util.List;

public record GetConcertSeatsResponse(
        int totalSeatCount,
        List<ConcertSeatResponse> unavailableSeats,
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
