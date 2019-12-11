package com.jitun.hibernateTest.service;

import java.util.HashSet;
import java.util.Set;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.jitun.hibernateTest.model.Cart;
import com.jitun.hibernateTest.model.Items;
import com.jitun.hibernateTest.util.HibernateUtil;

public class HibernateOneToManyTest {

	public static void main(String[] args) {

		Cart cart = new Cart();
		cart.setName("MyCart");
		
		Items item1 = new Items("I10", 10, 1, cart);
		Items item2 = new Items("I20", 20, 2, cart);
		Set<Items> itemsSet = new HashSet<Items>();
		
		itemsSet.add(item1); itemsSet.add(item2);
		
		cart.setItems(itemsSet);
		cart.setTotal(10*1 + 20*2);
		
		SessionFactory sessionFactory= null;
		Session session= null;
		Transaction tx= null;
		try {
			sessionFactory= HibernateUtil.getSessionFactory();
			session= sessionFactory.getCurrentSession();
			tx= session.beginTransaction();
			session.save(cart);
			session.save(item1);
			session.save(item2);
			tx.commit();
			
			System.out.println("Cart1 ID="+cart.getId());
			System.out.println("item1 ID="+item1.getId()+", Foreign Key Cart ID="+item1.getCart().getId());
			System.out.println("item2 ID="+item2.getId()+", Foreign Key Cart ID="+item2.getCart().getId());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sessionFactory != null || !sessionFactory.isClosed()){
				sessionFactory.close();
			}
		}
	}

}
