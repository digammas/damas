package solutions.digamma.damas.logging;

import javax.inject.Singleton;

@Singleton
public class MethodLevelInvocationInterception {
    
    @Logged
    public void doSomething() {
    }
}
