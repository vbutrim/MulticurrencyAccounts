package services;

import com.google.inject.Singleton;
import database.DBException;
import database.daos.ClientsDao;
import database.datasets.AccountsDataSet;
import database.datasets.ClientsDataSet;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Singleton
public class DBService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "create";

    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getH2Configuration();
        sessionFactory = createSessionFactory(configuration);

        printConnectInfo();
    }

    private Configuration getH2Configuration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(ClientsDataSet.class);
        configuration.addAnnotatedClass(AccountsDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:./h2db");
        configuration.setProperty("hibernate.connection.username", "test");
        configuration.setProperty("hibernate.connection.password", "test");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }

    ClientsDataSet getClientByName(String clientName) throws DBException {
        if (clientName == null || clientName.isEmpty()) {
            System.out.println(String.format("[1] Failed getClientByName by Name %s", clientName));
            return null;
        }

        try {
            Session session = sessionFactory.openSession();
            ClientsDao dao = new ClientsDao(session);
            Long l = dao.getClientId(clientName);
            if (l == null) {
                System.out.println(String.format("[2] Failed getClientByName by Name %s", clientName));
                session.close();
                return null;
            }

            session.close();
            return getClientById(l);
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    ClientsDataSet getClientById(Long clientId) throws DBException {
        if (clientId == null) {
            System.out.println(String.format("Failed getClientById by Id %s", clientId));
            return null;
        }

        try {
            Session session = sessionFactory.openSession();
            ClientsDao dao = new ClientsDao(session);
            ClientsDataSet dataSet = dao.get(clientId);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    List<ClientsDataSet> getAllClients() throws DBException {
        try {
            Session session = sessionFactory.openSession();
            ClientsDao dao = new ClientsDao(session);
            List<ClientsDataSet> dataSet = dao.getAll();
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    long registerClient(String name, String passportId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            ClientsDao dao = new ClientsDao(session);
            long id = dao.insertClient(name, passportId);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    private void printConnectInfo() {
        try {
            SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFactory;
            Connection connection = sessionFactoryImpl.getConnectionProvider().getConnection();
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
