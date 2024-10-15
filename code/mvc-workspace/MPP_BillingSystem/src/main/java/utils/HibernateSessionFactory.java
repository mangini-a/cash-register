package utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateSessionFactory {

	private static SessionFactory sessionFactory;
	
	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
					.configure() // configure settings from hibernate.cfg.xml
					.build();
			try {
				sessionFactory = new MetadataSources(registry).buildMetadata().getSessionFactoryBuilder().build();
			} catch (Exception e) {
				// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
				// so destroy it manually
				StandardServiceRegistryBuilder.destroy(registry);
				throw new RuntimeException("There was an error building the factory", e);
			}
		}
		return sessionFactory;
	}
}
