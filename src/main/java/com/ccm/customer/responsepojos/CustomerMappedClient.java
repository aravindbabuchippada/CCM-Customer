package com.ccm.customer.responsepojos;

import com.ccm.common.responsepojos.ClientMapResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerMappedClient {

    private Integer customerClientMapId;

    private Integer customerId;

    private String customerName;

    private ClientMapResponse mappedClient;
}
