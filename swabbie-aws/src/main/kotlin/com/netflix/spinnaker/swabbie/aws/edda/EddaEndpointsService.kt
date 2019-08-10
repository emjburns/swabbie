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

package com.netflix.spinnaker.swabbie.aws.edda

import retrofit.http.GET

/**
 * This is a service for discovering which edda endpoints are available.
 */
interface EddaEndpointsService {
  @GET("/api/v1/edda/endpoints.json")
  fun getEddaEndpoints(): EddaEndpointsContainer

  data class EddaEndpointsContainer(
    val edda_endpoints: EddaEndpoints
  ) {
    data class EddaEndpoints(
      val by_account: List<String>?,
      val by_cluster: List<String>?
    )

    fun get(): List<String> = edda_endpoints.by_account ?: emptyList()
  }
}
