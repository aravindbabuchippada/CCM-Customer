package com.ccm.customer.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPojo {

    private Integer id;

    private String customerName;

    @Builder.Default
    private Integer active = 1;

    private String createdBy;

    private String modifiedBy;
}
