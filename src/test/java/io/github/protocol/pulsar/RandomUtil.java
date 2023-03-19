/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.github.protocol.pulsar;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {

    public static Random random = new Random();

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static int randomInt() {
        return random.nextInt();
    }

    public static long randomLong() {
        return random.nextLong();
    }

    public static long randomPositiveLong(){
        return random.nextLong(0, Long.MAX_VALUE);
    }

    public static int randomPositiveInt(){
        return random.nextInt(0, Integer.MAX_VALUE);
    }

    public static int randomNegativeInt(){
        return random.nextInt(Integer.MIN_VALUE, -1);
    }

}
