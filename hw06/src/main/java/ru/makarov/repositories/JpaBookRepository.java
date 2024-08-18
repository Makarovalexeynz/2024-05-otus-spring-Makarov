package ru.makarov.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import ru.makarov.models.Book;

import java.util.List;

import java.util.Optional;

@Repository
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    public JpaBookRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class,id));
    }

    @Override
    @EntityGraph("book-author-genre")
    public List<Book> findAll() {
        TypedQuery<Book>query = em.createQuery(
                "SELECT b FROM Book b JOIN FETCH b.author JOIN FETCH b.genre", Book.class);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public void deleteById(long id) {
        Book book = em.find(Book.class, id);
        if (book != null) {
            em.remove(book);
        }
    }


    private Book insert(Book book) {
        em.persist(book);
        return book;
    }

    private Book update(Book book) {
        return em.merge(book);

    }

}
