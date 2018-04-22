package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by eljah32 on 4/22/2018.
 */

@Embeddable
@Setter
@Getter
@NoArgsConstructor
public class ExtractOfRegistry{

    private String ownershipCertificate;

    private String cadastralNumber;

    private String registryAddress;

    private String registryExtractFilename;

    private Date registryExtractDate;

}
