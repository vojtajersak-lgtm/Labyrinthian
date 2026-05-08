module cz.cvut.fel.pjv.labyrinthian {

    requires javafx.controls;
    requires javafx.graphics;
    requires java.logging;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires javafx.fxml;
    opens cz.cvut.fel.pjv.labyrinthian.UI to javafx.fxml;

    exports cz.cvut.fel.pjv.labyrinthian.Core;
    exports cz.cvut.fel.pjv.labyrinthian.Entities;
}