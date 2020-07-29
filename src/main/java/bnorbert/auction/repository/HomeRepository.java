package bnorbert.auction.repository;

import bnorbert.auction.domain.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HomeRepository extends JpaRepository<Home, Long> {

    @Override
    List<Home> findAll();
}
