package com.company.web.wallet.repositories;


import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.Invitation;
import com.company.web.wallet.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class InvitationRepositoryImpl implements InvitationRepository{

    private final SessionFactory sessionFactory;

    public InvitationRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Invitation invitation) {
        try(Session session = sessionFactory.openSession()){
            session.save(invitation);
        }
    }

    @Override
    public Invitation getByEmail(String email) {
        try (Session session = sessionFactory.openSession()){
            Query<Invitation> query = session.createQuery("from Invitation where email = :email ", Invitation.class);
            query.setParameter("email", email);

            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("Invitation", "email", email);
            } else {
                return query.list().get(0);
            }
        }
    }

    @Override
    public boolean checkByEmail(String email) {
        try (Session session = sessionFactory.openSession()){
            Query<Invitation> query = session.createQuery("from Invitation where email = :email ", Invitation.class);
            query.setParameter("email", email);
            return !query.list().isEmpty();
        }
    }

    @Override
    public int getInvitationsCount(User user) {
        try(Session session = sessionFactory.openSession()){
            Query<Invitation> query = session.createQuery("from Invitation where inviter.id = :inviterId", Invitation.class);
            query.setParameter("inviterId", user.getId());
            if (query.list().isEmpty()){
                throw new EntityNotFoundException("Invitation", "user", user.getUsername());
            }
            return query.list().size();
        }
    }

}
