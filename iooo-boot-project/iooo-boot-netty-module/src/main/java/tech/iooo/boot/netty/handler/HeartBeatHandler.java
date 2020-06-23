package tech.iooo.boot.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import java.util.Objects;

/**
 * Created on 2018-12-21 16:40
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

  private static final ByteBuf HEART_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("HEARTBEAT\n", CharsetUtil.UTF_8));

  private final Object heartBeatMsg;

  public HeartBeatHandler(Object heartBeatMsg) {
    if (Objects.nonNull(heartBeatMsg)) {
      this.heartBeatMsg = heartBeatMsg;
    } else {
      this.heartBeatMsg = HEART_SEQUENCE;
    }
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      Object msg = heartBeatMsg instanceof ByteBuf ? ((ByteBuf) heartBeatMsg).duplicate() : heartBeatMsg;
      ctx.writeAndFlush(msg).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    } else {
      super.userEventTriggered(ctx, evt);
    }
  }
}
