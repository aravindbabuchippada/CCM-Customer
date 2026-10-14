package com.ccm.customer.service;

import com.ccm.common.exception.UpdateException;
import com.ccm.common.responsepojos.ClientMapResponse;
import com.ccm.customer.entity.CustomerClientMap;
import com.ccm.customer.entity.Customers;
import com.ccm.customer.feignclient.CCMClientInterface;
import com.ccm.customer.pojos.CustomerClientMapPojo;
import com.ccm.customer.pojos.CustomerPojo;
import com.ccm.customer.repository.CustomerClientMapRepository;
import com.ccm.customer.repository.CustomerRepository;
import com.ccm.customer.responsepojos.CustomerMappedClient;
import com.ccm.customer.responsepojos.CustomerResponse;
import com.ccm.customer.responsepojos.ResponsePojo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerClientMapRepository customerClientMapRepository;
    private final CCMClientInterface ccmClientInterface;

    public CustomerService(CustomerRepository customerRepository,
                           CustomerClientMapRepository customerClientMapRepository,
                           CCMClientInterface ccmClientInterface) {
        this.customerRepository = customerRepository;
        this.customerClientMapRepository = customerClientMapRepository;
        this.ccmClientInterface = ccmClientInterface;
    }

    public List<CustomerResponse> getCustomerList() {
        try {
            List<Customers> customers = customerRepository.findAll(Sort.by(Sort.Direction.ASC, "customerName"));

            List<CustomerResponse> customerResponses = customers.stream().map(customer -> {
                CustomerResponse customerResponse = new CustomerResponse();
                BeanUtils.copyProperties(customer, customerResponse);
                customerResponse.setTotalCount(customers.size());
                return customerResponse;
            }).toList();

            return customerResponses;
        } catch (RuntimeException e) {
            throw new UpdateException("Failed to call getCustomerList => "+e.getMessage());
        }
    }

    private Customers findCustomerById(Integer id) {
        return customerRepository.findById(id).orElseThrow(() -> new NoSuchElementException("There is no Customer record with id = "+id));
    }

    public ResponsePojo saveCustomer(CustomerPojo customerPojo) {
        try {
            Customers customer;

            if(customerPojo.getId() > 0) {
                customer = customerRepository.findById(customerPojo.getId()).orElseThrow(() ->
                        new NoSuchElementException("There is no Customer with id = "+customerPojo.getId()));

                BeanUtils.copyProperties(customerPojo, customer);
                customer.setModifiedOn(LocalDateTime.now());
            }
            else {
                customer = new Customers();
                BeanUtils.copyProperties(customerPojo, customer);

                customer.setId(null);
                customer.setCreatedOn(LocalDateTime.now());
            }

            customerRepository.save(customer);
            return new ResponsePojo();
        } catch (RuntimeException e) {
            throw new UpdateException("Failed to call saveCustomer => "+e.getMessage());
        }
    }

    public ResponsePojo saveCustomerClientMap(CustomerClientMapPojo customerClientMapPojo) {
        try {
            CustomerClientMap customerClientMap = new CustomerClientMap();

            if(customerClientMapPojo.getId() > 0) {
                if(customerClientMapPojo.getCustomerId() > 0 && customerClientMapPojo.getClientId() > 0) {
                    customerClientMap = customerClientMapRepository.findById(customerClientMapPojo.getId()).orElseThrow(() ->
                            new NoSuchElementException("There is no record in CustomerClientMap with id = "+customerClientMapPojo.getId()));

                    BeanUtils.copyProperties(customerClientMapPojo, customerClientMap);
                    customerClientMap.setModifiedOn(LocalDateTime.now());
                }
            } else {
                BeanUtils.copyProperties(customerClientMapPojo, customerClientMap, "id");
                customerClientMap.setCreatedOn(LocalDateTime.now());
            }
            customerClientMapRepository.save(customerClientMap);
            return new ResponsePojo();
        } catch (RuntimeException e) {
            throw new UpdateException("Failed to call saveCustomerClientMap => "+e.getMessage());
        }
    }

    public CustomerMappedClient getCustomerClientMap(Integer customerClientMapId) {
        try {
            CustomerClientMap customerClientMap = customerClientMapRepository.findById(customerClientMapId)
                    .orElseThrow(() -> new NoSuchElementException("There is no CustomerClientMap record with id = " + customerClientMapId));

            ClientMapResponse clientMapResponse = ccmClientInterface.getClientById(customerClientMap.getClientId()).getBody();

            Customers existingCustomer = findCustomerById(customerClientMap.getCustomerId());

            CustomerMappedClient customerMappedClient = CustomerMappedClient.builder()
                    .customerClientMapId(customerClientMapId)
                    .customerId(customerClientMap.getCustomerId())
                    .customerName(existingCustomer.getCustomerName())
                    .mappedClient(clientMapResponse)
                    .build();

            return customerMappedClient;
        } catch (RuntimeException e) {
            throw new UpdateException("Failed to call getCustomerClientMap => "+e.getMessage());
        }
    }
}
