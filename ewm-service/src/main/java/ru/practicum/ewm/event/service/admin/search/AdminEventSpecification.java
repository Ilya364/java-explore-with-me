package ru.practicum.ewm.event.service.admin.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AdminEventSpecification implements Specification<Event> {
    private final AdminEventSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getUsers() != null) {
            Expression<Long> expression = root.get("initiator").get("id");
            Predicate predicate = expression.in(criteria.getUsers());
            predicates.add(predicate);
        }
        if (criteria.getStates() != null) {
            Expression<Long> expression = root.get("state");
            Predicate predicate = expression.in(
                    criteria.getStates().stream().map(EventState::valueOf).collect(Collectors.toList())
            );
            predicates.add(predicate);
        }
        if (criteria.getCategories() != null) {
            Expression<Long> expression = root.get("category").get("id");
            Predicate predicate = expression.in(criteria.getCategories());
            predicates.add(predicate);
        }
        if (criteria.getRangeStart() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("eventDate"), criteria.getRangeStart()
            ));
        }
        if (criteria.getRangeEnd() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("eventDate"), criteria.getRangeEnd()
            ));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
