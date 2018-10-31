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
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import javax.net.ssl.SSLEngine;
import lombok.AllArgsConstructor;

/**
 * Created on 2018/10/30 5:03 PM
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=miroku">Ivan97</a>
 */
@AllArgsConstructor
public class SslChannelInitializer extends ChannelInitializer<Channel> {

  private final SslContext sslContext;
  private final boolean startTls;

  @Override
  protected void initChannel(Channel ch) throws Exception {
    SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
    ch.pipeline().addFirst("ssl", new SslHandler(sslEngine, startTls));
  }
}
