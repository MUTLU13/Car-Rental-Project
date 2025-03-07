package com.sau.sbrental.Repositories;

import com.sau.sbrental.Models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByAvailableTrue();
}