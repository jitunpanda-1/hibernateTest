package com.jitun.hibernateTest.service;

import org.hibernate.Session;

import com.jitun.hibernateTest.model.Employee;
import com.jitun.hibernateTest.util.HibernateUtil;

public class HibernateMain {

	public static void main(String[] args) {
		Session session= HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		/**
		 * L-17-24 represents object save
		 */
		Employee employee=new Employee();
		employee.setEmail("jitun@gmail.com");
		employee.setFirstName("jitun2");
		employee.setLastName("Panda2");
		employee.setSalary(5000);
		
		session.save(employee);
		session.getTransaction().commit();
		
		/**
		 * get or load data from DB
		 * 
		 */
//		Integer impId=
		Employee emp1= (Employee)session.load(Employee.class, 50);
		System.out.println(emp1.getFirstName() + " - " +emp1.getLastName());
		
		
		Session sessionThree = HibernateUtil.getSessionFactory().openSession();
	      sessionThree.beginTransaction();
		Employee emp2 = (Employee) sessionThree.get("com.jitun.hibernateTest.model.Employee", 205);
		System.out.println(emp2.getFirstName() + " - " +emp2.getLastName());
		
		session.flush();
		HibernateUtil.shutdown();

	}

}
