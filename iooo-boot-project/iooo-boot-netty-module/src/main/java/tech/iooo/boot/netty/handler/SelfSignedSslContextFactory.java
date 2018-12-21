package tech.iooo.boot.netty.handler;

import io.netty.handler.ssl.OpenSsl;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLException;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2018-12-21 10:08
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@UtilityClass
public class SelfSignedSslContextFactory {

  private static final Logger logger = LoggerFactory.getLogger(SelfSignedSslContextFactory.class);

  public static SslContext sslContext() throws CertificateException, SSLException {
    if (logger.isInfoEnabled()) {
      logger.info("OpenSsl available: {}", OpenSsl.isAvailable());
    }
    SelfSignedCertificate ssc = new SelfSignedCertificate();
    SslContextBuilder sslContextBuilder;
    if (OpenSsl.isAvailable()) {
      sslContextBuilder = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).sslProvider(SslProvider.OPENSSL);
    } else {
      sslContextBuilder = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).sslProvider(SslProvider.JDK);
    }
    return sslContextBuilder.build();
  }
}
