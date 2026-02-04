package com.eventos.api.config.seed;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.eventos.api.domain.address.Address;
import com.eventos.api.domain.coupon.Coupon;
import com.eventos.api.domain.event.Event;
import com.eventos.api.repositories.AddressRepository;
import com.eventos.api.repositories.CouponRepository;
import com.eventos.api.repositories.EventRepository;

@Component
@Order(1)
public class DatabaseSeeder implements CommandLineRunner {

    private final EventRepository eventRepository;
    private final AddressRepository addressRepository;
    private final CouponRepository couponRepository;

    public DatabaseSeeder(EventRepository eventRepository,
            AddressRepository addressRepository,
            CouponRepository couponRepository) {
        this.eventRepository = eventRepository;
        this.addressRepository = addressRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (eventRepository.count() > 0)
            return;

        Event e1 = new Event();
        e1.setTitle("Workshop Java Spring Boot");
        e1.setDescription("Aprenda Spring Boot do zero em um workshop prático.");
        e1.setEventUrl("https://eventos.com/java-spring");
        e1.setRemote(false);
        e1.setDate(LocalDateTime.now().plusDays(10));
        e1.setImgUrl("/uploads/default1.jpg");
        eventRepository.save(e1);

        Address a1 = new Address();
        a1.setCity("São Paulo");
        a1.setState("SP");
        a1.setEvent(e1);
        addressRepository.save(a1);

        Coupon c1 = new Coupon();
        c1.setCode("SPRING50");
        c1.setDiscount(50);
        c1.setValid(LocalDateTime.now().plusDays(30));
        c1.setEvent(e1);
        couponRepository.save(c1);

        Event e2 = new Event();
        e2.setTitle("Curso de React.js");
        e2.setDescription("Curso online de React com práticas e exercícios.");
        e2.setEventUrl("https://eventos.com/react");
        e2.setRemote(true);
        e2.setDate(LocalDateTime.now().plusDays(5));
        e2.setImgUrl("/uploads/default2.jpg");
        eventRepository.save(e2);

        Address a2 = new Address();
        a2.setCity("Rio de Janeiro");
        a2.setState("RJ");
        a2.setEvent(e2);
        addressRepository.save(a2);

        Coupon c2 = new Coupon();
        c2.setCode("REACT20");
        c2.setDiscount(20);
        c2.setValid(LocalDateTime.now().plusDays(20));
        c2.setEvent(e2);
        couponRepository.save(c2);

        Event e3 = new Event();
        e3.setTitle("Workshop de UX/UI");
        e3.setDescription("Aprenda conceitos de UX/UI e prototipagem.");
        e3.setEventUrl("https://eventos.com/ux-ui");
        e3.setRemote(true);
        e3.setDate(LocalDateTime.now().plusDays(15));
        e3.setImgUrl("/uploads/default3.jpg");
        eventRepository.save(e3);

        Address a3 = new Address();
        a3.setCity("Belo Horizonte");
        a3.setState("MG");
        a3.setEvent(e3);
        addressRepository.save(a3);

        Coupon c3 = new Coupon();
        c3.setCode("UXUI30");
        c3.setDiscount(30);
        c3.setValid(LocalDateTime.now().plusDays(25));
        c3.setEvent(e3);
        couponRepository.save(c3);

        System.out.println("✅ Database seeded: 3 eventos, 3 endereços, 3 cupons");
    }
}
