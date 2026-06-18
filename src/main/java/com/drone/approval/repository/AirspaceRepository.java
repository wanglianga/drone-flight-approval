package com.drone.approval.repository;

import com.drone.approval.entity.Airspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AirspaceRepository extends JpaRepository<Airspace, Long> {

    Optional<Airspace> findByCode(String code);

    @Query(value = "SELECT * FROM airspaces a WHERE " +
            "ST_DWithin(ST_SetSRID(ST_MakePoint(a.center_longitude, a.center_latitude), 4326), " +
            "ST_SetSRID(ST_MakePoint(:lon, :lat), 4326), :radiusMeters)", nativeQuery = true)
    List<Airspace> findAirspacesNearPoint(@Param("lat") BigDecimal latitude,
                                          @Param("lon") BigDecimal longitude,
                                          @Param("radiusMeters") Double radiusMeters);

    @Query("SELECT a FROM Airspace a WHERE a.restriction != 'OPEN'")
    List<Airspace> findRestrictedAirspaces();
}
