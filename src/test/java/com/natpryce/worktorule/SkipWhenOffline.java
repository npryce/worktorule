package com.natpryce.worktorule;

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class SkipWhenOffline implements TestRule {
    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate();
                } catch (IOException e) {
                    if (isOffline()) {
                        throw new AssumptionViolatedException("offline", e);
                    }
                    else {
                        throw e;
                    }
                }
            }
        };
    }

    public static boolean isOffline() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface i = interfaces.nextElement();
            if (!i.isLoopback() && i.isUp()) {
                return true;
            }
        }

        return false;
    }
}
