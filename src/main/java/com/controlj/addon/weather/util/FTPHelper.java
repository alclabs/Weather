package com.controlj.addon.weather.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 *
 */
public class FTPHelper {

    public static void getTextFile(String server, String dir, String fileName, String user, String pass, OutputStream out) throws IOException {
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(server);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new IOException("FTP server refused connection with '"+ftp.getReplyString()+"'");
            }
            if (!ftp.login(user, pass)) {
                throw new IOException("FTP server refused login");
            }
            ftp.changeWorkingDirectory(dir);
            if (!ftp.retrieveFile(fileName, out)) {
                throw new IOException("File not retrieved - reply:"+ftp.getReplyString());
            }
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {} // exception while disconnecting, ignore
            }
        }
    }
}
