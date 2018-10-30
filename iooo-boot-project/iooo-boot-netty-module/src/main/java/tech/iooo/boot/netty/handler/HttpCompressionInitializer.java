/*
 * Copyright [2018] [IoooTech,Inc.]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package tech.iooo.boot.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.AllArgsConstructor;

/**
 * Created on 2018/10/30 5:15 PM
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=miroku">Ivan97</a>
 */
@AllArgsConstructor
public class HttpCompressionInitializer extends ChannelInitializer<Channel> {

  private final boolean client;

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    if (client) {
      pipeline.addLast("codec", new HttpClientCodec());
      pipeline.addLast("decompressor", new HttpContentDecompressor());
    } else {
      pipeline.addLast("codec", new HttpServerCodec());
      pipeline.addLast("compressor", new HttpContentCompressor());
    }
  }
}
