package com.github.eljah.saylaw.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ilya on 08.05.18.
 */
public class DenominatorFind {
    public List<FloatNominatorDenominator> allShares=new ArrayList();

    public void processByFractional()
    {
        Double sum=0d;
        Integer sumInt=0;
        Integer commonFactor=1;

        while (Math.abs(tryCalculateFractional(allShares,sum,commonFactor)-tryCalculateInteger(allShares,sumInt,commonFactor))>0.001)
        {
            commonFactor=commonFactor*10;
        }
        sum=tryCalculateFractional(allShares,sum,commonFactor);
        sumInt=tryCalculateInteger(allShares,sumInt,commonFactor);

        Double sumOfFractionals=0d;

        for (FloatNominatorDenominator current: allShares)
        {
            current.doubleFractional=((double)current.getDoubleValue()*commonFactor)/sum;
            sumOfFractionals=sumOfFractionals+current.doubleFractional;
            current.denominatorValue=(int)(sumInt);
            current.nominatorValue=(int)(current.getDoubleValue()*commonFactor);
            System.out.println(current);
        }
        System.out.println(sumOfFractionals);

        //for (FloatNominatorDenominator current: allShares) {
        //   int gcd=gcd2(current.nominatorValue, current.denominatorValue);
        //   System.out.println(gcd);
        //   current.nominatorValue=current.nominatorValue/gcd;
        //   current.denominatorValue=current.denominatorValue/gcd;
        //   System.out.println(current);
        //}
    }

    public void processByNominator(HashSet<Integer> denominators)
    {
        Double sum=0d;
        Integer sumInt=0;
        Integer commonFactor=1;

        for (Integer denominator: denominators)
        {
            System.out.println("Denominator: "+denominator);
            for (FloatNominatorDenominator current: allShares) {
                if (gcd2(denominator,(int)current.denominatorValue)==denominator) {
                    current.denominatorValue = current.denominatorValue * (denominator);
                    current.nominatorValue = current.nominatorValue * (denominator);
                    System.out.println(denominator+" N "+current);
                }
                System.out.println(denominator+" Y "+current);
            }
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
            sum = sum + (int) (current.getDoubleValue() * commonFactor);
            System.out.println(current);
        }
        System.out.println(sum);
        return sum;
    }

    private Double tryCalculateFractional(List<FloatNominatorDenominator> listShares, Double sum, Integer commonFactor) {
        for (FloatNominatorDenominator current : listShares) {
            sum = sum + (current.getDoubleValue() * commonFactor);
            System.out.println(current);
        }
        System.out.println(sum);
        return sum;
    }

}
