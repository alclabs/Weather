package com.controlj.addon.weather;

import com.controlj.green.addonsupport.LicensedFeatures;

/**
 *
 */
public class Licensing {
    private static final boolean licensed = LicensedFeatures.hasFeature(LicensedFeatures.FeaturePackage.Enterprise);

    public static boolean isLicensed()
    {
        return licensed;
    }

}
