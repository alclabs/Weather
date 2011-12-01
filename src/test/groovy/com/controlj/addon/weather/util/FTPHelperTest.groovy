package com.controlj.addon.weather.util

import spock.lang.Specification

/**
 * 
 */
class FTPHelperTest extends Specification {
    def normal() {
        when:
        File temp = new File("metar.txt");
        FileOutputStream out = new FileOutputStream(temp);
        try {
            FTPHelper.getTextFile("tgftp.nws.noaa.gov", "data/observations/metar/stations", "KRYY.TXT",
                    "anonymous", "", out);
        } catch (Throwable e) {
            e.printStackTrace()
        } finally {
            out.close()
        }

        then:
        temp.exists()
    }
}
