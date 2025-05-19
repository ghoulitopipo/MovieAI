package fr.univtln.laure.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Getter
@Setter
@Entity
public class Movie {
    @Id
    private long id;

    @Column
    private String title;

    @Column
    private String genre;

    @Column
    private Long tmdb;
}