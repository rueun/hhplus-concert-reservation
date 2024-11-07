package com.hhplus.concertreservation.apps.concert.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConcertSessions {
    private List<ConcertSession> sessions = new ArrayList<>();
}
