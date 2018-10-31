package tech.iooo.boot.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;
import java.io.FileInputStream;
import lombok.AllArgsConstructor;

/**
 * Created on 2018/10/30 5:33 PM
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@AllArgsConstructor
public class ChunckedWriteHandlerInitializer extends ChannelInitializer<Channel> {

  private final FileInputStream fileInputStream;

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast(new ChunkedWriteHandler());
    pipeline.addLast(new WriteStreamHandler());
  }

  public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      super.channelActive(ctx);
      ctx.writeAndFlush(new ChunkedStream(fileInputStream));
    }
  }
}
