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

package org.springframework.cloud.client;

import org.junit.jupiter.api.Test;

import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author Dave Syer
 */
public class HostInfoEnvironmentPostProcessorTests {

	private HostInfoEnvironmentPostProcessor processor = new HostInfoEnvironmentPostProcessor();

	private ConfigurableEnvironment environment = new StandardEnvironment();

	@Test
	public void hostname() {
		this.processor.postProcessEnvironment(this.environment, new SpringApplication());
		then(this.environment.getProperty("spring.cloud.client.hostname")).isNotNull();
	}

	@Test
	public void ipAddress() {
		this.processor.postProcessEnvironment(this.environment, new SpringApplication());
		String address = this.environment.getProperty("spring.cloud.client.ip-address");
		then(address).isNotNull();
	}

}
