package com.hhplus.concertreservation.apps.concert.infrastruture.repository;

import com.hhplus.concertreservation.apps.concert.domain.exception.ConcertErrorType;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.Concert;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSession;
import com.hhplus.concertreservation.apps.concert.domain.repository.ConcertReader;
import com.hhplus.concertreservation.apps.concert.infrastruture.entity.ConcertEntity;
import com.hhplus.concertreservation.apps.concert.infrastruture.entity.ConcertReservationEntity;
import com.hhplus.concertreservation.apps.concert.infrastruture.entity.ConcertSeatEntity;
import com.hhplus.concertreservation.apps.concert.infrastruture.entity.ConcertSessionEntity;
import com.hhplus.concertreservation.common.time.TimeProvider;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertJpaReader implements ConcertReader {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertSessionJpaRepository concertSessionJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;
    private final ConcertReservationJpaRepository concertReservationJpaRepository;

    private final TimeProvider timeProvider;

    @Override
    public Concert getConcertById(final Long concertId) {
        return concertJpaRepository.findById(concertId)
                .map(ConcertEntity::toDomain)
                .orElseThrow(() -> new CoreException(ConcertErrorType.CONCERT_NOT_FOUND));
    }

    @Override
    public ConcertSession getConcertSessionById(final Long concertSessionId) {
        return concertSessionJpaRepository.findById(concertSessionId)
                .map(ConcertSessionEntity::toDomain)
                .orElseThrow(() -> new CoreException(ConcertErrorType.CONCERT_SESSION_NOT_FOUND));
    }

    @Override
    public ConcertSeat getConcertSeatById(final Long concertSeatId) {
        return concertSeatJpaRepository.findById(concertSeatId)
                .map(ConcertSeatEntity::toDomain)
                .orElseThrow(() -> new CoreException(ConcertErrorType.CONCERT_SEAT_NOT_FOUND));
    }

    @Override
    public ConcertReservation getConcertReservationById(final Long concertReservationId) {
        return concertReservationJpaRepository.findById(concertReservationId)
                .map(ConcertReservationEntity::toDomain)
                .orElseThrow(() -> new CoreException(ConcertErrorType.CONCERT_RESERVATION_NOT_FOUND));
    }

    @Override
    public List<ConcertSeat> getConcertSeatsByIds(final List<Long> seatIds) {
        return concertSeatJpaRepository.findAllById(seatIds).stream()
                .map(ConcertSeatEntity::toDomain)
                .toList();
    }

    @Override
    public List<ConcertSession> getConcertSessionsByConcertId(final Long concertId) {
        return concertSessionJpaRepository.findAllByConcertId(concertId).stream()
                .map(ConcertSessionEntity::toDomain)
                .toList();
    }

    @Override
    public List<ConcertSeat> getConcertSeatsBySessionId(final Long concertSessionId) {
        return concertSeatJpaRepository.findAllByConcertSessionId(concertSessionId).stream()
                .map(ConcertSeatEntity::toDomain)
                .toList();
    }

    @Override
    public List<ConcertReservation> getTemporaryReservationsToBeExpired(final int minutes) {

        final LocalDateTime expirationTime = timeProvider.now().minusMinutes(minutes);

        return concertReservationJpaRepository.findTemporaryReservationsToBeExpired(expirationTime).stream()
                .map(ConcertReservationEntity::toDomain)
                .toList();
    }
}
