package by.anabios13.authorizationService.repository;

import by.anabios13.authorizationService.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address,Integer> {
}
