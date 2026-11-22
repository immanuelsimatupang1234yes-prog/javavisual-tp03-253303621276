import com.unpri.charactermanager.model.Character;
import com.unpri.charactermanager.model.CharacterType;
import com.unpri.charactermanager.model.CharacterManager;
import com.unpri.charactermanager.view.MainWindow;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        // 1. Siapkan DATA terlebih dahulu (layer Model)
        CharacterManager manager = new CharacterManager();
        manager.addCharacter(new Character("Arjuna", CharacterType.WARRIOR, 5));
        manager.addCharacter(new Character("Merlin", CharacterType.MAGE, 3));
        manager.addCharacter(new Character("Athena", CharacterType.ARCHER, 4));
        manager.addCharacter(new Character("Elara", CharacterType.HEALER, 2));
        manager.addCharacter(new Character("Gandalf", CharacterType.MAGE, 10));
        manager.addCharacter(new Character("Gatotkaca", CharacterType.WARRIOR, 7));
        manager.addCharacter(new Character("Morgana", CharacterType.MAGE, 1));
        manager.addCharacter(new Character("Artemis", CharacterType.ARCHER, 6));

        // 2. Bukti: Model menolak data buruk walaupun tidak lewat form
        try {
            new Character("Merlin Tua", CharacterType.MAGE, 80);
        } catch (IllegalArgumentException ex) {
            System.out.println("Model menolak: " + ex.getMessage());
        }

        // 3. Jalankan GUI dengan AMAN di Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow(manager);
            window.setVisible(true);
        });
    }
}