# Overview

Configures the broker to SOFT_FAIL when OCSP is not available during client certificate authentication.

# Prerequisite

Must set `ocsp.enable=true` in jvm's `conf/security/java.security`.

# Installation

The plugin must be compiled
```
$ mvn clean install
```

And then copied to the broker installation directory's lib dir:
```
$ cp target/plugin-ocsp-soft-fail-1.0.0.jar ${ARTMIS_INSTALL_DIR}/lib/
```

Configure broker's acceptor to use the custom TrustManagerFactoryPlugin:
```
<acceptor name="artemis">tcp://0.0.0.0:61617?...sslEnabled=true;keyStorePath=/tmp/jks/localhost.jks;keyStorePassword=mypassword;needClientAuth=true;trustManagerFactoryPlugin=org.apache.activemq.artemis.plugin.netty.MyTrustManagerFactoryPlugin</acceptor>
```


# Run broker

Specify the trust manager factory truststore and java revocation checking parameters
```
export JAVA_ARGS_APPEND="-DmyTrustManager.trustStorePath=/tmp/jks/trust.jks -DmyTrustManager.trustStorePassword=foobar -Dcom.sun.net.ssl.checkRevocation=true -Dcom.sun.security.enableCRLDP=true"; bin/artemis run
```
