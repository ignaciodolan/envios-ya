
package com.enviosya.reviewwrite.dao;

import com.enviosya.reviewwrite.dto.ReviewDTO;
import com.enviosya.reviewwrite.entities.ReviewEntity;
import com.enviosya.reviewwrite.entities.ReviewFeeling;
import com.enviosya.reviewwrite.entities.ReviewStatus;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class ReviewWriteDAO {
    
    @PersistenceContext(unitName = "Review-ejbPU")
    
    private EntityManager entityManager;
    
     public void add(ReviewDTO reviewDTO) {
        try {
            ReviewEntity entity = toEntity(reviewDTO);
            entityManager.persist(entity);
        } catch (Exception e) {
            throw e;
        }
    } 

    private ReviewEntity toEntity(ReviewDTO reviewDTO) {
        ReviewEntity entity = new ReviewEntity();
        entity.setComment(reviewDTO.getComment());
        entity.setCreatedDate(reviewDTO.getCreatedDate());
        entity.setRating(reviewDTO.getRating());
        entity.setReviewFeeling(ReviewFeeling.fromInteger(reviewDTO.getFeeling()));
        entity.setReviewStatus(ReviewStatus.fromInteger(reviewDTO.getStatus()));
        entity.setShimpentId(reviewDTO.getShipmentId());
        return entity;
    }
}
