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

package tech.iooo.boot.core.threadpool.support.eager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import tech.iooo.boot.core.threadlocal.NamedInternalThreadFactory;
import tech.iooo.boot.core.threadpool.ThreadPool;
import tech.iooo.boot.core.threadpool.ThreadPoolConfig;
import tech.iooo.boot.core.threadpool.support.AbortPolicyWithReport;

/**
 * EagerThreadPool When the core threads are all in busy, create new thread instead of putting task into blocking queue.
 *
 * @author Ivan97
 */
public class EagerThreadPool implements ThreadPool {

  @Override
  public ExecutorService executorService(ThreadPoolConfig config) {
    // init queue and executor
    TaskQueue<Runnable> taskQueue = new TaskQueue<>(config.getQueues() <= 0 ? 1 : config.getQueues());
    EagerThreadPoolExecutor executor = new EagerThreadPoolExecutor(config.getCores(),
        config.getThreads(),
        config.getAlive(),
        TimeUnit.MILLISECONDS,
        taskQueue,
        new NamedInternalThreadFactory(config.getNamePrefix(), config.isDaemon()),
        new AbortPolicyWithReport());
    taskQueue.setExecutor(executor);
    return executor;
  }

  public ExecutorService executorService() {
    return executorService(ThreadPoolConfig.DEFAULT_CONFIG.setThreads(Integer.MAX_VALUE));
  }
}
