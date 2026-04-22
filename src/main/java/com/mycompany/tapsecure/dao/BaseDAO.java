/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tapsecure.dao;

import java.util.List;

/**
 *
 * @author vaena
 */
public interface BaseDAO<T> {
    void save(T entity);
    void update(int index, T entity);
    void delete(int index);
    List<T> findAll();
    T findByIndex(int index); 
    
}
