package com.movie.util;

import com.movie.domain.Actor;
import com.movie.domain.Address;
import com.movie.domain.Category;
import com.movie.domain.City;
import com.movie.domain.Country;
import com.movie.domain.Customer;
import com.movie.domain.Film;
import com.movie.domain.FilmText;
import com.movie.domain.Inventory;
import com.movie.domain.Language;
import com.movie.domain.Payment;
import com.movie.domain.Rental;
import com.movie.domain.Staff;
import com.movie.domain.Store;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class DBSessionFactory {
    private static DBSessionFactory instance;
    private final SessionFactory sessionFactory;

    private DBSessionFactory() {
        Configuration configuration = new Configuration();
        configuration
                .addAnnotatedClass(Actor.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Film.class)
                .addAnnotatedClass(FilmText.class)
                .addAnnotatedClass(Inventory.class)
                .addAnnotatedClass(Payment.class)
                .addAnnotatedClass(Language.class)
                .addAnnotatedClass(Rental.class)
                .addAnnotatedClass(Staff.class)
                .addAnnotatedClass(Store.class)
                .configure();

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        this.sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public static SessionFactory getSessionFactory() {
        if (instance == null) {
            instance = new DBSessionFactory();
        }
        return instance.sessionFactory;
    }
}