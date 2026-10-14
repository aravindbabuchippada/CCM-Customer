package com.ccm.customer.repository;

import com.ccm.customer.entity.CustomerClientMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerClientMapRepository extends JpaRepository<CustomerClientMap, Integer> {

}
