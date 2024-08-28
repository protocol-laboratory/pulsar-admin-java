package io.github.protocol.pulsar.admin.jdk;

public class PAdmin {
    public static void main(String[] args) {
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder().port(8080).build();
        Tenants tenants = pulsarAdmin.tenants();
        System.out.println(tenants);
    }
}
