package com.capstone.bloodlink.doa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public class BloodBankDAO {
    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public void updateBloodBank(String email, int oPositive, int oNegative, int aPositive, int aNegative, int bPositive, int bNegative) {
        Query updateQuery = entityManager
                .createQuery("UPDATE BloodBank bb SET bb.oPositive = :oPositive, bb.oNegative= :oNegative, bb.aPositive= :aPositive, bb.aNegative=:aNegative, bb.bPositive=:bPositive, bb.bNegative=:bNegative WHERE bb.email=:email");
        updateQuery.setParameter("oPositive",oPositive);
        updateQuery.setParameter("oNegative",oNegative);
        updateQuery.setParameter("aPositive",aPositive);
        updateQuery.setParameter("aNegative",aNegative);
        updateQuery.setParameter("bPositive",bPositive);
        updateQuery.setParameter("bNegative",bNegative);
        updateQuery.setParameter("email",email);
        int updatedCount = updateQuery.executeUpdate();
    }

}
