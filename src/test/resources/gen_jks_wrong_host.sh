#!/bin/bash
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#

client_pass=pulsar_client_pwd
server_pass=pulsar_server_pwd
server_dname="C=CN,ST=GD,L=SZ,O=sh,OU=sh,CN=nonlocalhost"
client_dname="C=CN,ST=GD,L=SZ,O=sh,OU=sh,CN=nonlocalhost"
echo "generate client keystore"
keytool -genkeypair -keypass $client_pass -storepass $client_pass -dname $client_dname -keyalg RSA -keysize 2048 -validity 3650 -keystore pulsar_client_key_wrong_host.jks
echo "generate server keystore"
keytool -genkeypair -keypass $server_pass -storepass $server_pass -dname $server_dname -keyalg RSA -keysize 2048 -validity 3650 -keystore pulsar_server_key_wrong_host.jks
echo "export server certificate"
keytool -exportcert -keystore pulsar_server_key_wrong_host.jks -file server.cer -storepass $server_pass
echo "export client certificate"
keytool -exportcert -keystore pulsar_client_key_wrong_host.jks -file client.cer -storepass $client_pass
echo "add server cert to client trust keystore"
keytool -importcert -keystore pulsar_client_trust_wrong_host.jks -file server.cer -storepass $client_pass -noprompt
echo "add client cert to server trust keystore"
keytool -importcert -keystore pulsar_server_trust_wrong_host.jks -file client.cer -storepass $server_pass -noprompt
rm -f server.cer
rm -f client.cer
