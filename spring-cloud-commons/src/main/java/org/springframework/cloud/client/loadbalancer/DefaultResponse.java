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

package org.springframework.cloud.client.loadbalancer;

import java.util.Objects;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.style.ToStringCreator;

/**
 * @author Spencer Gibb
 * @author Olga Maciaszek-Sharma
 */
public class DefaultResponse implements Response<ServiceInstance> {

	private final ServiceInstance serviceInstance;

	public DefaultResponse(ServiceInstance serviceInstance) {
		this.serviceInstance = serviceInstance;
	}

	@Override
	public boolean hasServer() {
		return this.serviceInstance != null;
	}

	@Override
	public ServiceInstance getServer() {
		return this.serviceInstance;
	}

	@Override
	public String toString() {
		ToStringCreator to = new ToStringCreator(this);
		to.append("serviceInstance", serviceInstance);
		return to.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DefaultResponse that)) {
			return false;
		}
		return Objects.equals(serviceInstance, that.serviceInstance);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceInstance);
	}

}
