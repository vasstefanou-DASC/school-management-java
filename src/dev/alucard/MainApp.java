package dev.alucard;

import dev.alucard.gui.MainMenuWindow;

import javax.swing.*;

public class MainApp {

    public static void main(String[] args)  {
        SwingUtilities.invokeLater(MainMenuWindow::new);
    }
}
