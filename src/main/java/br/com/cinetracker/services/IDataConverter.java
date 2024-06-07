package br.com.cinetracker.services;

public interface IDataConverter {
    <T> T getData(String json, Class<T> clazz);
}
