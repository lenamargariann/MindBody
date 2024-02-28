package com.epam.xstack.dao;

import com.epam.xstack.model.Trainer;
import lombok.NonNull;

import java.util.Optional;

public interface AbstractDao<T> {
    Optional<T> create(@NonNull T object);

    Optional<T> findByUsername(@NonNull String username);

    Optional<T> update(@NonNull T object);

    boolean delete(@NonNull T object);

}

