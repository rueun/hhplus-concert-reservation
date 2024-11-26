
INSERT INTO users (id, name, email) VALUES (1, '홍길동1', 'hong1@email.com');
INSERT INTO users (id, name, email) VALUES (2, '홍길동2', 'hong2@email.com');
INSERT INTO users (id, name, email) VALUES (3, '홍길동3', 'hong3@email.com');
INSERT INTO users (id, name, email) VALUES (4, '홍길동4', 'hong4@email.com');
INSERT INTO users (id, name, email) VALUES (5, '홍길동5', 'hong5@email.com');
INSERT INTO users (id, name, email) VALUES (6, '홍길동6', 'hong6@email.com');
INSERT INTO users (id, name, email) VALUES (7, '홍길동7', 'hong7@email.com');
INSERT INTO users (id, name, email) VALUES (8, '홍길동8', 'hong8@email.com');
INSERT INTO users (id, name, email) VALUES (9, '홍길동9', 'hong9@email.com');
INSERT INTO users (id, name, email) VALUES (10, '홍길동10', 'hong10@email.com');


INSERT INTO user_point (id, user_id, amount) VALUES (1, 1, 100000);
INSERT INTO user_point (id, user_id, amount) VALUES (2, 2, 100000);
INSERT INTO user_point (id, user_id, amount) VALUES (3, 3, 100000);
INSERT INTO user_point (id, user_id, amount) VALUES (4, 4, 100000);
INSERT INTO user_point (id, user_id, amount) VALUES (5, 5, 100000);
INSERT INTO user_point (id, user_id, amount) VALUES (6, 6, 100000);
INSERT INTO user_point (id, user_id, amount) VALUES (7, 7, 100000);
INSERT INTO user_point (id, user_id, amount) VALUES (8, 8, 100000);
INSERT INTO user_point (id, user_id, amount) VALUES (9, 9, 100000);
INSERT INTO user_point (id, user_id, amount) VALUES (10, 10, 100000);


INSERT INTO concert (id, name, reservation_open_at, reservation_close_at) VALUES (1, '콘서트1', '2024-10-01 00:00:00', '2024-11-30 00:00:00');
INSERT INTO concert (id, name, reservation_open_at, reservation_close_at) VALUES (2, '콘서트2', '2024-10-01 00:00:00', '2024-11-30 00:00:00');
INSERT INTO concert (id, name, reservation_open_at, reservation_close_at) VALUES (3, '콘서트3', '2024-10-01 00:00:00', '2024-11-30 00:00:00');
INSERT INTO concert (id, name, reservation_open_at, reservation_close_at) VALUES (4, '콘서트4', '2024-11-01 00:00:00', '2024-11-30 00:00:00');
INSERT INTO concert (id, name, reservation_open_at, reservation_close_at) VALUES (5, '콘서트5', '2024-11-01 00:00:00', '2024-11-30 00:00:00');


INSERT INTO concert_session (id, concert_id, concert_at, total_seat_count) VALUES (1, 1, '2024-12-01 00:00:00', 100);
INSERT INTO concert_session (id, concert_id, concert_at, total_seat_count) VALUES (2, 1, '2024-12-01 00:00:00', 100);
INSERT INTO concert_session (id, concert_id, concert_at, total_seat_count) VALUES (3, 2, '2024-12-01 00:00:00', 400);
INSERT INTO concert_session (id, concert_id, concert_at, total_seat_count) VALUES (4, 2, '2024-12-01 00:00:00', 400);
INSERT INTO concert_session (id, concert_id, concert_at, total_seat_count) VALUES (5, 3, '2024-12-01 00:00:00', 600);
INSERT INTO concert_session (id, concert_id, concert_at, total_seat_count) VALUES (6, 3, '2024-12-01 00:00:00', 600);
INSERT INTO concert_session (id, concert_id, concert_at, total_seat_count) VALUES (7, 4, '2024-12-01 00:00:00', 100);
INSERT INTO concert_session (id, concert_id, concert_at, total_seat_count) VALUES (8, 5, '2024-12-01 00:00:00', 300);


INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (1, 1, 10000, 1, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (2, 1, 10000, 2, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (3, 1, 10000, 3, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (4, 1, 10000, 4, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (5, 1, 10000, 5, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (6, 1, 10000, 6, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (7, 1, 10000, 7, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (8, 1, 10000, 8, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (9, 1, 10000, 9, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (10, 1, 10000, 10, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (11, 2, 10000, 1, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (12, 2, 10000, 2, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (13, 2, 10000, 3, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (14, 2, 10000, 4, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (15, 2, 10000, 5, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (16, 2, 10000, 6, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (17, 2, 10000, 7, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (18, 2, 10000, 8, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (19, 2, 10000, 9, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (20, 2, 10000, 10, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (21, 3, 10000, 1, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (22, 3, 10000, 2, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (23, 3, 10000, 3, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (24, 3, 10000, 4, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (25, 3, 10000, 5, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (26, 4, 10000, 1, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (27, 4, 10000, 2, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (28, 4, 10000, 3, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (29, 4, 10000, 4, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (30, 4, 10000, 5, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (31, 5, 10000, 1, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (32, 5, 10000, 2, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (33, 5, 10000, 3, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (34, 5, 10000, 4, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (35, 5, 10000, 5, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (36, 6, 10000, 1, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (37, 6, 10000, 2, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (38, 6, 10000, 3, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (39, 6, 10000, 4, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (40, 6, 10000, 5, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (41, 7, 10000, 1, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (42, 7, 10000, 2, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (43, 7, 10000, 3, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (44, 7, 10000, 4, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (45, 7, 10000, 5, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (46, 8, 10000, 1, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (47, 8, 10000, 2, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (48, 8, 10000, 3, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (49, 8, 10000, 4, 'AVAILABLE', 0);
INSERT INTO concert_seat (id, concert_session_id, price, seat_number, status, version) VALUES (50, 8, 10000, 5, 'AVAILABLE', 0);

INSERT INTO outbox
(id, created_at, updated_at, eventType, eventKey, payload, status)
VALUES(16, '2024-11-21 19:21:47.576071', '2024-11-21 19:29:48.297713', 'ConcertReservedEvent', 'f571f19b-a6da-4452-8ae1-c152481cd329', '{"eventKey":"f571f19b-a6da-4452-8ae1-c152481cd329","reservationId":1}', 'INIT');
INSERT INTO outbox
(id, created_at, updated_at, eventType, eventKey, payload, status)
VALUES(17, '2024-11-21 19:29:47.576071', '2024-11-21 19:29:48.297713', 'ConcertReservedEvent', 'sdfrefgd-a6da-4452-8ae1-c152481cd329', '{"eventKey":"f571f19b-a6da-4452-8ae1-c152481cd329","reservationId":2}', 'PUBLISHED');