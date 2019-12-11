package com.jitun.hibernateTest.service;


import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.jitun.hibernateTest.model.Employee;
import com.jitun.hibernateTest.util.HibernateUtil;



public class HibernateCriteriaExamples {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// Prep work
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		//Get All Employees
		Criteria criteria = session.createCriteria(Employee.class);
		List<Employee> empList = criteria.list();
		for(Employee emp : empList){
			System.out.println("ID="+emp.getEmployeeId());
//			+", Zipcode="+emp.getAddress().getZipcode());
		}
		
		// Get with ID, creating new Criteria to remove all the settings
		criteria = session.createCriteria(Employee.class)
					.add(Restrictions.eq("id", new Long(3)));
		Employee emp = (Employee) criteria.uniqueResult();
		System.out.println("Name=" + emp.getFirstName() );
//		+ ", City="+ emp.getAddress().getCity());

		//Pagination Example
		empList = session.createCriteria(Employee.class)
					.addOrder(Order.desc("id"))
					.setFirstResult(0)
					.setMaxResults(2)
					.list();
		for(Employee emp4 : empList){
			System.out.println("Paginated Employees::"+emp4.getEmployeeId());
//			+","+emp4.getAddress().getCity());
		}

		//Like example
		empList = session.createCriteria(Employee.class)
				.add(Restrictions.like("name", "%i%"))
				.list();
		for(Employee emp4 : empList){
			System.out.println("Employees having 'i' in name::"+emp4.getFirstName());
//			+","+emp4.getAddress().getCity());
		}
		
		//Projections example
		long count = (Long) session.createCriteria(Employee.class)
				.setProjection(Projections.rowCount())
				.add(Restrictions.like("name", "%i%"))
				.uniqueResult();
		System.out.println("Number of employees with 'i' in name="+count);

		//using Projections for sum, min, max aggregation functions
		double sumSalary = (Double) session.createCriteria(Employee.class)
			.setProjection(Projections.sum("salary"))
			.uniqueResult();
		System.out.println("Sum of Salaries="+sumSalary);
		
		//Join example for selecting few columns
		criteria = session.createCriteria(Employee.class, "employee");
		criteria.setFetchMode("employee.address", FetchMode.JOIN);
		criteria.createAlias("employee.address", "address"); // inner join by default

		ProjectionList columns = Projections.projectionList()
						.add(Projections.property("name"))
						.add(Projections.property("address.city"));
		criteria.setProjection(columns);

		List<Object[]> list = criteria.list();
		for(Object[] arr : list){
			System.out.println(Arrays.toString(arr));
		}
		
		/**
		 *  Using Disjunction Objects with Criteria
		If we wanted to create an OR expression with more than two different criteria 
		(for example, “price > 25.0 OR name like Mou% OR description not like blocks%”),
		we would use an org.hibernate.criterion.Disjunction object to represent a disjunction.
		 */
		 	
		criteria = session.createCriteria(Employee.class);
		Criterion priceLessThan = Restrictions.lt("price", 10.0);
		Criterion mouse = Restrictions.ilike("description", "mouse", MatchMode.ANYWHERE);
		Criterion browser = Restrictions.ilike("description", "browser", MatchMode.ANYWHERE);
		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(priceLessThan);
		disjunction.add(mouse);
		disjunction.add(browser);
		criteria.add(disjunction);
		List results = criteria.list();
		
		/**
		 * sorting 
		 */
		criteria = session.createCriteria(Employee.class);
		criteria.add(Restrictions.gt("price",10.0));
		criteria.addOrder(Order.desc("price"));
		List<Employee> results1 = criteria.list();
		
		
		/*select distinct owner from Owner owner 
		join owner.cats cat 
		where cat.eyeColor = 'blue'
		Which is*/

		criteria = session.createCriteria(Employee.class, "owner");
		criteria.createAlias("owner.cats", "cat");
		criteria.add(Restrictions.eq("cat.eyeColor", "blue"));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		// Rollback transaction to avoid messing test data
		tx.commit();
		// closing hibernate resources
		sessionFactory.close();
	}

}
