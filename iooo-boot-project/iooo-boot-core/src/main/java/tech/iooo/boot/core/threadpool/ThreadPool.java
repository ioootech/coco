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
package tech.iooo.boot.core.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import tech.iooo.boot.core.threadlocal.NamedInternalThreadFactory;
import tech.iooo.boot.core.threadpool.support.AbortPolicyWithReport;

/**
 * ThreadPool
 *
 * @author Ivan97
 */
public interface ThreadPool {

  /**
   * Scheduled Executor Service
   *
   * @param config ThreadPoolConfig contains thread parameter
   * @return scheduled thread pool
   */
  static ScheduledExecutorService scheduledExecutorService(ThreadPoolConfig config) {
    return new ScheduledThreadPoolExecutor(config.getCores(),
        new BasicThreadFactory.Builder().namingPattern(config.getNamePrefix())
            .daemon(config.isDaemon()).build());
  }

  /**
   * Thread pool
   *
   * @param config ThreadPoolConfig contains thread parameter
   * @return thread pool
   */
  default ExecutorService executorService(ThreadPoolConfig config) {
    return new ThreadPoolExecutor(config.getCores(), config.getThreads(), config.getAlive(), TimeUnit.MILLISECONDS,
        config.getQueues() == 0 ? new SynchronousQueue<>() :
            (config.getQueues() < 0 ? new LinkedBlockingQueue<>()
                : new LinkedBlockingQueue<>(config.getQueues())),
        new NamedInternalThreadFactory(config.getNamePrefix(), config.isDaemon()), new AbortPolicyWithReport());
  }

}
