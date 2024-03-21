package org.apache.activemq.artemis.plugin.netty;

import org.apache.activemq.artemis.api.core.TrustManagerFactoryPlugin;

import javax.net.ssl.CertPathTrustManagerParameters;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertPathBuilder;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.X509CertSelector;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyTrustManagerFactoryPlugin implements TrustManagerFactoryPlugin {

    private Logger logger = Logger.getLogger(MyTrustManagerFactoryPlugin.class.getName());

    @Override
    public TrustManagerFactory getTrustManagerFactory() {

        TrustManagerFactory tmf = null;
        try {


            String tsPath = System.getProperty("myTrustManager.trustStorePath");
            String tsPassword = System.getProperty("myTrustManager.trustStorePassword");
            logger.info("configuring custom trust manager factory using truststore: " + tsPath);
            FileInputStream tsFile = new FileInputStream(tsPath);
            KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
            ts.load(tsFile, tsPassword.toCharArray());

            tmf = TrustManagerFactory.getInstance("PKIX");
            CertPathBuilder cpb = CertPathBuilder.getInstance("PKIX");
            PKIXRevocationChecker rc = (PKIXRevocationChecker) cpb.getRevocationChecker();
            rc.setOptions(EnumSet.of(PKIXRevocationChecker.Option.SOFT_FAIL));

            PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(ts, new X509CertSelector());
            pkixParams.addCertPathChecker(rc);

            tmf.init( new CertPathTrustManagerParameters(pkixParams) );

        } catch (Exception e) {
            logger.log(Level.SEVERE, "error configuring custom TrustManagerFactory: " + e.getMessage());
            System.exit(0);
        }

        return tmf;

    }


}
