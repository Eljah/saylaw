package com.github.eljah.saylaw.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilya on 08.05.18.
 */
public class DenominatorFind {
    public List<FloatNominatorDenominator> allShares=new ArrayList();

    public void process()
    {
        Double sum=0d;
        Integer sumInt=0;
        Integer commonFactor=1;



        while (Math.abs(tryCalculateFractional(allShares,sum,commonFactor)-tryCalculateInteger(allShares,sumInt,commonFactor))>0.0001)
        {
            commonFactor=commonFactor*10;
        }
        sum=tryCalculateFractional(allShares,sum,commonFactor);
        sumInt=tryCalculateInteger(allShares,sumInt,commonFactor);

        Double sumOfFractionals=0d;

        for (FloatNominatorDenominator current: allShares)
        {
            current.doubleFractional=((double)current.floatValue*commonFactor)/sum;
            sumOfFractionals=sumOfFractionals+current.doubleFractional;
            current.denominatorValue=(int)(sumInt);
            current.nominatorValue=(int)(current.floatValue*commonFactor);
            System.out.println(current);
        }
        System.out.println(sumOfFractionals);

        for (FloatNominatorDenominator current: allShares) {
           int gcd=gcd2(current.nominatorValue, current.denominatorValue);
           System.out.println(gcd);
           current.nominatorValue=current.nominatorValue/gcd;
           current.denominatorValue=current.denominatorValue/gcd;
           System.out.println(current);
        }
    }

    // non-recursive implementation
    private int gcd2(int p, int q) {
        while (q != 0) {
            int temp = q;
            q = p % q;
            p = temp;
        }
        return p;
    }

    private Integer tryCalculateInteger(List<FloatNominatorDenominator> listShares, Integer sum, Integer commonFactor) {
        for (FloatNominatorDenominator current : listShares) {
            sum = sum + (int) (current.getFloatValue() * commonFactor);
            System.out.println(current);
        }
        System.out.println(sum);
        return sum;
    }

    private Double tryCalculateFractional(List<FloatNominatorDenominator> listShares, Double sum, Integer commonFactor) {
        for (FloatNominatorDenominator current : listShares) {
            sum = sum + (current.getFloatValue() * commonFactor);
            System.out.println(current);
        }
        System.out.println(sum);
        return sum;
    }

}
