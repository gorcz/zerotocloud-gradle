/*
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package com.netflix.gradle.plugins.cloud.eureka.services

import com.netflix.gradle.plugins.cloud.services.AbstractHttpResourceService
import com.netflix.gradle.plugins.cloud.eureka.EurekaExtension

class EurekaService extends AbstractHttpResourceService {
  final EurekaExtension extension

  EurekaService(EurekaExtension ext) {
    super(ext)
    this.extension = ext
  }

  @Override
  protected String getName() {
    "eureka"
  }

  @Override
  protected String getInstanceType() {
    extension.getInstanceType()
  }

  @Override
  protected String getSecurityGroup() {
    extension.getSecurityGroup()
  }
}
