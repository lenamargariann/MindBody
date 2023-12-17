package com.epam.xstack.service;

public interface AbstractService<T> {

    T select(String username);

}
