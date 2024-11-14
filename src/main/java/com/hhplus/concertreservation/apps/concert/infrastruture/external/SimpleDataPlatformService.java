package com.hhplus.concertreservation.apps.concert.infrastruture.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleDataPlatformService implements DataPlatformService {

    @Override
    public void sendReservationData(final Long reservationId) {
        // send reservation data to data platform
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.error("Failed to send reservation data to data platform", e);
        }
    }
}
