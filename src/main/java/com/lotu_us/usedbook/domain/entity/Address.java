package com.lotu_us.usedbook.domain.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Address {

    @NotBlank
    private String postcode;

    @NotBlank
    private String defaultAddress;

    private String detailAddress;

    private String extraAddress;

}
