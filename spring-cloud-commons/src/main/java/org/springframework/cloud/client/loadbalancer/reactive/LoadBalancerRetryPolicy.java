/*
 * Copyright 2012-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.client.loadbalancer.reactive;

import org.springframework.cglib.core.internal.Function;
import org.springframework.http.HttpMethod;

/**
 * Pluggable policy used to establish whether a given load-balanced call should be
 * retried.
 *
 * @author Olga Maciaszek-Sharma
 * @since 3.0.0
 */
public interface LoadBalancerRetryPolicy {

	/**
	 * Return <code>true</code> to retry on the same service instance.
	 * @param context the context for the retry operation
	 * @return true to retry on the same service instance
	 */
	boolean canRetrySameServiceInstance(LoadBalancerRetryContext context);

	/**
	 * Return <code>true</code> to retry on the next service instance.
	 * @param context the context for the retry operation
	 * @return true to retry on the same service instance
	 */
	boolean canRetryNextServiceInstance(LoadBalancerRetryContext context);

	/**
	 * Return <code>true</code> to retry on the provided HTTP status code.
	 * @param statusCode the HTTP status code
	 * @return true to retry on the provided HTTP status code
	 */
	boolean retryableStatusCode(int statusCode);

	/**
	 * Return <code>true</code> to retry if the provided exception is thrown.
	 * @param exception the {@link Throwable} to evaluate
	 * @return true to retry on the provided exception
	 */
	boolean retryableException(Throwable exception);

	/**
	 * Return <code>true</code> to retry on the provided HTTP method.
	 * @param method the HTTP request method
	 * @return true to retry on the provided HTTP method
	 */
	boolean canRetryOnMethod(HttpMethod method);

	interface Factory extends Function<String, LoadBalancerRetryPolicy> {

	}

}
