package com.acme.eshop.repository;

import com.acme.eshop.model.Customer;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CRUDRepository<T, ID> {

    T create(T t) throws SQLException;

    List<T> createAll(T... ts) throws SQLException;

    List<T> findAll() throws SQLException;

    Optional<T> findByID(ID id) throws SQLException;

    boolean update(T t) throws SQLException;

    boolean delete(T t) throws SQLException;


}
