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



package com.netflix.gradle.plugins.cloud.asgard.tasks

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient
import com.amazonaws.services.identitymanagement.model.AddRoleToInstanceProfileRequest
import com.amazonaws.services.identitymanagement.model.CreateInstanceProfileRequest
import com.amazonaws.services.identitymanagement.model.CreateRoleRequest
import com.amazonaws.services.identitymanagement.model.PutRolePolicyRequest
import com.netflix.gradle.plugins.cloud.model.CloudInitializer
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class AsgardIamRoleInitializer extends DefaultTask implements CloudInitializer {
  AmazonIdentityManagement iam
  def String assumeRolePolicyDoc
  def String iamPolicyDoc
  def String iamRole
  def String instanceProfileName

  AsgardIamRoleInitializer() {
    this.iam = new AmazonIdentityManagementClient()
  }

  @Override
  @TaskAction
  void init() {
    create()
  }

  @Override
  void create() {
    try {
      def req = new CreateInstanceProfileRequest(instanceProfileName: getInstanceProfileName())
      iam.createInstanceProfile(req)
    } catch (AmazonServiceException e) {
      if (e.statusCode == 409) {
        println "Asgard Instance Profile already exists, continuing..."
      } else {
        throw e
      }
    }
    try {
      def req = new CreateRoleRequest(roleName: getIamRole()).withAssumeRolePolicyDocument(getAssumeRolePolicyDoc())
      iam.createRole(req)
    } catch (AmazonServiceException e) {
      if (e.statusCode == 409) {
        println "Asgard AssumeRole policy already exists, continuing..."
      } else {
        throw e
      }
    }
    try {
      def req = new PutRolePolicyRequest(roleName: getIamRole(), policyName: "${getIamRole()}_Policy",
          policyDocument: getIamPolicyDoc())
      iam.putRolePolicy(req)
    } catch (AmazonServiceException e) {
      if (e.statusCode == 409) {
        println "Asgard IAM Policy already exists, continuing..."
      } else {
        throw e
      }
    }
    try {
      def req = new AddRoleToInstanceProfileRequest(instanceProfileName: getInstanceProfileName(),
          roleName: getIamRole())
      iam.addRoleToInstanceProfile(req)
    } catch (AmazonServiceException e) {
      if (e.statusCode == 409) {
        println "Asgard Instance Profile already associated with Base IAM Role"
      } else {
        throw e
      }
    }
  }

}
