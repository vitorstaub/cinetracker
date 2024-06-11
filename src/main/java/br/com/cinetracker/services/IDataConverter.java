package br.com.cinetracker.services;

public interface IDataConverter {
    <T> T convertData(String json, Class<T> clazz);
}
