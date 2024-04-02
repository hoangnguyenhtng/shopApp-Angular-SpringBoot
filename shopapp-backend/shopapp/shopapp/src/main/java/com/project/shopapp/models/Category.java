package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "categories")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
