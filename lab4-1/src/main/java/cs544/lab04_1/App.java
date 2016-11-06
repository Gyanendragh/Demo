package cs544.lab04_1;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import cs544.lab04_1.model.Airline;
import cs544.lab04_1.model.Airplane;
import cs544.lab04_1.model.Airport;
import cs544.lab04_1.model.Flight;

public class App {
	private static Logger logger = Logger.getLogger(App.class);;

	private static final EntityManagerFactory emf;

	static {
		try {
			emf = Persistence.createEntityManagerFactory("cs544");
		} catch (Throwable ex) {
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static void main(String[] args) {
		EntityManager em = null;
		EntityTransaction tx = null;

		// fill the database
		fillDataBase();

		try {
			em = emf.createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			logger.trace("List all the flights:");
			System.out.println(" a).Flight >500");
			Query q1=em.createQuery("select f from Flight f where f.origin.country='USA' and f.airplane.capacity>500");
			List<Flight> flights =q1.getResultList();
			for(Flight f1:flights){
				System.out.println(f1.getArrivalDate()+"/t"+f1.getArrivalTime()+"/t"+f1.getDepartureDate()+"/t"+f1.getDepartureTime()+"/t"+f1.getFlightnr());
			}
			
			System.out.println("b).All Ailine of model A380");
			Query q2=em.createQuery("select a from Airline a join a.flights f join f.airplane a1 where a1.model='A380'");
			List<Airline> airline =q2.getResultList();
			for(Airline a1:airline){
				System.out.println(a1.getName());
			}
			
			System.out.println("c).All flight 747 And ! star Alliance");
			Query q3=em.createQuery("select f from Flight f where f.airplane.model='747' and f.airline.name <>'Star Alliance'");
			List<Flight> flight =q3.getResultList();
			for(Flight f2: flight){
				System.out.println(f2.getFlightnr());
			}
			
			System.out.println("d).All flights leaving before 12 pm");
			Query q4=em.createQuery("select f from Flight f where f.departureTime<'12:00:00' and f.departureDate='2009-08-07'");
			List<Flight> flightss =q4.getResultList();
			for(Flight f3: flightss){
				System.out.println(f3.getDepartureDate());
			}
		
			
			
//			logger.trace(String.format("%-9s%-31s%-31s", "Flight:", "Departs:",
//					"Arrives:"));
//			for (Flight flight : flights) {
//				logger.trace(String.format(
//						"%-7s  %-12s %7s %8s  %-12s %7s %8s",
//						flight.getFlightnr(), flight.getOrigin().getCity(),
//						flight.getDepartureDate(), flight.getDepartureTime(),
//						flight.getDestination().getCity(),
//						flight.getArrivalDate(), flight.getArrivalTime()));
//			}
			
			
		
			tx.commit();
		} catch (PersistenceException e) {
			if (tx != null) {
				logger.error("Rolling back:", e);
				tx.rollback();
			}
		} finally {
			if (em != null) {
				em.close();
			}
		}
		
		

	}

	public static void fillDataBase() {
		EntityManager em = null;
		EntityTransaction tx = null;

		try {

			Airport mco = new Airport("MCO", "Orlando", "Florida", "USA");
			Airport mia = new Airport("MIA", "Miami", "Florida", "USA");

			Airplane airplane1 = new Airplane("56789", "A380", 519);

			Airline airline1 = new Airline("Delta");

			Flight flight1 = new Flight("DE 36", "08/06/2009", "7:10 pm",
					"06/25/2015", "9:00 am", airline1, mia, mco, airplane1);

			em = emf.createEntityManager();
			tx = em.getTransaction();
			tx.begin();
			
			em.persist(flight1);

			tx.commit();
			
		} catch (PersistenceException e) {
			if (tx != null) {
				logger.error("Rolling back", e);
				tx.rollback();
			}
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

}
