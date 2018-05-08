package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by ilya on 08.05.18.
 */

@ToString
@Setter
@Getter
public class FloatNominatorDenominator {
    Double doubleValue;
    Double doubleFractional;
    Integer nominatorValue;
    Integer denominatorValue;
    Object dtoForBinding;
}
