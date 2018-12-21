package tech.iooo.boot.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2018-12-21 16:39
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class IdelStateHandlerInitializer extends ChannelInitializer<Channel> {

  private IdleStateHandler idleStateHandler;

  public IdelStateHandlerInitializer(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
    this.idleStateHandler = new IdleStateHandler((long) readerIdleTimeSeconds, (long) writerIdleTimeSeconds, (long) allIdleTimeSeconds, TimeUnit.SECONDS);
  }

  public IdelStateHandlerInitializer(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
    this.idleStateHandler = new IdleStateHandler(false, readerIdleTime, writerIdleTime, allIdleTime, unit);
  }

  public IdelStateHandlerInitializer(boolean observeOutput, long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
    this.idleStateHandler = new IdleStateHandler(observeOutput, readerIdleTime, writerIdleTime, allIdleTime, unit);
  }


  @Override
  protected void initChannel(Channel channel) throws Exception {
    ChannelPipeline pipeline = channel.pipeline();
    pipeline.addLast("iooo-default-idle-state-handler", idleStateHandler);
    pipeline.addLast("iooo-default-heartbeat-handler", new HeartBeatHandler());
  }
}
