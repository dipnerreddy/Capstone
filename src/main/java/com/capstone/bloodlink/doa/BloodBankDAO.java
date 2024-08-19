package com.capstone.bloodlink.doa;


import com.capstone.bloodlink.entity.BloodBank;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Transactional
    public List<BloodBank> allBloodBanks(String location){
        String jpql = "SELECT bb FROM BloodBank bb where bb.location=:location";
        List<BloodBank> resultList=entityManager.createQuery(jpql,BloodBank.class)
                .setParameter("location",location).getResultList();

        if(resultList.isEmpty()){
            return resultList;
        }
        else {
            return resultList;
        }
    }

}

/*

@Transactional
	public List<ScheduledTime> allScheduler(String mobileNo) {
		log.info("allScheduler " + mobileNo);
		String jpql = "SELECT NEW ScheduledTime (d.mobileNo, d.deviceId, d.rooom,d.deviceName,d.status,d.hour,d.minute,d.am_pm) FROM ScheduledTime d WHERE d.mobileNo = :mobile";
		List<ScheduledTime> resultList = entityManager.createQuery(jpql, ScheduledTime.class)
				.setParameter("mobile", mobileNo).getResultList();

		log.info("allScheduler:::" + resultList.size());

		if (resultList.isEmpty()) {
			return resultList;
		} else {
			return resultList;
		}
	}




public void updatePassword(String mobile, String password) {
		log.info("DAO class: " + mobile + password);

		Query updateQuery = entityManager
				.createQuery("UPDATE Users u SET u.password = :password WHERE u.mobileNo = :mobile");
		updateQuery.setParameter("password", password);
		updateQuery.setParameter("mobile", mobile);
		int updatedCount = updateQuery.executeUpdate();
		log.info("Updated Successfully: " + updatedCount);
	}
 */
