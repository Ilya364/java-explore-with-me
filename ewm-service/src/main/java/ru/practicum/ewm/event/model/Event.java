package ru.practicum.ewm.event.model;

import lombok.*;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@ToString
@Getter
@Setter
@Builder
@Entity
@Table(name = "events")
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private User initiator;
    @Embedded
    @AttributeOverride(name = "lat", column = @Column(name = "location_lat"))
    @AttributeOverride(name = "lon", column = @Column(name = "location_lon"))
    private Location location;
    private Boolean paid;
    @Column(name = "participants_limit")
    private Integer participantsLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    @Enumerated(EnumType.STRING)
    private EventState state;
    private String title;
    private Long views;
    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations;

    public void addCompilation(Compilation compilation) {
        compilations.add(compilation);
    }
}
