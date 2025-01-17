/*
 * Copyright 2013-2020 the original author or authors.
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

package org.springframework.cloud.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.NoneNestedConditions;
import org.springframework.context.annotation.Conditional;

import static org.springframework.cloud.util.PropertyUtils.BOOTSTRAP_ENABLED_PROPERTY;
import static org.springframework.cloud.util.PropertyUtils.MARKER_CLASS;
import static org.springframework.cloud.util.PropertyUtils.USE_LEGACY_PROCESSING_PROPERTY;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ConditionalOnBootstrapDisabled.OnBootstrapDisabledCondition.class)
public @interface ConditionalOnBootstrapDisabled {

	class OnBootstrapDisabledCondition extends NoneNestedConditions {

		OnBootstrapDisabledCondition() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnClass(name = MARKER_CLASS)
		static class OnBootstrapMarkerClassPresent {

		}

		@ConditionalOnProperty(name = USE_LEGACY_PROCESSING_PROPERTY)
		static class OnUseLegacyProcessingEnabled {

		}

		@ConditionalOnProperty(name = BOOTSTRAP_ENABLED_PROPERTY)
		static class OnBootstrapEnabled {

		}

	}

}
