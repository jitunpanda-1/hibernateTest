package com.jitun.hibernateTest.util;

import java.io.File;
import java.text.Annotation;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal();
	
	private static SessionFactory buildSessionFactory(){
		try {
			return new AnnotationConfiguration().configure(new File("src/main/resources/hibernate.cfg.xml")).buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
		}
    }
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	 public static void shutdown() {
		 getSessionFactory().close();
	 }
	 
	 public static void closeSession() throws HibernateException {
		    Session session = (Session) threadLocal.get();
		    threadLocal.set(null);

		    if (session != null) {
		      session.close();
		    }
		  }
}
