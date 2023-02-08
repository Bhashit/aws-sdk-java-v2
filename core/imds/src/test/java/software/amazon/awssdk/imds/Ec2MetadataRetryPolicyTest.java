/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.imds;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.core.retry.backoff.BackoffStrategy;

class Ec2MetadataRetryPolicyTest {

    @Test
    void equals_hashCode() {
        BackoffStrategy backoffStrategy = BackoffStrategy.defaultStrategy();
        Ec2MetadataRetryPolicy policy = Ec2MetadataRetryPolicy.builder()
                                                              .numRetries(3)
                                                              .backoffStrategy(backoffStrategy)
                                                              .build();
        assertThat(policy).isEqualTo(Ec2MetadataRetryPolicy.builder()
                                                           .numRetries(3)
                                                           .backoffStrategy(backoffStrategy)
                                                           .build());
    }

    @Test
    void builder_setNumRetriesCorrectly() {
        Ec2MetadataRetryPolicy policy = Ec2MetadataRetryPolicy.builder()
                                                              .numRetries(3)
                                                              .build();
        assertThat(policy.numRetries()).isEqualTo(3);
    }
}
