package com.movie;

import java.util.Properties;
import com.movie.domain.City;
import com.movie.domain.Film;
import com.movie.domain.Actor;
import com.movie.domain.Staff;
import com.movie.domain.Store;
import com.movie.domain.Rental;
import com.movie.domain.Payment;
import com.movie.domain.Address;
import com.movie.domain.Country;
import com.movie.domain.Category;
import com.movie.domain.Language;
import com.movie.domain.Customer;
import com.movie.domain.FilmText;
import com.movie.domain.Inventory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.Configuration;

public class Main {

    private final SessionFactory sessionFactory;

    public Main() {
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/movie");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "root");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.HBM2DDL_AUTO, "validate");

        sessionFactory = new Configuration()
                .addAnnotatedClass(Actor.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Film.class)
                .addAnnotatedClass(FilmText.class)
                .addAnnotatedClass(Inventory.class)
                .addAnnotatedClass(Language.class)
                .addAnnotatedClass(Payment.class)
                .addAnnotatedClass(Rental.class)
                .addAnnotatedClass(Staff.class)
                .addAnnotatedClass(Store.class)
                .addProperties(properties)
                .buildSessionFactory();
    }
    public static void main(String[] args) {
        Main main = new Main();
    }
}
