/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.hadoop.io;

import org.apache.hadoop.fs.FileSystem;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Spring ResourceLoader over Hadoop FileSystem.
 * 
 * @author Costin Leau
 */
public class HdfsResourceLoader implements ResourceLoader {

	private final FileSystem fs;

	public HdfsResourceLoader(FileSystem fs) {
		this.fs = fs;
	}

	public ClassLoader getClassLoader() {
		return fs.getConf().getClassLoader();
	}

	public Resource getResource(String location) {
		return new HdfsResource(location, fs);
	}
}
