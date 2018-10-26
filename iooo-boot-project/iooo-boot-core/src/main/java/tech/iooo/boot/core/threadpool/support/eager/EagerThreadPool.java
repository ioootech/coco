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

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import tech.iooo.boot.core.URL;
import tech.iooo.boot.core.constants.IoooConstants;
import tech.iooo.boot.core.threadlocal.NamedInternalThreadFactory;
import tech.iooo.boot.core.threadpool.ThreadPool;
import tech.iooo.boot.core.threadpool.support.AbortPolicyWithReport;

/**
 * EagerThreadPool When the core threads are all in busy, create new thread instead of putting task into blocking queue.
 */
public class EagerThreadPool implements ThreadPool {

  @Override
  public Executor getExecutor(URL url) {
    String name = url.getParameter(IoooConstants.THREAD_NAME_KEY, IoooConstants.DEFAULT_THREAD_NAME);
    int cores = url.getParameter(IoooConstants.CORE_THREADS_KEY, IoooConstants.DEFAULT_CORE_THREADS);
    int threads = url.getParameter(IoooConstants.THREADS_KEY, Integer.MAX_VALUE);
    int queues = url.getParameter(IoooConstants.QUEUES_KEY, IoooConstants.DEFAULT_QUEUES);
    int alive = url.getParameter(IoooConstants.ALIVE_KEY, IoooConstants.DEFAULT_ALIVE);

    // init queue and executor
    TaskQueue<Runnable> taskQueue = new TaskQueue<Runnable>(queues <= 0 ? 1 : queues);
    EagerThreadPoolExecutor executor = new EagerThreadPoolExecutor(cores,
        threads,
        alive,
        TimeUnit.MILLISECONDS,
        taskQueue,
        new NamedInternalThreadFactory(name, true),
        new AbortPolicyWithReport(name, url));
    taskQueue.setExecutor(executor);
    return executor;
  }
}
