#!/bin/bash
client_pass=pulsar_client_pwd
server_pass=pulsar_server_pwd
server_dname="C=CN,ST=GD,L=SZ,O=sh,OU=sh,CN=localhost"
client_dname="C=CN,ST=GD,L=SZ,O=sh,OU=sh,CN=localhost"
echo "generate client keystore"
keytool -genkeypair -keypass $client_pass -storepass $client_pass -dname $client_dname -keyalg RSA -keysize 2048 -validity 3650 -keystore pulsar_client_key.jks
echo "generate server keystore"
keytool -genkeypair -keypass $server_pass -storepass $server_pass -dname $server_dname -keyalg RSA -keysize 2048 -validity 3650 -keystore pulsar_server_key.jks
echo "export server certificate"
keytool -exportcert -keystore pulsar_server_key.jks -file server.cer -storepass $server_pass
echo "export client certificate"
keytool -exportcert -keystore pulsar_client_key.jks -file client.cer -storepass $client_pass
echo "add server cert to client trust keystore"
keytool -importcert -keystore pulsar_client_trust.jks -file server.cer -storepass $client_pass -noprompt
echo "add client cert to server trust keystore"
keytool -importcert -keystore pulsar_server_trust.jks -file client.cer -storepass $server_pass -noprompt
rm -f server.cer
rm -f client.cer
