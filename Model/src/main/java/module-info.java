module pl.komponentowe.model {
    requires com.google.common;
    requires java.desktop;
    requires org.slf4j;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    exports pl.komponentowe.model;
    exports pl.komponentowe.model.exceptions;
}