
package com.enviosya.reviewread.dao;


import com.enviosya.reviewread.dto.ReviewDTO;
import com.enviosya.reviewread.entities.ReviewEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class ReviewReadDAO extends BaseDao{
    
    @PersistenceContext(unitName = "Review-ejbPU")
    
    private EntityManager entityManager;

    public ReviewReadDAO() {
        super(ReviewEntity.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    public ReviewDTO toDTO(ReviewEntity entity) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setComment(entity.getComment());
        reviewDTO.setRating(entity.getRating());
        reviewDTO.setShipmentId(entity.getShipmentId());
        reviewDTO.setClientId(entity.getClientId());
        reviewDTO.setCreatedDate(entity.getCreatedDate());
        reviewDTO.setStatus(entity.getReviewStatus().ordinal());
        reviewDTO.setFeeling(entity.getReviewFeeling().ordinal());
        return reviewDTO;
    }
    
    public List<ReviewEntity> getReviewToSendClient(Long shipmentId, Long clientId) {
        try {
            return entityManager.createQuery(
                    "select r "
                    + "from ReviewEntity r "
                    + "where r.shipmentId = :shipmentId and r.clientId = :clientId")
                    .setParameter("shipmentId", shipmentId).setParameter("clientId", clientId).getResultList();
        } catch (Exception e) {
            throw e;
        }
    }
    public List<ReviewEntity> getReviews() {
        try {
            List<ReviewEntity> reviews = (List<ReviewEntity>) this.findAll();
            return reviews;
        } catch (Exception e) {
            throw e;
        }
    }

    public ReviewEntity find(Long id){
        return (ReviewEntity) entityManager.find(ReviewEntity.class, id);
    }

    public List<ReviewEntity> findByShipmentsId(Long shipmentId) {
        try {
            List<ReviewEntity> reviews = new ArrayList<>();
            reviews = entityManager.createQuery(
                    "select r "
                    + "from ReviewEntity r "
                    + "where r.shipmentId = :id"
                    + "ORDER BY r.createdDate DESC")
                    .setParameter("id", shipmentId).getResultList();            
            return reviews;
        } catch (Exception e) {
            throw e;
        }
    }
    
}
