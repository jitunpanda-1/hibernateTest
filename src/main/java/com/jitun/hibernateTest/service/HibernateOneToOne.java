package com.jitun.hibernateTest.service;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.jitun.hibernateTest.model.Customer;
import com.jitun.hibernateTest.model.Txn;
import com.jitun.hibernateTest.util.HibernateUtil;

public class HibernateOneToOne {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Txn txn=buildDemoTransaction();

		SessionFactory sessionFactory = null;
		Session session = null;
		Transaction tx = null;
		
		try {
			sessionFactory=HibernateUtil.getSessionFactory();
			session =sessionFactory.openSession();
			System.out.println("Session created using annotations configuration");
			
			//transaction started
			tx= session.beginTransaction();
			
			session.save(txn);
			tx.commit();
			System.out.println("Annotation Example. Transaction ID="+txn.getId());
			
			//Get Saved Trasaction Data
			printTransactionData(txn.getId(), sessionFactory);
			
		} catch (Exception e) {
			System.out.println("Exception occured. "+e.getMessage());
			e.printStackTrace();
		}finally {
			if(sessionFactory !=null && !sessionFactory.isClosed()){
				System.out.println("Closing SessionFactory");
				sessionFactory.close();
			}
		}
		
		
	}
	
	private static  void printTransactionData(long id, SessionFactory sessionFactory) {
		Session session = null;
		Transaction tx = null;
		try{
			//Get Session
			sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.getCurrentSession();
			//start transaction
			tx = session.beginTransaction();
			//Save the Model object
			Txn txn = (Txn) session.get(Txn.class, id);
			//Commit transaction
			tx.commit();
			System.out.println("Annotation Example. Transaction Details=\n"+txn);
			
			}catch(Exception e){
				System.out.println("Exception occured. "+e.getMessage());
				e.printStackTrace();
			}
	}
	private static Txn buildDemoTransaction() {
		Txn txn = new Txn();
		txn.setDate(new Date());
		txn.setTotal(100);
		
		Customer cust = new Customer();
		cust.setAddress("wakad, Pune");
		cust.setEmail("jitunpanda@yahoo.com");
		cust.setName("Jitun");
		
		txn.setCustomer(cust);
		
		cust.setTxn(txn);
		return txn;
	}

}
