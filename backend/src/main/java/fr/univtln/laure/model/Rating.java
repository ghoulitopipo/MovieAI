package fr.univtln.laure.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import fr.univtln.laure.service.DateConverter;

@Getter
@Setter
@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Users users;

    @Column
    private float rating;

    @Column
    @Convert(converter = DateConverter.class)
    private LocalDate date;
}