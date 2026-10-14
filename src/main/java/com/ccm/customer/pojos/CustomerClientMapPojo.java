package com.ccm.customer.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerClientMapPojo {

    private Integer id;

    private Integer customerId;

    private Integer clientId;

    public String createdBy;

    public String modifiedBy;
}
