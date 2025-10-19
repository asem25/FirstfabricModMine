package ru.semavin.db.repo;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.semavin.db.HibernateUtil;
import ru.semavin.entity.MessageEntity;

public class MessageRepo {

    public void save(MessageEntity entity) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.persist(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

}
