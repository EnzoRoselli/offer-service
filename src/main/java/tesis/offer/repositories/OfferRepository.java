package tesis.offer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tesis.offer.models.Offer;
import tesis.offer.models.OfferTypes;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByProductID(Integer id);

    List<Offer> findByBranchID(Integer id);

    @Query(
            value = "SELECT * FROM offers o " +
                    "inner join branches br on br.id=o.branch_id " +
                    "inner join companies cc on cc.id=br.company_id " +
                    "WHERE cc.id = ?1",
            nativeQuery = true)
    List<Offer> findByCompanyID(Integer id);

    @Query(value = "SELECT offers.id as ofID,offers.branch_id,offers.card_id,offers.price,offers.offer_type,offers.from_date,offers.to_date,offers.available,offers.old_price," +
            "products.id,products.name,products.image,products.clasification, products.description, offers.offer_description as descrip FROM db_my_market.offers  \n " +
            "inner join db_my_market.products on products.id=offers.product_id \n" +
            "where offers.from_date<= ?1 and offers.to_date>=?2 and offers.available=true and offers.offer_type in (?3)" +
            "and products.clasification in (?4) " +
            "and products.name LIKE CONCAT('%',?5,'%')" +
            "",nativeQuery = true)
    List<Object[]> dameProductos(LocalDateTime start, LocalDateTime end, List<String> offers,List<String> clasifications, String name);

    List<Offer> findByOfferTypeAndAvailableTrue(OfferTypes type);

    List<Offer> findByFromDateGreaterThanEqualAndAvailableTrue(LocalDateTime date);

    List<Offer> findAllByFromDateLessThanEqualAndToDateGreaterThanEqualAndAvailableTrueAndOfferTypeIn(LocalDateTime dateStart,LocalDateTime dateEnd,List<OfferTypes> offers);

    List<Offer> findByFromDateLessThanEqualAndToDateGreaterThanEqualAndAvailableTrue(LocalDateTime start, LocalDateTime end);

    @Query(
            value = "SELECT * FROM offers o " +
                    "WHERE ?1 between o.from_date and o.to_date",
            nativeQuery = true)
    List<Offer> findBySpecificDate(LocalDateTime specificDate);

    @Query(
            value = "select offers.* from offers\n" +
                    "inner join branches br on br.id = offers.branch_id\n" +
                    "inner join users us on us.id = br.user_id\n" +
                    "and offers.available=true\n" +
                    "and us.id=?1\n" +
                    "group by(offers.id)",
            nativeQuery = true)
    List<Offer> findAvailableOfferForUser(Long idUser);

    @Query(
            value = "SELECT * FROM offers o " +
                    "inner join branches br on br.id=o.branch_id " +
                    "WHERE br.city = ?1",
            nativeQuery = true)
    List<Offer> findByCity(String city);

    @Query(
            value = "select * from offers o \n" +
                    "where product_id = ?1 AND CAST(price AS DECIMAL) = CAST(?2 AS DECIMAL) \n" +
                    "AND card_id <=> ?3 AND offer_type = ?4 AND \n" +
                    "from_date = ?5 and to_date = ?6 AND \n" +
                    "available = TRUE AND old_price = ?7 and offer_description <=> ?8",
            nativeQuery = true)
    List<Offer> findSimilarOffers(Integer productID, Float price, Integer cardID, String offerType, LocalDateTime fromDate, LocalDateTime toDate, String oldPrice, String offerDescription);

    @Query(
            value = "select name from products \n" +
                    "where id = ?1"
            ,nativeQuery = true)
    String getProductName(Integer productID);

    @Query(
            value = "select image from products \n" +
                    "where id = ?1"
            ,nativeQuery = true)
    String getProductImage(Integer productID);
}
