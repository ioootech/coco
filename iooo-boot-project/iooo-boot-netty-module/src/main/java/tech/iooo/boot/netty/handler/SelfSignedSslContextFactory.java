package tech.iooo.boot.netty.handler;

import io.netty.handler.ssl.OpenSsl;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.iooo.boot.core.utils.NetUtils;

/**
 * Created on 2018-12-21 10:08
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class SelfSignedSslContextFactory {

  private static final Logger logger = LoggerFactory.getLogger(SelfSignedSslContextFactory.class);


  private SelfSignedCertificate ssc;

  private SelfSignedSslContextFactory(String hostname) {
    try {
      ssc = new SelfSignedCertificate(hostname);
    } catch (CertificateException e) {
      e.printStackTrace();
    }
  }

  public static SelfSignedSslContextFactory forHost(String host) {
    return new SelfSignedSslContextFactory(host);
  }

  public static SelfSignedSslContextFactory forLocalhost() {
    return new SelfSignedSslContextFactory(NetUtils.getLocalHost());
  }

  public SslContext asServer() throws CertificateException, SSLException {
    if (logger.isDebugEnabled()) {
      logger.debug("OpenSsl available: {}", OpenSsl.isAvailable());
    }
    SslContextBuilder sslContextBuilder;
    if (OpenSsl.isAvailable()) {
      sslContextBuilder = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).sslProvider(SslProvider.OPENSSL);
    } else {
      sslContextBuilder = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).sslProvider(SslProvider.JDK);
    }
    return sslContextBuilder.build();
  }

  public SslContext asClient(boolean auth) throws CertificateException, SSLException {
    if (logger.isDebugEnabled()) {
      logger.debug("OpenSsl available: {}", OpenSsl.isAvailable());
    }
    SslContextBuilder sslContextBuilder;
    if (OpenSsl.isAvailable()) {
      if (auth) {
        sslContextBuilder = SslContextBuilder.forClient().keyManager(ssc.certificate(), ssc.privateKey()).sslProvider(SslProvider.OPENSSL);
      } else {
        sslContextBuilder = SslContextBuilder.forClient().sslProvider(SslProvider.OPENSSL);
      }
    } else {
      if (auth) {
        sslContextBuilder = SslContextBuilder.forClient().keyManager(ssc.certificate(), ssc.privateKey()).sslProvider(SslProvider.JDK);
      } else {
        sslContextBuilder = SslContextBuilder.forClient().sslProvider(SslProvider.JDK);
      }
    }
    return sslContextBuilder.build();
  }

}
