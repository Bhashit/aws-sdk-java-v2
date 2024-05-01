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

package software.amazon.awssdk.migration.internal.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Option;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.tree.J;
import org.openrewrite.marker.SearchResult;
import software.amazon.awssdk.annotations.SdkInternalApi;

/**
 * Add a comment to a method
 */
@SdkInternalApi
public class AddCommentToMethod extends Recipe {
    private static final String COMMENT_PREFIX = "AWS SDK for Java v2 migration: ";

    @Option(displayName = "Method pattern",
        description = "A method pattern that is used to find matching method invocations.",
        example = "org.mockito.Matchers anyVararg()")
    private final String methodPattern;

    @Option(displayName = "Comment",
        description = "A comment to add to this method.",
        example = "This method is not supported in AWS SDK for Java v2.")
    private final String comment;

    @JsonCreator
    public AddCommentToMethod(@JsonProperty("methodPattern") String methodPattern,
                              @JsonProperty("comment") String comment) {
        this.methodPattern = methodPattern;
        this.comment = comment;
    }

    @Override
    public String getDisplayName() {
        return "Add a comment to a method";
    }

    @Override
    public String getDescription() {
        return "Add a comment to a method.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new Visitor(methodPattern, comment);
    }

    private static final class Visitor extends JavaIsoVisitor<ExecutionContext> {
        private final MethodMatcher methodMatcher;
        private final String comment;

        Visitor(String methodPattern, String comment) {
            this.methodMatcher = new MethodMatcher(methodPattern, false);
            this.comment = COMMENT_PREFIX + comment;
        }

        @Override
        public J.MethodInvocation visitMethodInvocation(J.MethodInvocation methodInvocation, ExecutionContext executionContext) {
            J.MethodInvocation method = super.visitMethodInvocation(methodInvocation, executionContext);

            if (!methodMatcher.matches(method)) {
                return method;
            }

            return SearchResult.found(method, comment);
        }
    }
}
