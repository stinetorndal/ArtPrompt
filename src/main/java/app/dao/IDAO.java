package app.dao;

import java.util.List;

//Denne klasse definerer en standardiseret skabelon/kontrakt for alle DAO'er
public interface IDAO<T, I> {
    T create(T t);
    T findById(I id);
    List<T> findAll();
    T update(T t);
    boolean delete(I id);
}