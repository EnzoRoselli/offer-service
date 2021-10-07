package mymarket.offer.repositories;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import mymarket.offer.models.Offer;
import mymarket.offer.models.enums.OfferTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    @Query(value = "SELECT * FROM offers o " +
            "INNER JOIN branches_x_offers bxo " +
            "ON o.id = bxo.offer_id " +
            "INNER JOIN branches b " +
            "ON bxo.branch_id = b.id " +
            "WHERE (?1 IS NULL OR o.product_id = ?1) " +
            "AND (?2 IS NULL OR bxo.branch_id = ?2) " +
            "AND o.offer_type IN (?3) " +
            "AND (?4 IS NULL OR o.from_date >= ?4) " +
            "AND (?5 IS NULL OR o.to_date <= ?5) " +
            "AND (?6 IS NULL OR ?6 between o.from_date and o.to_date) " +
            "AND (?7 IS NULL OR ?7 = b.user_id) " +
            "AND (?8 IS NULL OR ?8 = b.city) " +
            "AND o.available = ?9",
            nativeQuery = true)
    List<Offer> findByFilters(Integer productId, Integer branchId, List<String> offerTypes,
                  LocalDate startDate, LocalDate endDate, LocalDate specificDate, Long userId, String city, Boolean available);
}
