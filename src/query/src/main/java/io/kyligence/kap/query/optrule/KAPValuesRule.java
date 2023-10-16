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
package io.kyligence.kap.query.optrule;

import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.core.Values;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.kylin.query.relnode.OlapRel;
import org.apache.kylin.query.relnode.OlapValuesRel;

public class KAPValuesRule extends ConverterRule {
    public static final KAPValuesRule INSTANCE = new KAPValuesRule();

    KAPValuesRule() {
        super(Values.class, Convention.NONE, OlapRel.CONVENTION, "KapValuesRule");
    }

    @Override
    public RelNode convert(RelNode rel) {
        Values values = (Values) rel;
        RelOptCluster cluster = values.getCluster();
        RelDataType rowType = values.getRowType();
        RelTraitSet relTraits = OlapValuesRel.replaceTraitSet(cluster, rowType, values.getTuples());
        return new OlapValuesRel(cluster, rowType, values.getTuples(), relTraits);
    }
}
