package dev.alucard;

import dev.alucard.gui.MainMenuWindow;

import javax.swing.*;

public class MainApp {

    static void main()  {
        SwingUtilities.invokeLater(MainMenuWindow::new);
    }
}
