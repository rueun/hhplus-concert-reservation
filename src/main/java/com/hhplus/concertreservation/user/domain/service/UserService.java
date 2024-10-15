package com.hhplus.concertreservation.user.domain.service;

import com.hhplus.concertreservation.user.domain.exception.UserNotFoundException;
import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.user.domain.repository.UserReader;
import com.hhplus.concertreservation.user.domain.repository.UserWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserReader userReader;
    private final UserWriter userWriter;

    public void checkUserExist(final Long userId) {
        if (!userReader.existsById(userId)) {
            throw new UserNotFoundException();
        }
    }
}