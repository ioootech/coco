package tech.iooo.boot.netty.handler;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import tech.iooo.boot.core.io.Resources;

/**
 * Created on 2018/9/3 下午4:17
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-netty-moomba">Ivan97</a>
 */
@UtilityClass
public class ContextSSLFactory {

  public static SSLContext getServerSslContext(String path, String password) {
    SSLContext serverSslContext = null;
    try {
      serverSslContext = SSLContext.getInstance("SSLv3");
    } catch (NoSuchAlgorithmException e1) {
      e1.printStackTrace();
    }
    try {
      if (getKeyManagersServer(path, password) != null && getTrustManagersServer(path, password) != null) {
        serverSslContext.init(getKeyManagersServer(path, password), getTrustManagersServer(path, password), null);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    serverSslContext.createSSLEngine().getSupportedCipherSuites();
    return serverSslContext;
  }

  public static SSLContext getClientSslContext(String path, String password) {
    SSLContext clientSslContext = null;
    try {
      clientSslContext = SSLContext.getInstance("SSLv3");
    } catch (NoSuchAlgorithmException e1) {
      e1.printStackTrace();
    }
    try {
      if (getKeyManagersClient(path, password) != null && getTrustManagersClient(path, password) != null) {
        clientSslContext.init(getKeyManagersClient(path, password), getTrustManagersClient(path, password), null);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    clientSslContext.createSSLEngine().getSupportedCipherSuites();
    return clientSslContext;
  }

  @SneakyThrows
  private static TrustManager[] getTrustManagersServer(String path, String password) {
    KeyStore ks = null;
    TrustManagerFactory keyFac = null;

    InputStream inputStream = Resources.getResourceAsStream(path);
    TrustManager[] kms = null;
    try {
      // 获得KeyManagerFactory对象. 初始化位默认算法  
      keyFac = TrustManagerFactory.getInstance("SunX509");
      ks = KeyStore.getInstance("JKS");
      ks.load(inputStream, password.toCharArray());
      keyFac.init(ks);
      kms = keyFac.getTrustManagers();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return kms;
  }

  @SneakyThrows
  private static TrustManager[] getTrustManagersClient(String path, String password) {
    KeyStore ks = null;
    TrustManagerFactory keyFac = null;

    InputStream inputStream = Resources.getResourceAsStream(path);

    TrustManager[] kms = null;
    try {
      // 获得KeyManagerFactory对象. 初始化位默认算法  
      keyFac = TrustManagerFactory.getInstance("SunX509");
      ks = KeyStore.getInstance("JKS");
      ks.load(inputStream, password.toCharArray());
      keyFac.init(ks);
      kms = keyFac.getTrustManagers();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return kms;
  }

  @SneakyThrows
  private static KeyManager[] getKeyManagersServer(String path, String password) {
    KeyStore ks = null;
    KeyManagerFactory keyFac = null;

    InputStream inputStream = Resources.getResourceAsStream(path);

    KeyManager[] kms = null;
    try {
      // 获得KeyManagerFactory对象. 初始化位默认算法  
      keyFac = KeyManagerFactory.getInstance("SunX509");
      ks = KeyStore.getInstance("JKS");
      ks.load(inputStream, password.toCharArray());
      keyFac.init(ks, password.toCharArray());
      kms = keyFac.getKeyManagers();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return kms;
  }

  @SneakyThrows
  private static KeyManager[] getKeyManagersClient(String path, String password) {
    KeyStore ks = null;
    KeyManagerFactory keyFac = null;

    InputStream inputStream = Resources.getResourceAsStream(path);

    KeyManager[] kms = null;
    try {
      // 获得KeyManagerFactory对象. 初始化位默认算法  
      keyFac = KeyManagerFactory.getInstance("SunX509");
      ks = KeyStore.getInstance("JKS");
      ks.load(inputStream, password.toCharArray());
      keyFac.init(ks, password.toCharArray());
      kms = keyFac.getKeyManagers();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return kms;
  }
}  
