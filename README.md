# pulsar-admin-java

![License](https://img.shields.io/badge/license-Apache2.0-green) ![Language](https://img.shields.io/badge/language-Java-blue.svg) [![version](https://img.shields.io/github/v/tag/protocol-laboratory/pulsar-admin-java?label=release&color=blue)](https://github.com/protocol-laboratory/pulsar-admin-java/releases) [![codecov](https://codecov.io/gh/protocol-laboratory/pulsar-admin-java/branch/main/graph/badge.svg)](https://codecov.io/gh/protocol-laboratory/pulsar-admin-java)

This is a simple alternative to pulsar-admin built using the built-in HTTP client of the JDK. It minimizes project dependencies and requires a minimum of JDK 8.

Features:
- Basic functionality for managing Pulsar clusters 
- Built on top of the built-in HTTP client of the JDK 
- Minimizes project dependencies to provide a lightweight solution 
- Requires a minimum of JDK 8

Notices:
- currently disable hostname verification is not available. if you want to disable it ,please set property like : **System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true")**, but it's better to provide valid certificates.
