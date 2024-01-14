/*
 * Copyright 2012-2022 the original author or authors.
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

package org.springframework.cloud.loadbalancer.aot;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.aot.AotDetector;
import org.springframework.aot.test.generate.TestGenerationContext;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.context.annotation.UserConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.config.LoadBalancerAutoConfiguration;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.aot.ApplicationContextAotGenerator;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.test.tools.CompileWithForkedClassLoader;
import org.springframework.core.test.tools.TestCompiler;
import org.springframework.javapoet.ClassName;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link LoadBalancerChildContextInitializer}.
 *
 * @author Olga Maciaszek-Sharma
 */
@ExtendWith(OutputCaptureExtension.class)
public class LoadBalancerChildContextInitializerTests {

	private static final Log LOG = LogFactory.getLog(LoadBalancerChildContextInitializerTests.class);

	@BeforeEach
	@AfterEach
	void reset() {
		ReflectionTestUtils.setField(TomcatURLStreamHandlerFactory.class, "instance", null);
		ReflectionTestUtils.setField(URL.class, "factory", null);
	}

	@Test
	@CompileWithForkedClassLoader
	@SuppressWarnings("unchecked")
	void shouldStartLBChildContextsFromAotContributions(CapturedOutput output) {
		WebApplicationContextRunner contextRunner = new WebApplicationContextRunner(
				AnnotationConfigServletWebApplicationContext::new)
						.withConfiguration(AutoConfigurations.of(ServletWebServerFactoryAutoConfiguration.class,
								LoadBalancerAutoConfiguration.class))
						.withConfiguration(UserConfigurations.of(TestLoadBalancerConfiguration.class));
		contextRunner.withPropertyValues("spring.cloud.loadbalancer.eager-load.clients[0]=test1").prepare(context -> {
			TestGenerationContext generationContext = new TestGenerationContext(TestTarget.class);
			ClassName className = new ApplicationContextAotGenerator().processAheadOfTime(
					(GenericApplicationContext) context.getSourceApplicationContext(), generationContext);
			generationContext.writeGeneratedContent();
			TestCompiler compiler = TestCompiler.forSystem();
			compiler.with(generationContext).compile(compiled -> {
				ServletWebServerApplicationContext freshApplicationContext = new ServletWebServerApplicationContext();
				ApplicationContextInitializer<GenericApplicationContext> initializer = compiled
						.getInstance(ApplicationContextInitializer.class, className.toString());
				initializer.initialize(freshApplicationContext);
				assertThat(output).contains("Refreshing LoadBalancerClientFactory-test1",
						"Refreshing LoadBalancerClientFactory-test-2", "Refreshing LoadBalancerClientFactory-test_3");
				assertThat(output).doesNotContain("Instantiating bean from Test 2 custom config",
						"Instantiating bean from default custom config");
				TestPropertyValues.of(AotDetector.AOT_ENABLED + "=true")
						.applyToSystemProperties(freshApplicationContext::refresh);
				WebClient webClient = freshApplicationContext.getBean(WebClient.class);
				webClient.get().uri(URI.create("http://test-2/")).retrieve().bodyToMono(String.class).subscribe();
				assertThat(output).contains("Instantiating bean from Test 2 custom config",
						"Instantiating bean from default custom config");
			});
		});

	}

	static class TestTarget {

	}

	@Configuration(proxyBeanMethods = false)
	@LoadBalancerClients(value = { @LoadBalancerClient(value = "test-2", configuration = Test2Configuration.class),
			@LoadBalancerClient("test_3") }, defaultConfiguration = DefaultConfiguration.class)
	public static class TestLoadBalancerConfiguration {

		@Bean
		ReactorLoadBalancerExchangeFilterFunction exchangeFilterFunction(
				LoadBalancerClientFactory loadBalancerClientFactory) {
			return new ReactorLoadBalancerExchangeFilterFunction(loadBalancerClientFactory, new ArrayList<>());
		}

		@Bean
		WebClient webClient(ReactorLoadBalancerExchangeFilterFunction lbFunction) {
			return WebClient.builder().filter(lbFunction).build();
		}

	}

	public static class Test2Configuration {

		@Bean
		TestBean testBean() {
			LOG.debug("Instantiating bean from Test 2 custom config");
			return new TestBean();
		}

	}

	public static class DefaultConfiguration {

		@Bean
		TestBean defaultTestBean() {
			LOG.debug("Instantiating bean from default custom config");
			return new TestBean();
		}

	}

	public static class TestBean {

	}

}
