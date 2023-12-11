package com.epam.xstack.service;

import java.util.Optional;

public interface AbstractService<T> {

    Optional<T> select(String username);

}
