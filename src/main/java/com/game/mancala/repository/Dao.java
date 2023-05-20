package com.game.mancala.repository;

import java.util.List;

public interface Dao<T> {

    List<T> getAll();

    void save(T t);

}
