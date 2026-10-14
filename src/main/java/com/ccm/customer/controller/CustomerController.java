package com.ccm.customer.controller;

import com.ccm.customer.pojos.CustomerClientMapPojo;
import com.ccm.customer.pojos.CustomerPojo;
import com.ccm.customer.responsepojos.CustomerMappedClient;
import com.ccm.customer.responsepojos.CustomerResponse;
import com.ccm.customer.responsepojos.ResponsePojo;
import com.ccm.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("check")
    public ResponseEntity<String> check() {
        return new ResponseEntity<>("OK from CCM-Customer", HttpStatus.OK);
    }

    @GetMapping("getCustomerList")
    public ResponseEntity<List<CustomerResponse>> getCustomerList() {
        return new ResponseEntity<>(customerService.getCustomerList(), HttpStatus.OK);
    }

    @PostMapping("saveCustomer")
    public ResponseEntity<ResponsePojo> saveCustomer(@RequestBody CustomerPojo customerPojo) {
        return new ResponseEntity<>(customerService.saveCustomer(customerPojo), HttpStatus.OK);
    }

    @PostMapping("saveCustomerClientMap")
    public ResponseEntity<ResponsePojo> saveCustomerClientMap(@RequestBody CustomerClientMapPojo customerClientMapPojo) {
        return new ResponseEntity<>(customerService.saveCustomerClientMap(customerClientMapPojo), HttpStatus.OK);
    }

    @GetMapping("getCustomerClientMap")
    public ResponseEntity<CustomerMappedClient> getCustomerClientMap (@RequestParam("customerClientMapId") Integer customerClientMapId) {
        return new ResponseEntity(customerService.getCustomerClientMap(customerClientMapId), HttpStatus.OK);
    }
}
