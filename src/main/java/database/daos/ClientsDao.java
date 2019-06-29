package database.daos;

import database.datasets.ClientsDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

public class ClientsDao {
    private Session session;

    public ClientsDao(Session session) {
        this.session = session;
    }

    public List<ClientsDataSet> getAll() throws HibernateException {
        Criteria cb = session.createCriteria(ClientsDataSet.class);
        List<ClientsDataSet> result = new ArrayList<>();
        List b =  cb.list();
        for (Object o : b) {
            result.add((ClientsDataSet) o);
        }
        return result;
    }

    public ClientsDataSet get(long id) throws HibernateException {
        return (ClientsDataSet) session.get(ClientsDataSet.class, id);
    }

    public Long getClientId(String name) throws HibernateException {
        Criteria criteria = session.createCriteria(ClientsDataSet.class);
        ClientsDataSet obj = ((ClientsDataSet) criteria.add(Restrictions.eq("name", name)).uniqueResult());
        if (obj == null) {
            return null;
        }
        return obj.getId();
    }

    public String getClientPassportId(String name) throws HibernateException {
        Criteria criteria = session.createCriteria(ClientsDataSet.class);
        return ((ClientsDataSet) criteria.add(Restrictions.eq("name", name)).uniqueResult()).getPassportId();
    }

    public long insertClient(String name, String passportId) throws HibernateException {
        return (Long) session.save(new ClientsDataSet(name, passportId));
    }
}
