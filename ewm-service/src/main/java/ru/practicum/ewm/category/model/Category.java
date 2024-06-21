package ru.practicum.ewm.category.model;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
}
