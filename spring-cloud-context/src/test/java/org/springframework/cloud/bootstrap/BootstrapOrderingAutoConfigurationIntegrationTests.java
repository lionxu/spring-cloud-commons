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

package org.springframework.cloud.bootstrap;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.bootstrap.BootstrapOrderingAutoConfigurationIntegrationTests.Application;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(classes = Application.class,
		properties = { "encrypt.key:deadbeef", "spring.cloud.bootstrap.enabled=true" })
@ActiveProfiles("encrypt")
public class BootstrapOrderingAutoConfigurationIntegrationTests {

	@Autowired
	private ConfigurableEnvironment environment;

	@Test
	public void bootstrapPropertiesExist() {
		then(this.environment.getPropertySources().contains("applicationConfig: [classpath:/bootstrap.properties]"))
				.isTrue();
	}

	@Test
	public void normalPropertiesDecrypted() {
		then(this.environment.resolvePlaceholders("${foo}")).isEqualTo("foo");
	}

	@Test
	public void bootstrapPropertiesDecrypted() {
		then(this.environment.resolvePlaceholders("${bar}")).isEqualTo("bar");
	}

	@EnableAutoConfiguration
	@Configuration(proxyBeanMethods = false)
	protected static class Application {

	}

}
