/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tech.iooo.boot.core.threadpool.support.cached;

import java.util.concurrent.ExecutorService;
import lombok.experimental.Delegate;
import tech.iooo.boot.core.threadpool.ThreadPool;
import tech.iooo.boot.core.threadpool.ThreadPoolConfig;

/**
 * This thread pool is self-tuned. Thread will be recycled after idle for one minute, and new thread will be created for the upcoming request.
 *
 * @author Ivan97
 * @see java.util.concurrent.Executors#newCachedThreadPool()
 */
public class CachedThreadPool implements ThreadPool {

  @Delegate
  private ThreadPoolConfig config = ThreadPoolConfig.DEFAULT_CONFIG;

  public ExecutorService executorService() {
    return executorService(config.setThreads(Integer.MAX_VALUE));
  }
}
