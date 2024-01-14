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

package org.springframework.cloud.client.actuator;

/**
 * @author Spencer Gibb
 */
public class NamedFeature {

	private final String name;

	private final Class<?> type;

	public NamedFeature(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public Class<?> getType() {
		return this.type;
	}

}
