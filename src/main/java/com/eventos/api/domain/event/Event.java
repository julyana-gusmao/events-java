package com.eventos.api.domain.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.eventos.api.domain.address.Address;
import com.eventos.api.domain.coupon.Coupon;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "event_url", nullable = false)
    private String eventUrl;

    @Column(nullable = false)
    private Boolean remote;

    @Column(nullable = false)
    private LocalDateTime date;

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coupon> coupons;
}
