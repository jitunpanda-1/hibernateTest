package com.jitun.hibernateTest.crud;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.jitun.hibernateTest.model.Student;
import com.jitun.hibernateTest.util.HibernateUtil;

public class CRUDTest {

	
	public static void main(String[] args) {
		SessionFactory factory=HibernateUtil.getSessionFactory();
		Session session= factory.getCurrentSession();
		final Transaction tx= session.beginTransaction();
		
		saveStudent(factory,session, tx);
//		getStudentList(factory, session, tx);
//		updateStudent(factory, session, tx);
//		deleteStudent(factory, session, tx);

	}
	
	public static void saveStudent(SessionFactory factory, Session session,Transaction tx) {
		
		Student std= new Student(1, "Suvendu", 1, "java");
		Student std1= new Student(2, "Suvendu", 2, "java");
		Student std2= new Student(3, "Suvendu", 3, "java");
		try {
//			session.beginTransaction();
//			tx.begin();
			session.save(std);
			session.save(std1);
			session.save(std2);
//			Integer stdId = std.getId();
			tx.commit();
			
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally {
			if(null!= factory || !factory.isClosed()){
				HibernateUtil.closeSession();
				factory.close();
			}
		}
	}
	
	public static List<Student> getStudentList(SessionFactory factory, Session session,Transaction tx) {
		List<Student> listStd= new ArrayList<Student>();
		try {
			Query query =session.createQuery("from Student");
			listStd= query.list();
			for(Student std:listStd){
				System.out.println(std.getId()+" : "+std.getStudentName()+" : "+std.getRollNumber()+" : "+std.getCourse());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(null!= factory || !factory.isClosed()){
				HibernateUtil.closeSession();
				factory.close();
			}
		}
		return listStd;
	}
	
	public static void updateStudent(SessionFactory factory, Session session,Transaction tx) {
		try {
			Student student = (Student)session.get(Student.class, 21);
			System.out.println(student);
			  student.setStudentName("jitun panda");
			  session.update(student);
			  System.out.println("Updated Successfully");
			  session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			if(null!= factory || !factory.isClosed()){
				HibernateUtil.closeSession();
				factory.close();
			}
		}
		
	}
	
	public static void deleteStudent(SessionFactory factory, Session session,Transaction tx) {
		try {
			Student student = (Student)session.get(Student.class, 21);
			session.delete(student);
			System.out.println("deleted Successfully");
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			if(null!= factory || !factory.isClosed()){
				HibernateUtil.closeSession();
				factory.close();
			}
		}
	}

}
