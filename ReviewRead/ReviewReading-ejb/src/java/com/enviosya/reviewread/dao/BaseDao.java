/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.reviewread.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;


public abstract class BaseDao<T> {

	private Class<T> entityClass;

	public BaseDao(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	protected abstract EntityManager getEntityManager();

	public T persist(T entity) {
		getEntityManager().persist(entity);
		return entity;
	}

	public T edit(T entity) {
		getEntityManager().merge(entity);
		return entity;
	}

	public T remove(T entity) {
		getEntityManager().remove(entity);
		return entity;
	}

	public List<T> findAll() {
		Query q = getEntityManager().createQuery("SELECT e FROM " + entityClass.getName() + " e");
		List<T> list = (List<T>) q.getResultList();
		return list;
	}

	public T find(Long id) {
		T e = getEntityManager().find(entityClass, id);
		return e;
	}
	
	public List<T> findByAttribute(String attribute, String attributeName) {
		Query q = getEntityManager().createQuery("SELECT e FROM " + entityClass.getName() + " e where e." + attributeName + " = :attribute");
		q.setParameter("attribute", attribute);
		List<T> list = (List<T>) q.getResultList();
		return list;
	}
}