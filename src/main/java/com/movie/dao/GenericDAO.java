package com.movie.dao;

import java.util.List;

import org.hibernate.Session;
import jakarta.persistence.Query;
import org.hibernate.SessionFactory;

public abstract class GenericDAO<T> {
    private final Class<T> clazz;
    private SessionFactory sessionFactory;

    public GenericDAO(final Class<T> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    /**
     * Возвращает объект по его id
     *
     * @param id - id объекта
     * @return
     */
    public T getById(final int id) {
        return (T) getCurrentSession().get(clazz, id);
    }

    /**
     * Возвращает список объектов в диапазоне
     *
     * @param offset - начальный элемент
     * @param count  - количество элементов
     * @return
     */
    public List<T> getItems(int offset, int count) {
        Query query = getCurrentSession().createQuery("from " + clazz.getName(), clazz);
        query.setFirstResult(offset);
        query.setMaxResults(count);
        return query.getResultList();
    }

    /**
     * Возвращает весь список элементов
     *
     * @return
     */
    public List<T> findAll() {
        return getCurrentSession().createQuery("from " + clazz.getName(), clazz).list();
    }

    /**
     * Сохранеят объект в базу
     *
     * @param entity - объект
     * @return
     */
    public T save(final T entity) {
        getCurrentSession().saveOrUpdate(entity);
        return entity;
    }

    /**
     * Обновляет объект в базе
     *
     * @param entity - объект
     * @return
     */
    public T update(final T entity) {
        return (T) getCurrentSession().merge(entity);
    }

    /**
     * Удаляет объект из базы
     *
     * @param entity - объект
     */
    public void delete(final T entity) {
        getCurrentSession().delete(entity);
    }

    /**
     * Удаляет объект из базы по его id
     *
     * @param entityId - id объекта
     */
    public void deleteById(final int entityId) {
        final T entity = getById(entityId);
        delete(entity);
    }

    /**
     * Возвращает сессию
     *
     * @return
     */
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}
