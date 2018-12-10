/**
 * @author Ahmad Shahwan
 */
module solutions.digamma.damas.rs {
    requires java.ws.rs;
    requires javax.inject;
    requires java.logging;
    requires java.xml.bind;

    requires solutions.digamma.damas.api;

    exports solutions.digamma.damas.rs.content
            to solutions.digamma.damas.rs.test;
    exports solutions.digamma.damas.rs.auth
            to solutions.digamma.damas.rs.test;
    exports solutions.digamma.damas.rs
            to solutions.digamma.damas.rs.test;
}