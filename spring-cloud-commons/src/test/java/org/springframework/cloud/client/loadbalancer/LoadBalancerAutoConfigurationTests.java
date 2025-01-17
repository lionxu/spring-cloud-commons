/*
 * Copyright 2012-2023 the original author or authors.
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

package org.springframework.cloud.client.loadbalancer;

import java.util.List;

import org.springframework.cloud.test.ClassPathExclusions;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author Spencer Gibb
 * @author Olga Maciaszek-Sharma
 */
@ClassPathExclusions({ "spring-retry-*.jar", "spring-boot-starter-aop-*.jar" })
public class LoadBalancerAutoConfigurationTests extends AbstractLoadBalancerAutoConfigurationTests {

	@Override
	protected void assertLoadBalanced(RestTemplate restTemplate) {
		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
		then(interceptors).hasSize(1);
		ClientHttpRequestInterceptor interceptor = interceptors.get(0);
		then(interceptor).isInstanceOf(LoadBalancerInterceptor.class);
	}

	@Override
	protected void assertLoadBalanced(RestClient.Builder restClientBuilder) {
		restClientBuilder.requestInterceptors(interceptors -> {
			assertThat(interceptors).hasSize(1);
			assertThat(interceptors.get(0)).isInstanceOf(LoadBalancerInterceptor.class);
		});
	}

}
