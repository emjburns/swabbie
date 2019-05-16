/*
 * Copyright 2018 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.swabbie.agents

import com.netflix.spectator.api.NoopRegistry
import com.netflix.spinnaker.config.SwabbieProperties
import com.netflix.spinnaker.kork.dynamicconfig.DynamicConfigService
import com.netflix.spinnaker.swabbie.NoopCacheStatus
import com.netflix.spinnaker.swabbie.ResourceTypeHandler
import com.netflix.spinnaker.swabbie.ResourceTypeHandlerTest.workConfiguration
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argWhere
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Test
import java.time.Clock

object NotificationAgentTest {
  private val clock = Clock.systemDefaultZone()
  private val onCompleteCallback = {}
  private val configuration = workConfiguration()
  private val agentExecutor = BlockingThreadExecutor()
  private val swabbieProperties = SwabbieProperties()
  private val cacheStatus = NoopCacheStatus()
  private val dynamicConfigService = mock<DynamicConfigService>()

  @Test
  fun `should not notify if no handler found`() {
    val resourceTypeHandler = mock<ResourceTypeHandler<*>>()
    whenever(resourceTypeHandler.handles(configuration)) doReturn false

    NotificationAgent(
      registry = NoopRegistry(),
      clock = clock,
      resourceTypeHandlers = listOf(resourceTypeHandler),
      workConfigurations = listOf(configuration),
      agentExecutor = agentExecutor,
      swabbieProperties = swabbieProperties,
      cacheStatus = cacheStatus,
      dynamicConfigService = dynamicConfigService
    ).process(configuration, onCompleteCallback)

    verify(resourceTypeHandler, never()).notify(any(), any())
  }

  @Test
  fun `should notify`() {
    val resourceTypeHandler = mock<ResourceTypeHandler<*>>()

    whenever(resourceTypeHandler.handles(configuration)) doReturn true
    NotificationAgent(
      registry = NoopRegistry(),
      resourceTypeHandlers = listOf(resourceTypeHandler),
      clock = clock,
      workConfigurations = listOf(configuration),
      agentExecutor = agentExecutor,
      swabbieProperties = swabbieProperties,
      cacheStatus = cacheStatus,
      dynamicConfigService = dynamicConfigService
    ).process(configuration, onCompleteCallback)

    verify(resourceTypeHandler, times(1)).notify(
      argWhere { it == configuration }, any()
    )
  }
}
