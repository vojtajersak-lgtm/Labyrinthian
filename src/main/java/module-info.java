module cz.cvut.fel.pjv.labyrinthian {

    requires javafx.controls;
    requires javafx.graphics;
    requires java.logging;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires javafx.fxml;
    requires com.google.gson;
    opens cz.cvut.fel.pjv.labyrinthian.UI to javafx.fxml;

    opens cz.cvut.fel.pjv.labyrinthian.Core to com.google.gson, javafx.fxml;
    opens cz.cvut.fel.pjv.labyrinthian.Entities to com.google.gson;
    opens cz.cvut.fel.pjv.labyrinthian.Items to com.google.gson;
    opens cz.cvut.fel.pjv.labyrinthian.Items.Consumables to com.google.gson;
    opens cz.cvut.fel.pjv.labyrinthian.Items.Weapon to com.google.gson;

    exports cz.cvut.fel.pjv.labyrinthian.UI;
    exports cz.cvut.fel.pjv.labyrinthian.Core;
    exports cz.cvut.fel.pjv.labyrinthian.Entities;
}