/*
 * Copyright 2013-2021 the original author or authors.
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

package org.springframework.cloud.context.test;

import java.util.HashMap;
import java.util.Objects;

import org.springframework.boot.context.config.ConfigDataResource;

public class TestConfigDataResource extends ConfigDataResource {

	final HashMap<String, Object> props;

	public TestConfigDataResource(HashMap<String, Object> props) {
		this.props = props;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TestConfigDataResource that = (TestConfigDataResource) o;
		return Objects.equals(this.props, that.props);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.props);
	}

}
