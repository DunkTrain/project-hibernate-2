package com.movie;

import com.movie.dao.*;
import com.movie.domain.*;

import java.math.BigDecimal;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import com.movie.util.DBSessionFactory;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.Configuration;

public class Main {
    private final SessionFactory sessionFactory;

    private final ActorDAO actorDAO;
    private final AddressDAO addressDAO;
    private final CategoryDAO categoryDAO;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;
    private final CustomerDAO customerDAO;
    private final FilmDAO filmDAO;
    private final FilmTextDAO filmTextDAO;
    private final InventoryDAO inventoryDAO;
    private final LanguageDAO languageDAO;
    private final PaymentDAO paymentDAO;
    private final RentalDAO rentalDAO;
    private final StaffDAO staffDAO;
    private final StoreDAO storeDAO;

    public Main() {
        sessionFactory = DBSessionFactory.getSessionFactory();

        actorDAO = new ActorDAO(sessionFactory);
        addressDAO = new AddressDAO(sessionFactory);
        categoryDAO = new CategoryDAO(sessionFactory);
        cityDAO = new CityDAO(sessionFactory);
        countryDAO = new CountryDAO(sessionFactory);
        customerDAO = new CustomerDAO(sessionFactory);
        filmDAO = new FilmDAO(sessionFactory);
        filmTextDAO = new FilmTextDAO(sessionFactory);
        inventoryDAO = new InventoryDAO(sessionFactory);
        languageDAO = new LanguageDAO(sessionFactory);
        paymentDAO = new PaymentDAO(sessionFactory);
        rentalDAO = new RentalDAO(sessionFactory);
        staffDAO = new StaffDAO(sessionFactory);
        storeDAO = new StoreDAO(sessionFactory);
    }

    public static void main(String[] args) {
        Main main = new Main();
        Customer customer = main.createCustomer();
        main.customerReturnInventoryToStore();
        main.customerRentInventory(customer);
        main.newFilmWasMade();
    }

    private void newFilmWasMade() {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();

            Language language = languageDAO.getItems(0, 20).stream()
                    .unordered()
                    .findAny()
                    .get();
            List<Category> categories = categoryDAO.getItems(0, 5);
            List<Actor> actors = actorDAO.getItems(0, 20);

            Film film = new Film();
            film.setTitle("Cry");
            film.setDescription("New scary film!!!");
            film.setYear(Year.now());
            film.setActors(new HashSet<>(actors));
            film.setLength((short) 73);
            film.setLanguage(language);
            film.setOriginalLanguage(language);
            film.setCategories(new HashSet<>(categories));
            film.setRating(Rating.NC17);
            film.setRentalRate(BigDecimal.ZERO);
            film.setRentalDuration((byte) 73);
            film.setReplacementCost(BigDecimal.TEN);
            film.setSpecialFeatures(Set.of(Feature.TRAILERS, Feature.COMMENTARIES));
            filmDAO.save(film);

            FilmText filmText = new FilmText();
            filmText.setId(film.getId());
            filmText.setFilm(film);
            filmText.setTitle("Cry");
            filmText.setDescription("Why here 73?");
            filmTextDAO.save(filmText);

            transaction.commit();
        }
    }

    private void customerRentInventory(Customer customer) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();

            Film film = filmDAO.getFirstAvailableFilmForRent();
            Store store = storeDAO.getItems(0, 1).get(0);

            Inventory inventory = new Inventory();
            inventory.setFilm(film);
            inventory.setStore(store);
            inventoryDAO.save(inventory);

            Staff staff = store.getStaff();

            Rental rental = new Rental();
            rental.setRentalDate(LocalDateTime.now());
            rental.setCustomer(customer);
            rental.setInventory(inventory);
            rental.setStaff(staff);
            rentalDAO.save(rental);

            Payment payment = new Payment();
            payment.setPaymentDate(LocalDateTime.now());
            payment.setCustomer(customer);
            payment.setAmount(BigDecimal.valueOf(73.73));
            payment.setRental(rental);
            payment.setStaff(staff);
            paymentDAO.save(payment);

            transaction.commit();
        }
    }

    private void customerReturnInventoryToStore() {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();

            Rental unreturnedRental = rentalDAO.getAnyUnreturnedRental();
            unreturnedRental.setReturnDate(LocalDateTime.now());
            rentalDAO.save(unreturnedRental);

            transaction.commit();
        }
    }

    private Customer createCustomer() {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();

            Store store = storeDAO.getItems(0, 1).get(0);
            City city = cityDAO.getByName("Kragujevac");

            Address address = new Address();
            address.setAddress("Indep str, 48");
            address.setCity(city);
            address.setPhone("999-111-555");
            address.setDistrict("strangeSomething");
            addressDAO.save(address);

            Customer customer = new Customer();
            customer.setFirstName("Dmitry");
            customer.setLastName("DSH");
            customer.setAddress(address);
            customer.setEmail("test@gmail.com");
            customer.setStore(store);
            customer.setActive(true);
            customerDAO.save(customer);

            transaction.commit();
            return customer;
        }
    }
}
