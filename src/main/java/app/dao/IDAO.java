package app.dao;

import java.util.List;

public interface IDAO<T, I> {
    T create(T t);
    T findById(I id);
    List<T> findAll();
    T update(T t);
    boolean delete(I id);
}