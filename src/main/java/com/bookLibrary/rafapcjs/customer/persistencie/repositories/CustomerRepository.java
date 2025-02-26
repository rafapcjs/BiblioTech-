package com.bookLibrary.rafapcjs.customer.persistencie.repositories;

import com.bookLibrary.rafapcjs.customer.persistencie.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository  extends JpaRepository<Customer,Long> {

}
