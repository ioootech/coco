package tech.iooo.boot.netty.handler;

import com.google.protobuf.MessageLite;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.AllArgsConstructor;

/**
 * Created on 2018/10/30 5:42 PM
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@AllArgsConstructor
public class ProtoBufInitializer extends ChannelInitializer<Channel> {

  private final MessageLite messageLite;

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast(new ProtobufVarint32FrameDecoder());
    pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
    pipeline.addLast(new ProtobufEncoder());
    pipeline.addLast(new ProtobufDecoder(messageLite));
  }
}
