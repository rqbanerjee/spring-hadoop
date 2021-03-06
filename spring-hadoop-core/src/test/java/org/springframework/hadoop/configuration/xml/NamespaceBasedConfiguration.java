/*
 * Copyright 2006-2011 the original author or authors.
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
package org.springframework.hadoop.configuration.xml;

import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hadoop.configuration.JobFactoryBean;

/**
 * Simple POJO configuration for the things that we can't (yet) configure using
 * the XML namespace
 * 
 * @author Josh Long
 * @author 1.0
 */
@Configuration
public class NamespaceBasedConfiguration {

	@Bean
	public FactoryBean<Job> classConfiguredJob(@Value("${input.path:src/test/resources/input}") String inputPath,
			@Value("${output.path:target/output}") String outputPath) throws Exception {
		JobFactoryBean factory = new JobFactoryBean();
		factory.setMapper(this.mapper);
		factory.setReducer(this.reducer);
		factory.setCombiner(this.reducer);
		factory.setOutputKeyClass(outputKeyType());
		factory.setOutputValueClass(outputValueType());
		factory.setInputPaths(inputPath);
		factory.setOutputPath(outputPath);
		return factory;
	}

	protected Class<IntWritable> outputValueType() {
		return IntWritable.class;
	}

	protected Class<Text> outputKeyType() {
		return Text.class;
	}

	@Bean
	@SuppressWarnings("unused")
	public Object pojoReducer() {

		return new Object() {
			/**
			 * another public method to throw the
			 * {@link org.springframework.hadoop.configuration.ReducerFactoryBean}
			 * off. Must be specified in the XML
			 * 
			 */
			public void doIt() {
			}

			public int reduce(Iterable<Integer> values) {
				int sum = 0;
				for (Integer val : values) {
					sum += val;
				}
				return sum;
			}
		};
	}

	@Bean
	@SuppressWarnings("unused")
	public Object pojoMapper() {
		return new Object() {
			/**
			 * another public method to throw the
			 * {@link org.springframework.hadoop.configuration.MapperFactoryBean}
			 * off. Must be specified in the XML
			 * 
			 */
			public void doIt() {
			}

			public void map(String value, Map<String, Integer> writer) {
				StringTokenizer itr = new StringTokenizer(value);
				while (itr.hasMoreTokens()) {
					writer.put(itr.nextToken(), 1);
				}
			}
		};
	}

	/**
	 * these are provided by the namespace
	 */
	@Autowired
	private Mapper<?, ?, ?, ?> mapper;

	/**
	 * these are provided by the namespace
	 */
	@Autowired
	private Reducer<?, ?, ?, ?> reducer;

}
