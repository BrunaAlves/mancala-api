package com.game.mancala.dao;

import java.util.List;

public interface IDAO<T> {

    List<T> getAll();

    T save(T t);

    void delete(T t);

}
