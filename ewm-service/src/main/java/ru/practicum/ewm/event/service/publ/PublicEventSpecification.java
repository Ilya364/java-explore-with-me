package ru.practicum.ewm.event.service.publ;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PublicEventSpecification implements Specification<Event> {
    private final PublicEventSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getText() != null) {
            Predicate annotationPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("annotation")), "%" + criteria.getText().toLowerCase() + "%"
            );
            Predicate descriptionPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")), "%" + criteria.getText().toLowerCase() + "%"
            );
            predicates.add(
                    criteriaBuilder.or(annotationPredicate, descriptionPredicate)
            );
        }
        if (criteria.getCategories() != null) {
            Expression<Long> expression = root.get("category").get("id");
            Predicate predicate = expression.in(criteria.getCategories());
            predicates.add(predicate);
        }
        if (criteria.getRangeStart() != null && criteria.getRangeEnd() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("eventDate"), criteria.getRangeStart()
            ));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("eventDate"), criteria.getRangeEnd()
            ));
        } else {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("eventDate"), LocalDateTime.now()
            ));
        }
        if (criteria.getOnlyAvailable()) {
            predicates.add(criteriaBuilder.lessThan(
                    root.get("confirmedRequests"), root.get("participantsLimit")
            ));
        }
        if (criteria.getSort() != null) {
            if (criteria.getSort().equals("EVENT_DATE")) {
                criteriaBuilder.asc(root.get("eventDate"));
            } else if (criteria.getSort().equals("VIEWS")) {
                criteriaBuilder.asc(root.get("views"));
            }
        } else {
            criteriaBuilder.asc(root.get("views"));
        }
        predicates.add(criteriaBuilder.equal(root.get("state"), EventState.PUBLISHED));
        if (criteria.getPaid() != null) {
            predicates.add(criteriaBuilder.equal(root.get("paid"), criteria.getPaid()));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
