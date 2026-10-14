package com.ccm.customer.feignclient;

import com.ccm.common.responsepojos.ClientMapResponse;
import com.ccm.customer.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "ccm-client",
        configuration = FeignClientConfig.class
)
public interface CCMClientInterface {

    @GetMapping("client/getClientById")
    public ResponseEntity<ClientMapResponse> getClientById(@RequestParam("id") Integer id);
}
