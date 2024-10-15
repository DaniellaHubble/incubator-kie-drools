/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.drools.model.codegen.execmodel;

import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.drools.model.codegen.execmodel.domain.Person;
import org.drools.model.codegen.execmodel.domain.Address;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HalfBinaryTest extends BaseModelTest {

    public HalfBinaryTest(RUN_TYPE testRunType) {
        super(testRunType);
    }

    @Test
    public void testHalfBinary() {
        final String drl1 =
                "rule R1 when\n" +
                "    Integer(this > 2 && < 5)\n" +
                "then\n" +
                "end\n";

        KieSession ksession = getKieSession(drl1);

        ksession.insert(3);
        ksession.insert(4);
        ksession.insert(6);
        assertThat(ksession.fireAllRules()).isEqualTo(2);
    }

    @Test
    public void testHalfBinaryWithParenthesis() {
        final String drl1 =
                "rule R1 when\n" +
                "    Integer(intValue (> 2 && < 5))\n" +
                "then\n" +
                "end\n";

        KieSession ksession = getKieSession(drl1);

        ksession.insert(3);
        ksession.insert(4);
        ksession.insert(6);
        assertThat(ksession.fireAllRules()).isEqualTo(2);
    }

    @Test
    public void testComplexHalfBinary() {
        final String drl1 =
                "rule R1 when\n" +
                "    Integer(intValue ((> 2 && < 4) || (> 5 && < 7)))\n" +
                "then\n" +
                "end\n";

        KieSession ksession = getKieSession(drl1);

        ksession.insert(3);
        ksession.insert(4);
        ksession.insert(6);
        assertThat(ksession.fireAllRules()).isEqualTo(2);
    }

    @Test
    public void testHalfBinaryOnComparable() {
        final String drl1 =
                "rule R1 when\n" +
                "    String(this >= \"B\" && <= \"D\")\n" +
                "then\n" +
                "end\n";

        KieSession ksession = getKieSession(drl1);

        ksession.insert("B");
        ksession.insert("D");
        ksession.insert("H");
        ksession.insert("S");
        assertThat(ksession.fireAllRules()).isEqualTo(2);
    }

    @Test
    public void testHalfBinaryOrOnComparable() {
        final String drl1 =
                "rule R1 when\n" +
                "    String(this == \"B\" || == \"D\")\n" +
                "then\n" +
                "end\n";

        KieSession ksession = getKieSession(drl1);

        ksession.insert("B");
        ksession.insert("D");
        ksession.insert("H");
        ksession.insert("S");
        assertThat(ksession.fireAllRules()).isEqualTo(2);
    }

    @Test
    public void testComplexHalfBinaryOnComparable() {
        final String drl1 =
                "rule R1 when\n" +
                "    String(this == \"B\" || == \"D\" || == \"S\")\n" +
                "then\n" +
                "end\n";

        KieSession ksession = getKieSession(drl1);

        ksession.insert("B");
        ksession.insert("D");
        ksession.insert("Q");
        ksession.insert("S");
        assertThat(ksession.fireAllRules()).isEqualTo(2);
    }

    @Test
    public void testComplexHalfBinaryOnComparableField() {
        final String drl1 =
                "rule R1 when\n" +
                "    Person(name == \"B\" || == \"D\" || == \"S\")\n" +
                "then\n" +
                "end\n";

        KieSession ksession = getKieSession(drl1);

        ksession.insert(new Person("B"));
        ksession.insert(new Person("D"));
        ksession.insert(new Person("Q"));
        ksession.insert(new Person("S"));
        assertThat(ksession.fireAllRules()).isEqualTo(2);
    }

    @Test
    public void testComplexHalfBinaryOnComparableInternalField() {
        final String drl1 =
                "rule R1 when\n" +
                "    Person(address.zipCode == \"B\" || == \"D\")\n" +
                "then\n" +
                "end\n";

        KieSession ksession = getKieSession(drl1);

        Person b = new Person("B");
        b.setAddress(new Address("B"));
        Person d = new Person("D");
        d.setAddress(new Address("D"));
        Person q = new Person("Q");
        q.setAddress(new Address("Q"));
        Person s = new Person("S");
        s.setAddress(new Address("S"));

        ksession.insert(b);
        ksession.insert(d);
        ksession.insert(q);
        ksession.insert(s);
        assertThat(ksession.fireAllRules()).isEqualTo(2);
    }
}
