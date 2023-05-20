package com.game.mancala.dao;

import java.util.List;

public interface DAO<T> {

    List<T> getAll();

    void save(T t);

}
