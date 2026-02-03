package com.eventos.api.domain.event;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "event")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Column(name = "event_url", nullable = false)
    private String eventUrl;

    @Column(nullable = false)
    private Boolean remote;

    @Column(nullable = false)
    private Date date;
}
