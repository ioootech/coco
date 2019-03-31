package tech.iooo.boot.netty.handler;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueDatagramChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.resolver.dns.DefaultDnsServerAddressStreamProvider;
import io.netty.resolver.dns.DnsAddressResolverGroup;
import java.util.Locale;

/**
 * Created on 2018-11-27 09:30
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class NettyAdaptiveHelper {

  private static final boolean EPOLL = Epoll.isAvailable();
  private static final boolean KQUEUE = KQueue.isAvailable();

  public static boolean isMac = System.getProperty("os.namePrefix").toLowerCase(Locale.US).contains("mac");
  public static boolean isWin = System.getProperty("os.namePrefix").toLowerCase(Locale.US).contains("win");
  public static boolean isLinux = System.getProperty("os.namePrefix").toLowerCase(Locale.US).contains("linux");

  public static DnsAddressResolverGroup resolverGroup =
      new DnsAddressResolverGroup(EPOLL ? EpollDatagramChannel.class : KQUEUE ? KQueueDatagramChannel.class : NioDatagramChannel.class,
          DefaultDnsServerAddressStreamProvider.INSTANCE);

  public static Class<? extends ServerSocketChannel> serverSocketChannel =
      EPOLL ? EpollServerSocketChannel.class : KQUEUE ? KQueueServerSocketChannel.class : NioServerSocketChannel.class;

  public static Class<? extends SocketChannel> clientSocketChannel = EPOLL ? EpollSocketChannel.class : KQUEUE ? KQueueSocketChannel.class : NioSocketChannel.class;


  public static EventLoopGroup bossEventLoopGroup = isMac ? new KQueueEventLoopGroup() : isLinux ? new EpollEventLoopGroup() : new NioEventLoopGroup();

  public static EventLoopGroup clientEventLoopGroup() {
    return isMac ? new KQueueEventLoopGroup() : isLinux ? new EpollEventLoopGroup() : new NioEventLoopGroup();
  }

  public static EventLoopGroup clientEventLoopGroup(int thread) {
    return isMac ? new KQueueEventLoopGroup(thread) : isLinux ? new EpollEventLoopGroup(thread) : new NioEventLoopGroup(thread);
  }
}
