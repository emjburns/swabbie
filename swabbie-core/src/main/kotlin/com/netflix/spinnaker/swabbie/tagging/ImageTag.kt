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

package com.netflix.spinnaker.swabbie.tagging

open class ImageTagsRequest(
  open val type: String,
  open val application: String,
  open val description: String
) : TagRequest

data class UpsertImageTagsRequest(
  val imageNames: Set<String>,
  val regions: Set<String>,
  val tags: Map<String, String>,
  val cloudProvider: String,
  val cloudProviderType: String,
  override val application: String,
  override val description: String
) : ImageTagsRequest("upsertImageTags", application, description)
