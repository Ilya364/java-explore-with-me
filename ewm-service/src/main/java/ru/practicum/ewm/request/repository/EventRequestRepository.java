package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.model.EventRequest;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Optional;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    List<EventRequest> findAllByEventId(Long eventId);

    List<EventRequest> findAllByRequesterId(Long requesterId);

    Optional<EventRequest> findByEventAndRequester(Event event, User requester);
}
