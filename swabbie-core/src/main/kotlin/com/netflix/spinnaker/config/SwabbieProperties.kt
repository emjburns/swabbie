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

package com.netflix.spinnaker.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("swabbie")
open class SwabbieProperties {
  var enabled: Boolean = false
  var dryRun: Boolean = false
  var globalExclusions: MutableList<Exclusion>? = mutableListOf()
  var providers: List<CloudProviderConfiguration> = mutableListOf()
}

class CloudProviderConfiguration {
  var name: String = "aws"
  var locations: List<String> = mutableListOf()
  var exclusions: List<Exclusion>? = mutableListOf()
  var resourceTypes: List<ResourceTypeConfiguration> = mutableListOf()
  var attributes: List<Attribute> = mutableListOf()
  override fun toString(): String {
    return "CloudProviderConfiguration(name='$name', exclusions=$exclusions, resourceTypes=$resourceTypes, attributes=$attributes)"
  }
}

class Exclusion {
  var type: String = ""
  var comments: String = ""
  var attributes: List<Attribute> = mutableListOf()
  override fun toString(): String {
    return "Exclusion(type='$type', comments='$comments', attributes=$attributes)"
  }
}

class ResourceTypeConfiguration {
  var enabled: Boolean = true
  var dryRun: Boolean = false
  var name: String = ""
  var retention: Retention = Retention()
  var exclusions: MutableList<Exclusion>? = mutableListOf()
}

class Retention {
  var days: Int = 0
  var ageThresholdDays: Int = 0
  constructor(days: Int = 14, ageThresholdDays: Int = 14) {
    this.days = days
    this.ageThresholdDays = ageThresholdDays
  }

  override fun toString(): String {
    return "Retention(days=$days, ageThresholdDays=$ageThresholdDays)"
  }
}

class Attribute {
  var key: String = ""
  var value: List<String> = mutableListOf()
  override fun toString(): String {
    return "Attribute(key='$key', value=$value)"
  }

}

enum class ExclusionType { Literal, Account, Tag }

internal fun mergeExclusions(global: MutableList<Exclusion>?, local: MutableList<Exclusion>?): List<Exclusion> {
  if (global == null && local == null) {
    return emptyList()
  } else if (global == null && local != null) {
    return local
  } else if (global != null && local == null) {
    return global
  }

  // TODO: local is additive to global. local can override global
  return HashSet(global!! + local!!).toList()
}
