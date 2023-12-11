/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.kylin.rest;

import static org.apache.kylin.common.persistence.metadata.jdbc.JdbcUtil.datasourceParameters;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.kylin.common.KylinConfig;
import org.apache.kylin.common.exception.KylinRuntimeException;
import org.apache.kylin.common.persistence.metadata.JdbcDataSource;
import org.apache.kylin.common.util.HostInfoFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Profile("!dev")
@Slf4j
public class InitConfiguration {
    @Autowired
    private HostInfoFetcher hostInfoFetcher;

    @PostConstruct
    public void init() {
        if (KylinConfig.getInstanceFromEnv().isCheckHostname() && hostInfoFetcher.getHostname().indexOf("_") != -1) {
            throw new KylinRuntimeException("The hostname does not support containing '_' characters,"
                    + " please modify the hostname of Kylin 5.0 nodes.");
        }
    }

    @Bean
    public DataSource dataSource() throws Exception {
        val url = KylinConfig.getInstanceFromEnv().getMetadataUrl();
        val props = datasourceParameters(url);
        if (!url.getScheme().equals("jdbc")) {
            throw new RuntimeException("Failed to init jdbc data source!");
        }
        return JdbcDataSource.getDataSource(props);
    }
}
