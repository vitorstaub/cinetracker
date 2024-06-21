package br.com.cinetracker.service;

public interface IDataConverter {
    <T> T convertData(String json, Class<T> clazz);
}
